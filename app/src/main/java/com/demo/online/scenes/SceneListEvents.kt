package com.demo.online.scenes

import com.demo.online.scenes.SceneItem

/**
 * Created by Midas on 03.03.2018.
 */
interface SceneListEvents {
    fun showTasks(scene: SceneItem)
    fun selectScene(scene:SceneItem)
}