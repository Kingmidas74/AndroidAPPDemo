package com.demo.online.scenes

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
import com.demo.online.constants.Constants.Companion.SCENE_LOADER_ID
import com.demo.online.databinding.FragmentSceneListItemBinding
import com.demo.online.helpers.recyclerViewAdapters.ActionModeCallbacks
import com.demo.online.helpers.recyclerViewAdapters.SimpleAdapter
import com.demo.online.helpers.recyclerViewAdapters.Type
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import com.demo.online.scenes.archives.ArchiveListFragment
import com.demo.online.scenes.logs.LogListDialog
import com.demo.online.scenes.tasks.TaskListFragment
import kotlinx.android.synthetic.main.fragment_scene_list.*


class SceneListFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<SceneItem>>, SceneListEvents, SwipeRefreshLayout.OnRefreshListener, ActionModeCallbacks {

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                SceneListFragment().apply {
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
        mode.menuInflater.inflate(R.menu.menu_scenes_actions, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        selectedSecenesIds.clear()
        unSelectScenes()
        mode.finish()
        supportMode=null
    }

    private var supportMode: ActionMode? = null

    private fun SwitchAppBarMode(itemExist:Boolean)
    {
        if(itemExist && supportMode==null) {
            supportMode = mListener?.startSupportActionMode(this)
            supportMode?.title = "1 scene selected"
        }
        else if(itemExist) {
            supportMode?.title = selectedSecenesIds.size.toString()+" scenes selected"
        }
        else if(supportMode != null && !itemExist) {
            supportMode?.finish()
        }
    }

    private var selectedSecenesIds:ArrayList<Int>  = ArrayList()


    override fun selectScene(scene: SceneItem) {
        if(!selectedSecenesIds.contains(scene.scene.Id)) {
            selectedSecenesIds.add(scene.scene.Id)
        }
        else {
            selectedSecenesIds.removeAt(selectedSecenesIds.indexOf(scene.scene.Id))
        }
        SwitchAppBarMode(selectedSecenesIds.size>0)
    }

    private fun unSelectScenes() {
        (scene_list.adapter as SimpleAdapter).unSelectItems()
    }

    override fun onRefresh() {
      //  loaderManager.getLoader<SceneLoader>(SCENE_LOADER_ID).forceLoad()
    }

    private var mListener: MainActivity? =null


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<SceneItem>> {
        val preference = PreferenceHelper.defaultPrefs(context)
        val user = Gson().fromJson(preference.getString("user",null), User::class.java)
        val api = API(user.Id,user.Hash)
        return SceneLoader(context, api)
    }

    override fun showTasks(scene: SceneItem) {
        SwitchAppBarMode(false)
        PreferenceHelper.defaultPrefs(context).edit().putString("sceneId", scene.scene.Id.toString()).apply()
        mListener?.replaceFragment(TaskListFragment.newInstance(scene.scene.Title,scene.scene.Id))
    }

    override fun onLoadFinished(loader: Loader<ArrayList<SceneItem>>?, data: ArrayList<SceneItem>) {
        SimpleAdapter(data.toMutableList(), BR.item, false,true).type { item, _ ->
            when (item) {
                is SceneItem -> typeScene
                else -> null
            }
        }.into(scene_list)
        scenes_not_exist.isRefreshing=false
    }

    override fun onLoaderReset(loader: Loader<ArrayList<SceneItem>>?) {
        loader?.reset()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_scene_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderManager.initLoader(SCENE_LOADER_ID, null, this)
        setHasOptionsMenu(true)
        add_scene_fab.setOnClickListener {
            supportMode?.finish()
            mListener?.replaceFragment(ArchiveListFragment())
        }
        swipeRefreshLayoutScenes.setOnRefreshListener({loaderManager.getLoader<SceneLoader>(SCENE_LOADER_ID).forceLoad()})
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

    private val typeScene = Type<FragmentSceneListItemBinding>(R.layout.fragment_scene_list_item)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick {
                showTasks((it.binding.item!!))
                activity.toast("Clicked #${it.adapterPosition}: ${it.binding.item}")
            }
            .onLongClick {
                selectScene(it.binding.item!!)
                activity.toast("Long-clicked #${it.adapterPosition}: ${it.binding.item}")
            }
            .onChildElementClick {holder->
                holder.binding.sceneLogsBtn.setOnClickListener {
                    PreferenceHelper.defaultPrefs(context).edit().putString("sceneId", holder.binding.item?.scene?.Id.toString()).apply()
                    mListener?.replaceFragment(LogListDialog())
                }
            }

    private fun Context?.toast(text: String) = this?.let { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }
}

