package com.demo.online.scenes.tasks.preview

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.User
import com.demo.online.ActivityActions
import com.demo.online.BR
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.constants.Constants.Companion.NODE_LOADER_ID
import com.demo.online.databinding.PreviewFragmentBinding

import com.demo.online.helpers.recyclerViewAdapters.SimpleAdapter
import com.demo.online.helpers.recyclerViewAdapters.Type
import com.demo.online.helpers.sytemResourse.PreferenceHelper

class PreviewFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var mListener: MainActivity? =null

    override fun onRefresh() {
        loaderManager.getLoader<NodeLoader>(NODE_LOADER_ID).forceLoad()
    }

    inner class NodeLoader(private var nodeName:String):LoaderManager.LoaderCallbacks<ArrayList<NodeItem>> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<NodeItem>> {
            val preference = PreferenceHelper.defaultPrefs(context)
            val user = Gson().fromJson(preference.getString("user",null), User::class.java)
            val taskId = preference.getInt("task_id",0)
            val api = API(user.Id,user.Hash)
            return NodeLoader(context, api,taskId,nodeName)
        }


        override fun onLoadFinished(loader: Loader<ArrayList<NodeItem>>?, data: ArrayList<NodeItem>) {
            vm?.updateNodes(data)
        }

        override fun onLoaderReset(loader: Loader<ArrayList<NodeItem>>?) {
            loader?.reset()
        }
    }

    private var vm:PreviewVM?=null
    private var binding: PreviewFragmentBinding?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding  = DataBindingUtil.inflate(inflater!! , R.layout.preview_fragment,container, false)
        val myView : View  = binding?.root!!
        vm= PreviewVM("")
        binding?.vm=vm
        binding?.executePendingBindings()
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderManager.initLoader(NODE_LOADER_ID, null, NodeLoader(""))
        setHasOptionsMenu(true);

        loaderManager.getLoader<NodeLoader>(NODE_LOADER_ID).forceLoad()
        //swipeRefreshLayoutArchive.setOnRefreshListener({})
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_archives, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActivityActions) {
            mListener = context as MainActivity
        } else {
            throw RuntimeException(context!!.toString() + " must implement ActivityActions")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener=null
    }

/*
    private val typeNode = Type<FragmentNodeListItemBinding>(R.layout.fragment_node_list_item)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick {
                //selectNode(it.binding.item?.archive?.Title!!)
                activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}")
            }
            .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }
*/

    private fun Context?.toast(text: String) = this?.let { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }

}