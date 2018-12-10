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
import com.demo.api.API
import com.demo.online.*
import com.demo.online.constants.Constants.Companion.USER_SIGN_UP_LOADER
import com.demo.online.databinding.SignUpFragmentBinding
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignUpFragment:Fragment() {

    private var vm:SignVM?=null
    private var mListener: SignActivity? =null
    private var binding:SignUpFragmentBinding?=null

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                SignUpFragment().apply {
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

    inner class UserRegistrateLoader(private val data:SignVM): LoaderManager.LoaderCallbacks<Boolean> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Boolean> {
            return UserSignUpLoader(context, API(),data.email.get()!!,data.password.get()!!, data.repassword.get()!!, data.name.get()!!, data.surname.get()!!)
        }


        override fun onLoadFinished(loader: Loader<Boolean>?, data: Boolean) {
            showProgress(false)
            loaderManager.destroyLoader(USER_SIGN_UP_LOADER)
            if(try {
                        data
                    } catch(e:Exception) {
                        false
                    })
            {
                Toast.makeText(context,"Email send", Toast.LENGTH_LONG)
            }
            else
            {
                password.error = getString(R.string.error_incorrect_password)
                password.requestFocus()
            }
        }
        override fun onLoaderReset(loader: Loader<Boolean>?) {
            loader?.reset()
        }
    }


    private fun attemptLogin() {

        email.error = null
        password.error = null
        repassword.error = null
        username.error = null
        usersurname.error = null

        val emailStr = vm?.email?.get()!!
        val passwordStr = vm?.password?.get()!!
        val repasswordStr = vm?.repassword?.get()!!
        val nameStr = vm?.name?.get()!!
        val surnameStr = vm?.surname?.get()!!

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }
        if (!TextUtils.equals(passwordStr,repasswordStr)) {
            repassword.error = getString(R.string.error_invalid_password)
            focusView = repassword
            cancel = true
        }
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        }

        if (TextUtils.isEmpty(nameStr)) {
            username.error = getString(R.string.error_field_required)
            focusView = username
            cancel = true
        }
        if (TextUtils.isEmpty(surnameStr)) {
            usersurname.error = getString(R.string.error_field_required)
            focusView = usersurname
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            loaderManager.initLoader(USER_SIGN_UP_LOADER, null, UserRegistrateLoader(vm!!)).forceLoad()
        }
    }

    private fun showProgress(show: Boolean) {
        vm?.InProgress?.set(show)
    }




    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate(inflater!! , R.layout.sign_up_fragment,container , false)
        val myView : View  = binding?.root!!
        vm= SignVM("");
        binding?.vm=vm
        binding?.executePendingBindings()
        return myView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_in_link.setOnClickListener{
            mListener?.replaceFragment(SignInFragment.newInstance("SignIn"))
        }
        email_sign_in_button.setOnClickListener { attemptLogin() }
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