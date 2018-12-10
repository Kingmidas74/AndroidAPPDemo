package com.demo.online.scenes.tasks

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager.LoaderCallbacks
import android.support.v4.content.Loader
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.view.ActionMode
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.RenderSettings
import com.demo.api.User
import com.demo.online.ActivityActions
import com.demo.online.BR
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.constants.Constants.Companion.RENDER_SETTINGS_LOADER_ID
import com.demo.online.constants.Constants.Companion.TASK_LOADER_ID
import com.demo.online.databinding.FragmentTaskListItemBinding
import com.demo.online.helpers.recyclerViewAdapters.ActionModeCallbacks
import com.demo.online.helpers.recyclerViewAdapters.SimpleAdapter
import com.demo.online.helpers.recyclerViewAdapters.Type
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import com.demo.online.scenes.tasks.preview.PreviewFragment
import com.demo.online.scenes.tasks.settings.SimpleTaskSettingsFragment
import com.demo.online.scenes.tasks.settings.TaskCORONASettingsFragment
import com.demo.online.scenes.tasks.settings.TaskDEFAULTSettingsFragment
import com.demo.online.scenes.tasks.settings.TaskVRAYSettingsFragment
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : Fragment(), TaskListEvents, SwipeRefreshLayout.OnRefreshListener,ActionModeCallbacks {
    override fun onRefresh() {
        loaderManager.getLoader<TaskLoader>(TASK_LOADER_ID).forceLoad()
    }

    companion object {
        const val WHAT = 1
        @JvmStatic
        fun newInstance(title: String, sceneId: Int) =
                TaskListFragment().apply {
                    arguments = Bundle().apply {
                        putString("title", title)
                        putInt("sceneId",sceneId)
                    }
                }
    }
    override fun onResume() {
        super.onResume()
        // Set title
        mListener?.title = arguments?.getString("title")
    }

    inner class SettingsLoader(private var task: TaskItem?): LoaderCallbacks<RenderSettings> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<RenderSettings> {
            val preference = PreferenceHelper.defaultPrefs(context)
            val user = Gson().fromJson(preference.getString("user",null), User::class.java)
            val api = API(user.Id, user.Hash)
            return if(task!==null) {
                SettingsLoader(context, api, sceneId!!, task?.task?.Id!!)
            } else {
                SettingsLoader(context, api, sceneId!!)
            }
        }


        override fun onLoadFinished(loader: Loader<RenderSettings>?, data: RenderSettings) {
            PreferenceHelper.defaultPrefs(context).edit().putString("render_settings", Gson().toJson(data).toString()).apply()
            val handler = @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message) {

                    var newTaskTitle = "New task..."
                    if(task!=null) {
                        newTaskTitle = task?.task?.Title!!
                    }
                    /*when {
                        data.Render.contains("V_RAY") -> mListener?.replaceFragment(TaskVRAYSettingsFragment(), R.id.task_vray_settings_tabs, newTaskTitle, true)
                        data.Render.contains("CORONA") -> mListener?.replaceFragment(TaskCORONASettingsFragment(), R.id.task_corona_settings_tabs, newTaskTitle, true)
                        else -> mListener?.replaceFragment(TaskDEFAULTSettingsFragment(), R.id.task_default_settings_tabs, newTaskTitle, true)

                    }*/
                    mListener?.replaceFragment(SimpleTaskSettingsFragment.newInstance(newTaskTitle, sceneId!!))
                    loaderManager.destroyLoader(RENDER_SETTINGS_LOADER_ID)
                }
            }
            handler.sendEmptyMessage(WHAT)

        }

        //
        //getLoaderManager().destroyLoader(id);
        override fun onLoaderReset(loader: Loader<RenderSettings>?) {
            loader?.reset()
        }
    }



    inner class TasksLoader : LoaderCallbacks<ArrayList<TaskItem>> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<TaskItem>> {
            val preference = PreferenceHelper.defaultPrefs(context)
            val user = Gson().fromJson(preference.getString("user",null), User::class.java)
            val sceneId = preference.getString("sceneId",null).toInt()
            val api = API(user.Id, user.Hash)
            return TaskLoader(context, api, sceneId)
        }


        override fun onLoadFinished(loader: Loader<ArrayList<TaskItem>>?, data: ArrayList<TaskItem>) {
           SimpleAdapter(data.toMutableList(), BR.item, false, true).type { item, _ ->
                when (item) {
                    is TaskItem -> typeTask
                    else -> null
                }
            }.into(task_list)
            tasks_not_exist.isRefreshing = false
            loaderManager.destroyLoader(TASK_LOADER_ID);
        }

        override fun onLoaderReset(loader: Loader<ArrayList<TaskItem>>?) {
            loader?.reset()
        }
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_tasks_actions, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean {

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        selectedTasksIds.clear()
        unSelectTasks()
        mode.finish()
        supportMode=null
    }

    private var supportMode: ActionMode? = null

    private fun switchAppBarMode(itemExist:Boolean)
    {
        if(itemExist && supportMode==null) {
            supportMode = mListener?.startSupportActionMode(this)!!
            supportMode?.title = "1 task selected"
        }
        else if(itemExist) {
            supportMode?.title = selectedTasksIds.size.toString()+" tasks selected"
        }
        else if(supportMode != null && !itemExist) {
            supportMode?.finish()
        }
    }

    private var selectedTasksIds:ArrayList<Int>  = ArrayList()

    private fun unSelectTasks() {
        (task_list.adapter as SimpleAdapter).unSelectItems()
    }

    override fun selectTask(task: TaskItem) {
        if(!selectedTasksIds.contains(task.task.Id)) {
            selectedTasksIds.add(task.task.Id)
        }
        else {
            selectedTasksIds.removeAt(selectedTasksIds.indexOf(task.task.Id))
        }
        switchAppBarMode(selectedTasksIds.size>0)
    }
    private var title: String? = null
    private var sceneId:Int?=null

    private var mListener: MainActivity? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
            sceneId=it.getInt("sceneId")
            mListener?.title=title
            // param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun showTaskInfo(task: TaskItem) {
        //mListener?.showTaskInfo(task)
        loaderManager.initLoader(RENDER_SETTINGS_LOADER_ID, null, SettingsLoader(task)).forceLoad()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(_view==null) {
            _view = inflater?.inflate(R.layout.fragment_task_list, container, false)
        }
        return _view;
    }

    private var _view:View? = null;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        add_task_fab.setOnClickListener {
            supportMode?.finish()
            loaderManager.initLoader(RENDER_SETTINGS_LOADER_ID, null, SettingsLoader(null)).forceLoad()
            //mListener?.replaceFragment(TaskVRAYSettingsFragment(),R.id.task_vray_settings_tabs,"New task",true)
        }
        loaderManager.initLoader(TASK_LOADER_ID, null, TasksLoader())
        swipeRefreshLayoutTasks.setOnRefreshListener({loaderManager.getLoader<TaskLoader>(TASK_LOADER_ID).forceLoad()})

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

    private val typeTask = Type<FragmentTaskListItemBinding>(R.layout.fragment_task_list_item)
            .onCreate { println("Created ${it.binding.item} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.item} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.item} at #${it.adapterPosition}") }
            .onClick {
                showTaskInfo((it.binding.item!!))
            }
            .onLongClick {
                selectTask(it.binding.item!!)
            }
            .onChildElementClick {holder->
                holder.binding.taskExpandBtn.setOnClickListener {
                    (task_list.adapter as SimpleAdapter).switchExpendItem(holder.adapterPosition)
                }
                holder.binding.taskPreviewBtn.setOnClickListener{
                    PreferenceHelper.defaultPrefs(context).edit().putInt("task_id", (holder.binding.item?.task?.Id!!)).apply()
                    mListener?.replaceFragment(PreviewFragment())
                }
            }

    private fun Context?.toast(text: String) = this?.let { Toast.makeText(it, text, Toast.LENGTH_SHORT).show() }
}