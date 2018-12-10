package com.demo.online.scenes.tasks.settings

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import com.demo.api.RenderSettings


data class SharedSettings(private var renderSetting: RenderSettings): BaseObservable() {
    var IsDistribute=ObservableField<Boolean>(renderSetting.IsDistribute)


    @get:Bindable
    @set:Bindable
    var title = ObservableField<String>(renderSetting.Name)

    @Bindable
    fun getTitleWatcher():TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //notifyChange()
                title.set(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }

    fun switchDistribute() {IsDistribute.set(IsDistribute.get()?.not())}

    var RAMValues:Array<String> = renderSetting.RAMValues.map { it -> it.name }.toTypedArray()
    var RAMIndex =ObservableField<Int>(renderSetting.Cameras.indexOfFirst { it->it.selected==true })
    @Bindable
    fun getRAMWatcher(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                RAMIndex.set(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }

    var SoftIndex =ObservableField<Int>(0)
    @Bindable
    fun getSoftWatcher(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                SoftIndex.set(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }


    var cameras:Array<String> = renderSetting.Cameras.map { it -> it.name }.toTypedArray()
    var CameraIndex =ObservableField<Int>(renderSetting.Cameras.indexOfFirst { it->it.selected==true })
    @Bindable
    fun getCameraWatcher(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                CameraIndex.set(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }

    var SceneStates:Array<String> = renderSetting.States.map { it -> it.name }.toTypedArray()
    var SceneStateIndex =ObservableField<Int>((renderSetting.States.indexOfFirst { it->it.selected==true }))
    @Bindable
    fun getSceneStateWatcher(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                SceneStateIndex.set(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }

    var StateSets:Array<String> = renderSetting.Sets.map { it -> it.name }.toTypedArray()
    var StateSetIndex =ObservableField<Int>((renderSetting.Sets.indexOfFirst { it->it.selected==true }))
    @Bindable
    fun getStateSetWatcher(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                StateSetIndex.set(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }



    fun GetSettings():RenderSettings {
        renderSetting.Name=title.get()
        return renderSetting
    }


}