package com.demo.online.scenes.tasks.settings

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.demo.api.RenderSettings
import com.demo.online.ActivityActions
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.databinding.SharedSettingDialogBinding
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import kotlinx.android.synthetic.main.shared_setting_dialog.*


/**
 * Created by Midas on 18.03.2018.
 */
class SharedSettingsDialog : Fragment() {

    private var vm:SharedSettings?=null
    private var mListener: MainActivity? =null
    private var binding:SharedSettingDialogBinding?=null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate(inflater!! ,R.layout.shared_setting_dialog,container , false)
        val myView : View  = binding?.root!!
        val preference = PreferenceHelper.defaultPrefs(context)
        val rs = Gson().fromJson(preference.getString("render_settings",null), RenderSettings::class.java)
        vm = SharedSettings(rs)
        binding?.vm=vm
        binding?.executePendingBindings()
        setHasOptionsMenu(true)
        return myView
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
        Toast.makeText(activity, vm?.GetSettings()?.Name, Toast.LENGTH_SHORT).show()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActivityActions) {
            mListener = context as MainActivity
          //  mListener?.actionBar?.hide()
        } else {
            throw RuntimeException(context!!.toString() + " must implement ActivityActions")
        }
    }

    override fun onDetach() {
        super.onDetach()
       // mListener?.actionBar?.show()
        mListener=null
    }


}