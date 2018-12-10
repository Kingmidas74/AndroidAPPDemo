package com.demo.online.helpers.recyclerViewAdapters

abstract class EntityItem(private val index:Int):StableId {
    override val stableId: Long
        get() = index.toLong()
    var IsSelected: Boolean = false
    var IsExpended: Boolean = true

    var IsRemoved:Boolean = false
}