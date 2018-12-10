package com.demo.online.scenes.archives

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.Archive
import com.demo.api.User
import com.demo.online.helpers.sytemResourse.PreferenceHelper

/**
 * Created by Midas on 03.03.2018.
 */
class ArchiveLoader(context: Context?, private var api:API ): AsyncTaskLoader<ArrayList<ArchiveItem>>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): ArrayList<ArchiveItem>?  {
        var archiveList = ArrayList<ArchiveItem>()

        for ((index, value) in api.LoadArchives().withIndex()) {
            val ai = ArchiveItem(value,index);
            archiveList.add(ArchiveItem(value,index))
        }
        return archiveList;
    }
}