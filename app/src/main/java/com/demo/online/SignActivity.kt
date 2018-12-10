package com.demo.online

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.widget.Toast
import com.demo.online.user.SignInFragment
import com.demo.online.user.SignRestFragment
import com.demo.online.user.SignUpFragment
import kotlinx.android.synthetic.main.activity_sign.*

/**
 * A login screen that offers login via email/password.
 */
class SignActivity : AppCompatActivity(),ActivityActions {

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

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(content_frame.id,fragment).addToBackStack(fragment.javaClass.name).commit()
    }
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.READ_CONTACTS)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            101)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_CONTACTS)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_CONTACTS),
                            102)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }

        }

        setContentView(R.layout.activity_sign)
        setFragment(R.layout.sign_in_fragment)
    }

    private fun getFragment(sectionNumber:Int): Fragment {
        when(sectionNumber)
        {
            R.layout.sign_in_fragment -> {
                return SignInFragment.newInstance("Sign in")
            }
            R.layout.sign_up_fragment -> {
                return SignUpFragment.newInstance("Sign up")
            }
            R.layout.sign_rest_fragment -> {
                return SignRestFragment.newInstance("Rest password")
            }
        }
        return SignInFragment.newInstance("Sign in")
    }


    private fun setFragment(id:Int) {
        replaceFragment(getFragment(id))
    }




}
