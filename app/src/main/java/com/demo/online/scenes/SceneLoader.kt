package com.demo.online.scenes

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API

/**
 * Created by Midas on 03.03.2018.
 */
class SceneLoader(context: Context?, private var api:API ): AsyncTaskLoader<ArrayList<SceneItem>>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): ArrayList<SceneItem>?  {
        var sceneList = ArrayList<SceneItem>()

        for ((index, value) in api.LoadScenes().withIndex()) {
            sceneList.add(SceneItem(value,index))
        }
        return sceneList;
    }
}