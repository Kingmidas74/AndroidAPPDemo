package com.demo.online.user

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.User

class UserSignUpLoader(context: Context?, private var api: API, private var email:String, private var password:String, private var repassword:String, private var name:String, private var surname:String): AsyncTaskLoader<Boolean>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): Boolean?  {
        try {
            return api.CreateUser(email,password,repassword,name,surname) //blalalakov
        }
        catch (e:Exception)
        {
            return false
        }

    }
}