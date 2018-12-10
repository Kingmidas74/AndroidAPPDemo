package com.demo.online.scenes.tasks.settings

import java.util.*

/**
 * Created by Midas on 18.03.2018.
 */
interface TaskSettings {
    fun GetSettings():Dictionary<String,Dictionary<String,String>>
}