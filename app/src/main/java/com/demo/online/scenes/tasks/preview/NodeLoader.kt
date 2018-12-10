package com.demo.online.scenes.tasks.preview

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API

/**
 * Created by Midas on 03.03.2018.
 */
class NodeLoader(context: Context?, private var api: API, private var taskId:Int, private var nodeName:String): AsyncTaskLoader<ArrayList<NodeItem>>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): ArrayList<NodeItem>?  {
        val nodeList = ArrayList<NodeItem>()

        for ((index, value) in api.LoadPreviewNodes(taskId,nodeName).withIndex()) {
            nodeList.add(NodeItem(value, index))
        }
        return nodeList;
    }
}