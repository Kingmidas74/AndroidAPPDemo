package com.demo.online.scenes.tasks

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API

/**
 * Created by Midas on 03.03.2018.
 */
class TaskLoader(context: Context?, private var api: API, private var sceneId:Int): AsyncTaskLoader<ArrayList<TaskItem>>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): ArrayList<TaskItem>?  {
        val taskList = ArrayList<TaskItem>()

        for ((index, value) in api.LoadTasks(sceneId).withIndex()) {
            taskList.add(TaskItem(value, index))
        }
        return taskList;
    }
}