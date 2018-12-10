package com.demo.online.user

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.User
import com.demo.online.*
import com.demo.online.constants.Constants.Companion.USER_SIGN_IN_LOADER
import com.demo.online.databinding.SignInFragmentBinding
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment:Fragment() {

    private var vm:SignVM?=null
    private var mListener: SignActivity? =null
    private var binding:SignInFragmentBinding?=null

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                SignInFragment().apply {
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

    private fun attemptLogin() {
        email.error = null
        password.error = null

        val emailStr = vm?.email?.get()!!
        val passwordStr = vm?.password?.get()!!

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            loaderManager.initLoader(USER_SIGN_IN_LOADER, null, UserLoginLoader(emailStr,passwordStr)).forceLoad()
        }
    }

    private fun showProgress(show: Boolean) {
        vm?.InProgress?.set(show)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate(inflater!! , R.layout.sign_in_fragment,container , false)
        val myView : View  = binding?.root!!
        vm= SignVM("")
        binding?.vm=vm
        binding?.executePendingBindings()
        return myView
    }


    inner class UserLoginLoader(private val email:String, private val Password:String): LoaderManager.LoaderCallbacks<User> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<User> {
            return UserSignInLoader(context,API(),email,Password)
        }


        override fun onLoadFinished(loader: Loader<User>?, data: User) {
            showProgress(false)
            loaderManager.destroyLoader(USER_SIGN_IN_LOADER)
            if(try {
                data.Id > 0
            } catch(e:Exception) {
                false
            })
            {

                PreferenceHelper.defaultPrefs(context).edit().putString("user", Gson().toJson(data).toString()).apply()
                startActivity(Intent(context, MainActivity::class.java))
            }
            else
            {
                password.error = getString(R.string.error_incorrect_password)
                password.requestFocus()
            }
        }
        override fun onLoaderReset(loader: Loader<User>?) {
            loader?.reset()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email_sign_in_button.setOnClickListener { attemptLogin() }
        sign_up_link.setOnClickListener{
            mListener?.replaceFragment(SignUpFragment.newInstance("SignUp"))
        }
        forgot_password.setOnClickListener{
            mListener?.replaceFragment(SignRestFragment.newInstance("Rest"))
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActivityActions) {
            mListener = context as SignActivity
        } else {
            throw RuntimeException(context!!.toString() + " must implement ActivityActions")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener=null
    }

}