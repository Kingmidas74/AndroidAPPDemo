package com.demo.online.scenes.tasks

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.RenderSettings

/**
 * Created by Midas on 03.03.2018.
 */
class SettingsLoader(context: Context?, private var api: API, private var sceneId:Int=0,private var taskId:Int=0): AsyncTaskLoader<RenderSettings>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): RenderSettings?  {
        if(taskId!=0) return api.GetTaskSettings(taskId)
        if(sceneId!=0) return api.GetSceneSettings(sceneId)
        throw Exception("Nothing params deined")
    }
}