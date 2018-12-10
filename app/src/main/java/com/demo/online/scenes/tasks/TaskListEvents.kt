package com.demo.online.scenes.tasks

/**
 * Created by Midas on 03.03.2018.
 */
interface TaskListEvents {
    fun showTaskInfo(task: TaskItem)
    fun selectTask(task: TaskItem)
}