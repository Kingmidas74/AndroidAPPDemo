package com.demo.online.user

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.User

class UserRestPwdLoader(context: Context?, private var api: API, private var email:String): AsyncTaskLoader<Boolean>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): Boolean?  {
        try {
            return api.RestPassword(email) //blalalakov
        }
        catch (e:Exception)
        {
            return false
        }

    }
}