package com.demo.online.spents

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.online.scenes.tasks.TaskItem

/**
 * Created by Midas on 03.03.2018.
 */
class SpentLoader(context: Context?, private var api: API): AsyncTaskLoader<ArrayList<TaskItem>>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): ArrayList<TaskItem>?  {
        val taskList = ArrayList<TaskItem>()

        for ((index, value) in api.LoadSpents().withIndex()) {
            taskList.add(TaskItem(value, index))
        }
        return taskList
    }
}