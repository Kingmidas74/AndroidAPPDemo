package com.demo.online.scenes.logs

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API

/**
 * Created by Midas on 03.03.2018.
 */
class LogLoader(context: Context?, private var api: API, private var sceneId:Int): AsyncTaskLoader<ArrayList<LogItem>>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): ArrayList<LogItem>?  {
        var logList = ArrayList<LogItem>()

        for ((index, value) in api.LoadLogs(sceneId).withIndex()) {
            logList.add(LogItem(value,index))
        }
        return logList;
    }
}