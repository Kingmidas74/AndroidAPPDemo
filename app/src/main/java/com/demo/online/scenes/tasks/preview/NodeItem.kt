package com.demo.online.scenes.tasks.preview

import com.demo.api.Node
import com.demo.online.helpers.recyclerViewAdapters.EntityItem

/**
 * Created by Midas on 02.03.2018.
 */
data class NodeItem(var node: Node, val index:Int):EntityItem(index) {
}