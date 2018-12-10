package com.demo.online.scenes.logs

import com.demo.api.Scene
import com.demo.online.helpers.recyclerViewAdapters.EntityItem

/**
 * Created by Midas on 02.03.2018.
 */
data class LogItem (var log: String, var index:Int):EntityItem(index) {

}