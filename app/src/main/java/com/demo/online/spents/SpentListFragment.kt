package com.demo.online.spents

import android.content.Context
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
import com.demo.online.scenes.tasks.TaskItem
import com.demo.online.R
import com.demo.online.constants.Constants.Companion.SPENTS_LOADER_ID
import com.demo.online.databinding.FragmentSpentsListItemBinding
import com.demo.online.helpers.recyclerViewAdapters.SimpleAdapter
import com.demo.online.helpers.recyclerViewAdapters.Type
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_spents.*

class SpentListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        loaderManager.getLoader<SpentsLoader>(SPENTS_LOADER_ID).forceLoad()
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                SpentListFragment().apply {
                    arguments = Bundle().apply {
                        putString("title", title)
                        // putString(ARG_PARAM2, param2)
                    }
                }
    }
    override fun onResume() {
        super.onResume()
        // Set title
        mListener?.title = arguments?.getString("title")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mListener?.title=it.getString("title")
            // param2 = it.getString(ARG_PARAM2)
        }
    }

    inner class SpentsLoader : LoaderManager.LoaderCallbacks<ArrayList<TaskItem>> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<TaskItem>> {
            val preference = PreferenceHelper.defaultPrefs(context)
            val user = Gson().fromJson(preference.getString("user",null), User::class.java)
            val api = API(user.Id, user.Hash)
            return SpentLoader(context, api)
        }


        override fun onLoadFinished(loader: Loader<ArrayList<TaskItem>>?, data: ArrayList<TaskItem>) {
            SimpleAdapter(data.toMutableList(), BR.item, false,true).type { item, _ ->
                when (item) {
                    is TaskItem -> typeTask
                    else -> null
                }
            }.into(spents_list)
            //tasks_not_exist.isRefreshing=false

        }

        override fun onLoaderReset(loader: Loader<ArrayList<TaskItem>>?) {
            loader?.reset()
        }
    }

    private var mListener: MainActivity? =null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_spents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderManager.initLoader(SPENTS_LOADER_ID, null, SpentsLoader())
        setHasOptionsMenu(true)
        swipeRefreshLayoutSpents.setOnRefreshListener({loaderManager.getLoader<SpentsLoader>(SPENTS_LOADER_ID).forceLoad()})
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tasks, menu)
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

    private val typeTask = Type<FragmentSpentsListItemBinding>(R.layout.fragment_spents_list_item)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick {

                activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}")
            }
            .onLongClick {

                activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}")
            }
            .onChildElementClick {
            }

    private fun Context?.toast(text: String) = this?.let { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }
}