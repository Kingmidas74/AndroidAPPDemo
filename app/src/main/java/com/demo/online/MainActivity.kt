package com.demo.online

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.gson.Gson
import com.demo.api.User
import com.demo.online.scenes.SceneListFragment
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import com.demo.online.scenes.tasks.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import android.widget.Toast
import com.demo.online.payments.BalanceFragment
import java.nio.file.Files.size
import android.content.Intent
import android.support.v7.app.AppCompatDelegate


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ActivityActions {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        val preference = PreferenceHelper.defaultPrefs(applicationContext)
        val user = Gson().fromJson(preference.getString("user",null),User::class.java)
        nav_view.getHeaderView(0).UserFIO.text=("${user.Name} ${user.Surname}")
        nav_view.getHeaderView(0).UserEmail.text=user.Email
        setFragment(R.id.nav_scenes)
        nav_view.setCheckedItem(0)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getFragment(sectionNumber:Int): Fragment {
        when(sectionNumber)
        {
            R.id.nav_scenes -> {
                return SceneListFragment.newInstance("Scenes")
            }
            R.id.nav_balance -> {
                setTitle(R.string.Balance)
                return BalanceFragment.newInstance("Balance")
            }
            R.id.nav_history -> {
                setTitle(R.string.History)
                return HistoryFragment.newInstance("History")
            }
            R.id.nav_statistics -> {
                setTitle(R.string.Statistics)
                return SceneListFragment.newInstance("Scenes")
            }
            /*R.id.task_list -> {
                return TaskListFragment.newInstance("Tasks")
            }*/
        }

        return SceneListFragment.newInstance("Scenes")
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(content_frame.id,fragment).addToBackStack(fragment.javaClass.name).commit()
    }

    private fun setFragment(id:Int) {
        replaceFragment(getFragment(id))
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        setFragment(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
