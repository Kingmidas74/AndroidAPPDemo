package com.demo.online.payments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import com.demo.online.ActivityActions
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.extensions.toast
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import kotlinx.android.synthetic.main.webview_payment_dialog.*


/**
 * Created by Midas on 18.03.2018.
 */
class WebviewPaymentDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val result = inflater?.inflate(R.layout.webview_payment_dialog, container, false)
        return (result)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        button_close.setOnClickListener({
            dismiss()
            //mListener?.replaceFragment(Task)
        })
        payment_web_view.webViewClient = object:WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if(url?.startsWith("https://online.demo.com")!!)
                {
                    mListener?.toast("WE ARE BACK!");
                }
                else {
                    mListener?.toast("IS PAYMENT PAGE");
                }
            }
        }
        var url = PreferenceHelper.defaultPrefs(context).getString("url",null)
        payment_web_view.getSettings().loadsImagesAutomatically = true
        payment_web_view.settings.javaScriptEnabled = true
        payment_web_view.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        payment_web_view.loadUrl(url)

    }

    private var mListener: MainActivity? =null

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }




    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActivityActions) {
            mListener = context as MainActivity
            mListener?.supportActionBar?.hide()
        } else {
            throw RuntimeException(context!!.toString() + " must implement ActivityActions") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener?.supportActionBar?.show()
    }

    override fun dismiss() {
        super.dismiss()
        mListener?.supportActionBar?.show()
    }
}