package com.demo.online.helpers.recyclerViewAdapters

import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.databinding.OnRebindCallback
import android.databinding.ViewDataBinding
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.ProgressBar
import kotlin.math.max

class SimpleAdapter(val list: MutableList<Any>,
                    private val variable: Int? = null,
                    private val isSingleClickSelected:Boolean=false,
                    stableIds: Boolean = false)
    : RecyclerView.Adapter<Holder<ViewDataBinding>>() {

    private var mSelectedItems = SparseBooleanArray()

    fun onItemDismiss(position: Int) {
        if(position>=0) {
            var idx =(list[position] as EntityItem).stableId
            (getType(position) as Type<ViewDataBinding>).onSwipe?.invoke(position)
            list.removeAt(position)
            notifyItemRangeRemoved(position,1)
        }
    }

    fun isSelectedMode(): Boolean {
        for(item:Any in list)
        {
            if((item as EntityItem).IsSelected)
            {
                return true
            }
        }
        return false
    }
/*
    constructor(list: MutableList<Any>) : this(list, null, false)
    constructor(list: MutableList<Any>, variable: Int) : this(list, variable, false)
    constructor(list: MutableList<Any>, stableIds: Boolean) : this(list, null, stableIds)*/

    private val DATA_INVALIDATION = Any()
    private val callback = ObservableListCallback(this)
    private var recyclerView: RecyclerView? = null
    private var inflater: LayoutInflater? = null
    private var scrollViewContent: SwipeRefreshLayout?=null
    /*private var scrollViewEmptyData: SwipeRefreshLayout?=null
    private var progressBar:ProgressBar?=null*/

    private val map = mutableMapOf<Class<*>, BaseType>()
    private var layoutHandler: LayoutHandler? = null
    private var typeHandler: TypeHandler? = null

    init {
        setHasStableIds(stableIds)
    }

   /* @JvmOverloads
    fun <T : Any> map(clazz: Class<T>, layout: Int, variable: Int? = null)
            = apply { map[clazz] = BaseType(layout, variable) }

    inline fun <reified T : Any> map(layout: Int, variable: Int? = null)
            = map(T::class.java, layout, variable)

    fun <T : Any> map(clazz: Class<T>, type: AbsType<*>)
            = apply { map[clazz] = type }

    inline fun <reified T : Any> map(type: AbsType<*>)
            = map(T::class.java, type)

    inline fun <reified T : Any, B : ViewDataBinding> map(layout: Int,
                                                          variable: Int? = null,
                                                          noinline f: (Type<B>.() -> Unit)? = null)
            = map(T::class.java, Type<B>(layout, variable).apply { f?.invoke(this) })*/

    fun handler(handler: Handler) = apply {
        when (handler) {
            is LayoutHandler -> {
                if (variable == null) {
                    throw IllegalStateException("No variable specified in LastAdapter constructor")
                }
                layoutHandler = handler
            }
            is TypeHandler -> typeHandler = handler
        }
    }

    inline fun layout(crossinline f: (Any, Int) -> Int) = handler(object : LayoutHandler {
        override fun getItemLayout(item: Any, position: Int) = f(item, position)
    })

    inline fun type(crossinline f: (Any, Int) -> AbsType<*>?) = handler(object : TypeHandler {
        override fun getItemType(item: Any, position: Int) = f(item, position)
    })

    fun into(recyclerView: RecyclerView) = apply { recyclerView.adapter = this }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): Holder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(this.inflater!!, viewType, view, false)
        val holder = Holder(binding)
        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding) = recyclerView?.isComputingLayout ?: false
            override fun onCanceled(binding: ViewDataBinding) {
                if (recyclerView?.isComputingLayout != false) {
                    return
                }
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION)
                }
            }
            //override fun onItemRangeRemoved()
        })
        return holder
    }



    override fun onBindViewHolder(holder: Holder<ViewDataBinding>, position: Int) {
        val type = getType(position)!!
        holder.binding.setVariable(getVariable(type), list[position])
        holder.binding.executePendingBindings()
        @Suppress("UNCHECKED_CAST")
        if (type is AbsType<*>) {
            if (!holder.created) {
                notifyCreate(holder, type as AbsType<ViewDataBinding>)
            }
            notifyBind(holder, type as AbsType<ViewDataBinding>)
        }
    }

    override fun onBindViewHolder(holder: Holder<ViewDataBinding>, position: Int, payloads: List<Any>) {
        if (isForDataBinding(payloads)) {
            holder.binding.executePendingBindings()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onViewRecycled(holder: Holder<ViewDataBinding>) {
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION && position < list.size) {
            val type = getType(position)!!
            if (type is AbsType<*>) {
                @Suppress("UNCHECKED_CAST")
                notifyRecycle(holder, type as AbsType<ViewDataBinding>)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            val item = list[position]
            if (item is StableId) {
                item.stableId
            } else {
                throw IllegalStateException("${item.javaClass.simpleName} must implement StableId interface.")
            }
        } else {
            super.getItemId(position)
        }
    }

    override fun getItemCount() = list.size

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        if (recyclerView == null && list is ObservableList) {
            list.addOnListChangedCallback(callback)
        }

        recyclerView = rv
        recyclerView?.layoutManager=LinearLayoutManager(rv.context)



        ItemTouchHelper(SimpleItemTouchHelperCallback(this)).attachToRecyclerView(recyclerView)

        val itemAnimator:RecyclerView.ItemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 1000
        itemAnimator.removeDuration = 1000
        recyclerView?.itemAnimator = itemAnimator

        scrollViewContent = (rv.parent as SwipeRefreshLayout)
        if(itemCount>0) {
            scrollViewContent?.visibility = View.VISIBLE
        }
        else scrollViewContent?.visibility= View.GONE
        scrollViewContent?.isRefreshing=false
        inflater = LayoutInflater.from(rv.context)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        if (recyclerView != null && list is ObservableList) {
            list.removeOnListChangedCallback(callback)
        }
        recyclerView = null
        scrollViewContent?.visibility=View.GONE
    }

    override fun getItemViewType(position: Int)
            = layoutHandler?.getItemLayout(list[position], position)
            ?: typeHandler?.getItemType(list[position], position)?.layout
            ?: getType(position)?.layout
            ?: throw RuntimeException("Invalid object at position $position: ${list[position].javaClass}")

    private fun getType(position: Int)
            = typeHandler?.getItemType(list[position], position)
            ?: map[list[position].javaClass]

    private fun getVariable(type: BaseType)
            = type.variable
            ?: variable
            ?: throw IllegalStateException("No variable specified for type ${type.javaClass.simpleName}")

    private fun isForDataBinding(payloads: List<Any>): Boolean {
        if (payloads.isEmpty()) {
            return false
        }
        payloads.forEach {
            if (it != DATA_INVALIDATION) {
                return false
            }
        }
        return true
    }

    private fun notifyCreate(holder: Holder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> {
                setClickListeners(holder, type)
                type.onCreate?.invoke(holder)
            }
            is ItemType -> type.onCreate(holder)
        }
        holder.created = true
    }

    private fun notifyBind(holder: Holder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> {
                type.onBind?.invoke(holder)
                type.onChildElementClick?.invoke(holder)
                // type.onSwipe?.invoke(holder)
            }
            is ItemType -> type.onBind(holder)
        }
        //holder.itemView.isSelected = mSelectedItems.get((list[holder.adapterPosition] as EntityItem).stableId.toInt())

    }

    private fun notifyRecycle(holder: Holder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> type.onRecycle?.invoke(holder)
            is ItemType -> type.onRecycle(holder)
        }
    }

    fun unSelectItems() {
        for(item:Any in list)
        {
            mSelectedItems.put((item as EntityItem).stableId.toInt(), false)
            item.IsSelected=false
        }
        notifyDataSetChanged()
    }


    private fun onSelectItem(holder: Holder<ViewDataBinding>) {
        if(holder.adapterPosition>=0) {
            val position = max(holder.adapterPosition, 0)
            val element = (list[position] as EntityItem)
            val currentState = (list[position] as EntityItem).IsSelected
            val nextState = !currentState

            element.IsSelected = nextState
            mSelectedItems.put(
                    element.stableId.toInt(),
                    nextState
            )
            notifyItemRangeChanged(position, 1)
        }
    }



    private fun setClickListeners(holder: Holder<ViewDataBinding>, type: Type<ViewDataBinding>) {
        val onClick = type.onClick
        if (onClick != null) {
            holder.itemView.setOnClickListener {
                if(!isSelectedMode()) {
                    onClick(holder)
                }
                if (isSingleClickSelected) {
                    onSelectItem(holder)
                }
            }
        }
        val onLongClick = type.onLongClick
        if (onLongClick != null) {
            holder.itemView.setOnLongClickListener {
                onLongClick(holder)
                if(!isSingleClickSelected) {
                    onSelectItem(holder)
                }
                true
            }
        }
    }

    fun switchExpendItem(position: Int) {
        (list[max(position,0)] as EntityItem).IsExpended = !(list[max(position,0)] as EntityItem).IsExpended//.xor(true)
        notifyItemChanged(position)
    }
}