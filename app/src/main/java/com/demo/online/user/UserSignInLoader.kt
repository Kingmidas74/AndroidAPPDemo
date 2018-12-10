package com.demo.online.user

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.User

class UserSignInLoader(context: Context?, private var api: API, private var email:String, private var password:String): AsyncTaskLoader<User>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): User?  {
        try {
            return api.TryLoginUser(email,password);
        }
        catch (e:Exception)
        {
            val u = User()
            u.Id=0
            return u
        }

    }
}