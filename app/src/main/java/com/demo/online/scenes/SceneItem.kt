package com.demo.online.scenes

import com.demo.api.Scene
import com.demo.online.helpers.recyclerViewAdapters.EntityItem

/**
 * Created by Midas on 02.03.2018.
 */
data class SceneItem (var scene: Scene, var index:Int):EntityItem(index) {

}