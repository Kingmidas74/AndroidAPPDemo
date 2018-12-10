package com.demo.online.scenes.tasks.settings

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.*
import com.demo.online.ActivityActions
import com.demo.online.MainActivity
import com.demo.online.R
import kotlinx.android.synthetic.main.task_vray_settings_tabs.*
import kotlinx.android.synthetic.main.task_vray_settings_tabs.view.*
import java.util.*


class TaskVRAYSettingsFragment : Fragment(), TaskSettings {
    override fun GetSettings(): Dictionary<String, Dictionary<String, String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var tabs:TabLayout
    private var mListener: MainActivity? =null

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val result: View =inflater!!.inflate(R.layout.task_vray_settings_tabs, container, false)
        setHasOptionsMenu(true)
        tabs=result.render_tabs
        return (result)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setTab(sectionNumber:Int) {
        for (index in 0 until vray_settings_content_frame.childCount) {
            vray_settings_content_frame.getChildAt(index).visibility=View.GONE
        }
        vray_settings_content_frame.getChildAt(sectionNumber).visibility=View.VISIBLE
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabs.addOnTabSelectedListener(object:
                TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                setTab(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        setTab(0)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActivityActions) {
            mListener = context as MainActivity
        } else {
            throw RuntimeException(context!!.toString() + " must implement ActivityActions") as Throwable
        }





        // Set up the ViewPager with the sections adapter.
        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if(id==R.id.create_task) {
            mListener?.replaceFragment(SharedSettingsDialog())
            //SharedSettingsDialog().show(activity.supportFragmentManager,"ssd")
        }

        return super.onOptionsItemSelected(item)
    }
}