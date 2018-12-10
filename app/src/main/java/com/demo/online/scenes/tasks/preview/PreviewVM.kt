package com.demo.online.scenes.tasks.preview

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import android.view.View
import android.widget.AdapterView


data class PreviewVM(private var data:String): BaseObservable() {

    var frames = ObservableField(mutableListOf<String>(""))

    private var listNodes:ArrayList<NodeItem>?=null
    private var selectedNode:String?=null

    fun updateNodes(nodeItems:ArrayList<NodeItem>)
    {
        val temp = mutableListOf<String>()
        for((node,index) in nodeItems)
        {
            temp.add(node.FrameName)
        }
        frames.set(temp)
        selectedNode=nodeItems[0].node.NodeName
        img.set(nodeItems[0].node.NodeName)
        infoStatus.set(nodeItems[0].node.InfoStatus)
        FrameIndex.set(0)
        listNodes=nodeItems
    }



    var img= ObservableField<String>("")

    var infoStatus= ObservableField<String>("")

    var FrameIndex =ObservableField<Int>(0)

    @Bindable
    fun getFrameWatcher(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                FrameIndex.set(position)
                if(listNodes!=null && listNodes?.size!! >0) {
                    img.set(listNodes?.get(position)?.node?.NodeName)
                    infoStatus.set(listNodes?.get(position)?.node?.InfoStatus)
                    selectedNode = listNodes?.get(position)?.node?.NodeName
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }
}