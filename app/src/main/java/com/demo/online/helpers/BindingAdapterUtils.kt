package com.demo.online.helpers

import android.databinding.BindingAdapter
import android.databinding.adapters.CheckedTextViewBindingAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatSpinner
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import com.demo.api.RenderTypeList
import com.demo.api.StatusList
import com.demo.online.R

@BindingAdapter("bind:renderType")
fun renderTypeToImageConverter(view: ImageView, renderType: RenderTypeList) {
    when (renderType) {
        RenderTypeList.V_RAY -> {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_render_vray))
        }
        RenderTypeList.CORONA -> {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_render_corona))
        }
        RenderTypeList.SCANLINE -> {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_3dmax))
        }
        RenderTypeList.MENTAL -> {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_3dmax))
        }
        RenderTypeList.RENDER -> {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_3dmax))
        }
    }
}


@BindingAdapter("bind:expendState")
fun expendStateToImageConverter(view: ImageView, isExpend: Boolean) {
    when (isExpend) {
        true -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_expand_down))
        false -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_expand_up))
    }
}

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}


@BindingAdapter("selectedValueChangedListener")
fun bindSelectWatcher(selectBox: AppCompatSpinner, selectWatcher: AdapterView.OnItemSelectedListener) {
    selectBox.setOnItemSelectedListener(selectWatcher)
}

@BindingAdapter("bind:actionType")
fun statusTypeToActionButtonImageConverter(view: ImageView, statusType: StatusList) {
    when (statusType) {
        StatusList.CREATING -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.CREATED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.STARTED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.SAVED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.NOT_STARTED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_start))
        StatusList.RUNNING -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.FINISHED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.HOLDED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.ABORTED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.ERROR -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.QUEUED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.HOLDED_BY_BILLING -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.HOLDED_BY_USER -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_start))
        StatusList.HOLDED_BY_ADMIN -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.TIMEOUT_REACHED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.SUSPENDED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.COMPLETE -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.TIMEOUT_HAS_BEEN_REACHED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.RESERVED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        else -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
    }
}

@BindingAdapter("bind:statusType")
fun statusTypeToImageConverter(view: ImageView, statusType: StatusList) {
    when (statusType) {
        StatusList.CREATING -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.CREATED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.STARTED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.SAVED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.NOT_STARTED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_start))
        StatusList.RUNNING -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.FINISHED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.HOLDED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.ABORTED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.ERROR -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.QUEUED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_stop))
        StatusList.HOLDED_BY_BILLING -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.HOLDED_BY_USER -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_start))
        StatusList.HOLDED_BY_ADMIN -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
        StatusList.TIMEOUT_REACHED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.SUSPENDED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.COMPLETE -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.TIMEOUT_HAS_BEEN_REACHED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        StatusList.RESERVED -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_settings_white))
        else -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_help))
    }
}