package com.demo.online.scenes.archives

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.ActionMode
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.Archive
import com.demo.api.Scene
import com.demo.api.User
import com.demo.online.ActivityActions
import com.demo.online.BR
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.constants.Constants.Companion.ARCHIVE_LOADER_ID
import com.demo.online.databinding.FragmentArchiveListItemBinding
import com.demo.online.helpers.recyclerViewAdapters.ActionModeCallbacks

import com.demo.online.helpers.recyclerViewAdapters.SimpleAdapter
import com.demo.online.helpers.recyclerViewAdapters.Type
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import com.demo.online.scenes.SceneListFragment
import kotlinx.android.synthetic.main.fragment_archive_list.*

class ArchiveListFragment : Fragment(), ArchiveListEvents, SwipeRefreshLayout.OnRefreshListener, ActionModeCallbacks {

    private var mListener: MainActivity? =null

    inner class SceneCreatorLoader(private var archive: Archive): LoaderManager.LoaderCallbacks<Scene> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Scene> {
            val preference = PreferenceHelper.defaultPrefs(context)
            val user = Gson().fromJson(preference.getString("user",null), User::class.java)
            val sceneId = preference.getString("sceneId",null).toInt()
            val api = API(user.Id, user.Hash)
            return SceneCreator(context,api,archive);
        }


        override fun onLoadFinished(loader: Loader<Scene>?, data: Scene) {
            // mListener?.replaceFragment(TaskListFragment.newInstance("create",sceneId!!))
            val WHAT = 1
            val handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    if (msg.what === WHAT) mListener?.replaceFragment(SceneListFragment.newInstance("Scenes"))
                }
            }
            handler.sendEmptyMessage(WHAT)
        }

        //
        //getLoaderManager().destroyLoader(id);
        override fun onLoaderReset(loader: Loader<Scene>?) {
            loader?.reset()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                ArchiveListFragment().apply {
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

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_archives_actions, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean {
        for(title:String in selectedArchivesTitles) {
            val archive = Archive()
            archive.Title=title
            loaderManager.initLoader(16, null, SceneCreatorLoader(archive)).forceLoad()
        }
        UnSelectItems()
        mode.finish()
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        selectedArchivesTitles.clear()
        UnSelectItems()
        mode.finish()
        supportMode =null
    }

    private var supportMode: ActionMode? = null

    private fun SwitchAppBarMode(itemExist:Boolean)
    {
        if(itemExist && supportMode==null) {
            supportMode = mListener?.startSupportActionMode(this)
            supportMode?.title = "1 scene selected"
        }
        else if(itemExist) {
            supportMode?.title = selectedArchivesTitles.size.toString()+" scenes selected"
        }
        else if(supportMode != null && !itemExist) {
            supportMode?.finish()
        }
    }

    private var selectedArchivesTitles:ArrayList<String>  = ArrayList()



    override fun selectArchive(archive_title: String) {
        if(!selectedArchivesTitles.contains(archive_title)) {
            selectedArchivesTitles.add(archive_title)
        }
        else {
            selectedArchivesTitles.removeAt(selectedArchivesTitles.indexOf(archive_title))
        }
        SwitchAppBarMode(selectedArchivesTitles.size>0)
    }

    override fun removeArchive(archive: Archive) {
        val preference = PreferenceHelper.defaultPrefs(context)
        val user = Gson().fromJson(preference.getString("user",null), User::class.java)
        val api = API(
                user.Id,
                user.Hash
        )
        object : Thread() {
            override fun run() {
                api.RemoveArchive(archive)
            }
        }.start()
    }

    private fun UnSelectItems() {
        (archive_list.adapter as SimpleAdapter).unSelectItems()
    }

    override fun onRefresh() {
        loaderManager.getLoader<ArchiveLoader>(ARCHIVE_LOADER_ID).forceLoad()
    }

    inner class ArchiveLoader: LoaderManager.LoaderCallbacks<ArrayList<ArchiveItem>> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<ArchiveItem>> {
            val preference = PreferenceHelper.defaultPrefs(context)
            val user = Gson().fromJson(preference.getString("user",null), User::class.java)
            val api = API(user.Id,user.Hash)
            return ArchiveLoader(context, api)
        }


        override fun onLoadFinished(loader: Loader<ArrayList<ArchiveItem>>?, data: ArrayList<ArchiveItem>) {
            SimpleAdapter(data.toMutableList(), BR.item, true,true).type { item, position ->
                when (item) {
                    is ArchiveItem -> typeArchive
                    else -> null
                }
            }.into(archive_list)
            archives_not_exist.isRefreshing=false
        }

        override fun onLoaderReset(loader: Loader<ArrayList<ArchiveItem>>?) {
            loader?.reset()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_archive_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderManager.initLoader(ARCHIVE_LOADER_ID, null, ArchiveLoader())
        setHasOptionsMenu(true);
        loaderManager.getLoader<ArchiveLoader>(ARCHIVE_LOADER_ID).forceLoad()
        swipeRefreshLayoutArchive.setOnRefreshListener({loaderManager.getLoader<ArchiveLoader>(ARCHIVE_LOADER_ID).forceLoad()})
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


    private val typeArchive = Type<FragmentArchiveListItemBinding>(R.layout.fragment_archive_list_item)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick {
                selectArchive(it.binding.item?.archive?.Title!!)
                activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}")
            }
            .onLongClick { activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}") }
            .onSwipe {
                removeArchive(((archive_list.adapter as SimpleAdapter).list[it] as ArchiveItem).archive)
            }

    private fun Context?.toast(text: String) = this?.let { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }

}