package com.demo.online.scenes.tasks.settings

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.RenderSettings
import com.demo.api.Task

class TaskCreator(context: Context?, private var api: API, private var sceneId:Int, private var renderSettings: RenderSettings): AsyncTaskLoader<Task>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): Task?  {
        return api.CreateTask(sceneId,renderSettings)
    }
}