package com.demo.online.user

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView

data class SignVM(val data:String): BaseObservable() {

    @get:Bindable
    @set:Bindable
    var email = ObservableField<String>("")

    @Bindable
    fun getEmailWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //notifyChange()
                email.set(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }

    @get:Bindable
    @set:Bindable
    var password = ObservableField<String>("")

    @Bindable
    fun getPasswordWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //notifyChange()
                password.set(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }

    @get:Bindable
    @set:Bindable
    var repassword = ObservableField<String>("")

    @Bindable
    fun getRePasswordWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //notifyChange()
                repassword.set(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }

    @get:Bindable
    @set:Bindable
    var name = ObservableField<String>("")

    @Bindable
    fun getNameWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //notifyChange()
                name.set(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }

    @get:Bindable
    @set:Bindable
    var surname = ObservableField<String>("")

    @Bindable
    fun getSurnameWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //notifyChange()
                surname.set(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }

    var languageIndex =ObservableField<Int>(1)
    @Bindable
    fun getLanguageWatcher(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                languageIndex.set(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }

    var InProgress=ObservableField<Boolean>(false)


}