package com.demo.online.scenes.archives

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.Archive
import com.demo.api.Scene

/**
 * Created by Midas on 03.03.2018.
 */
class SceneCreator(context: Context?, private var api: API, private var archive:Archive): AsyncTaskLoader<Scene>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): Scene?  {
        return api.CreateScene(archive)
    }
}