package com.demo.online.scenes.logs

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.ActionMode
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.User
import com.demo.online.ActivityActions
import com.demo.online.BR
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.constants.Constants.Companion.LOGS_LOADER_ID
import com.demo.online.databinding.FragmentSceneListItemBinding
import com.demo.online.databinding.LogsListItemBinding
import com.demo.online.helpers.recyclerViewAdapters.ActionModeCallbacks
import com.demo.online.helpers.recyclerViewAdapters.SimpleAdapter
import com.demo.online.helpers.recyclerViewAdapters.Type
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import com.demo.online.scenes.archives.ArchiveListFragment
import com.demo.online.scenes.tasks.TaskListFragment
import kotlinx.android.synthetic.main.fragment_scene_list.*


class LogListDialog : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<LogItem>>, SwipeRefreshLayout.OnRefreshListener {



    override fun onRefresh() {
        loaderManager.getLoader<LogLoader>(LOGS_LOADER_ID).forceLoad()
    }

    private var mListener: MainActivity? =null


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<LogItem>> {
        val preference = PreferenceHelper.defaultPrefs(context)
        val user = Gson().fromJson(preference.getString("user",null), User::class.java)
        val api = API(user.Id,user.Hash)
        val sceneId = preference.getString("sceneId",null).toInt()
        return LogLoader(context, api,sceneId)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<LogItem>>?, data: ArrayList<LogItem>) {
        SimpleAdapter(data.toMutableList(), BR.item, false,true).type { item, _ ->
            when (item) {
                is LogItem -> typeLog
                else -> null
            }
        }.into(scene_list)
        scenes_not_exist.isRefreshing=false
    }

    override fun onLoaderReset(loader: Loader<ArrayList<LogItem>>?) {
        loader?.reset()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_scene_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderManager.initLoader(LOGS_LOADER_ID, null, this)
        setHasOptionsMenu(true)
        swipeRefreshLayoutScenes.setOnRefreshListener({loaderManager.getLoader<LogLoader>(LOGS_LOADER_ID).forceLoad()})
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_scenes, menu)
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

    private val typeLog = Type<LogsListItemBinding>(R.layout.logs_list_item)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick {

            }
            .onLongClick {

            }

    private fun Context?.toast(text: String) = this?.let { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }
}

