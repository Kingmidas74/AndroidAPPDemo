package com.demo.online.scenes.archives

import android.app.Application
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.Archive
import com.demo.api.User
import com.demo.online.helpers.recyclerViewAdapters.EntityItem
import com.demo.online.helpers.recyclerViewAdapters.StableId
import com.demo.online.helpers.sytemResourse.PreferenceHelper

/**
 * Created by Midas on 02.03.2018.
 */

data class ArchiveItem (var archive: Archive, var index:Int):EntityItem(index) {


}