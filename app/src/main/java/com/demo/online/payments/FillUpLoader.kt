package com.demo.online.payments

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.Payment

/**
 * Created by Midas on 03.03.2018.
 */
class FillUpLoader(context: Context?, private var api: API): AsyncTaskLoader<ArrayList<PaymentItem>>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): ArrayList<PaymentItem>?  {
        var taskList = ArrayList<PaymentItem>()

        for(payment: Payment in api.LoadPayments()) {
            taskList.add(PaymentItem(payment))
        }
        return taskList;
    }
}