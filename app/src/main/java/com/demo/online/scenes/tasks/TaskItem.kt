package com.demo.online.scenes.tasks

import com.demo.api.Task
import com.demo.online.helpers.recyclerViewAdapters.EntityItem

/**
 * Created by Midas on 02.03.2018.
 */
data class TaskItem(var task: Task, val index:Int):EntityItem(index) {
}