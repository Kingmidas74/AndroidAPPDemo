package com.demo.online.payments

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.demo.api.API
import com.demo.api.Payment

/**
 * Created by Midas on 03.03.2018.
 */
class MoneyToMinutesConverter(context: Context?, private var api: API, private var money:Double): AsyncTaskLoader<Double>(context) {

    override fun onStartLoading() {
        forceLoad() // Starts the loadInBackground method
    }

    override fun loadInBackground(): Double?  {
        return api.ConvertMoneyToMinutes(money)
    }
}