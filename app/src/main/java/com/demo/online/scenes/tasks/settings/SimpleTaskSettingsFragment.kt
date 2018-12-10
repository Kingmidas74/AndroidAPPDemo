package com.demo.online.scenes.tasks.settings

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.RenderSettings
import com.demo.api.Task
import com.demo.api.User
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.databinding.FragmentSimpleTaskSettingsBinding
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import com.demo.online.scenes.tasks.TaskListFragment

class SimpleTaskSettingsFragment : Fragment() {
    private var title: String? = null
    private var sceneId: Int? = null

    private var vm:SharedSettings?=null
    private var mListener: MainActivity? =null
    private var binding: FragmentSimpleTaskSettingsBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
            sceneId=it.getInt("sceneId")
            mListener?.title=title
           // param2 = it.getString(ARG_PARAM2)
        }
    }

    inner class TaskCreatorLoader(private var rs:RenderSettings): LoaderManager.LoaderCallbacks<Task> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Task> {
            val preference = PreferenceHelper.defaultPrefs(context)
            val user = Gson().fromJson(preference.getString("user",null), User::class.java)
            val sceneId = preference.getString("sceneId",null).toInt()
            val api = API(user.Id, user.Hash)
            return TaskCreator(context,api,sceneId,rs);
        }


        override fun onLoadFinished(loader: Loader<Task>?, data: Task) {
           // mListener?.replaceFragment(TaskListFragment.newInstance("create",sceneId!!))
            val WHAT = 1
            val handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    if (msg.what === WHAT) mListener?.replaceFragment(TaskListFragment.newInstance("create",sceneId!!))
                }
            }
            handler.sendEmptyMessage(WHAT)
        }

        //
        //getLoaderManager().destroyLoader(id);
        override fun onLoaderReset(loader: Loader<Task>?) {
            loader?.reset()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate(inflater!! ,R.layout.fragment_simple_task_settings,container , false)
        val myView : View  = binding?.root!!
        val preference = PreferenceHelper.defaultPrefs(context)
        val rs = Gson().fromJson(preference.getString("render_settings",null), RenderSettings::class.java)
        vm = SharedSettings(rs)
        binding?.vm=vm
        binding?.executePendingBindings()
        setHasOptionsMenu(true)
        return myView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, sceneId: Int) =
                SimpleTaskSettingsFragment().apply {
                    arguments = Bundle().apply {
                        putString("title", title)
                        putInt("sceneId",sceneId)
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shared_dialog_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if(id==R.id.start_task) {
            createTask()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createTask() {
        val preference = PreferenceHelper.defaultPrefs(context)
        val user = Gson().fromJson(preference.getString("user",null), User::class.java)
        val api = API(user.Id,user.Hash)
       loaderManager.initLoader(15, null, TaskCreatorLoader(vm?.GetSettings()!!)).forceLoad()

    }
}
