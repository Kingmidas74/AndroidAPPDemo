package com.demo.online.extensions

import android.content.Context
import android.widget.Toast
import java.math.BigDecimal

/**
 * Created by Midas on 07.03.2018.
 */
fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Double.roundToDecimalPlaces(length:Int) =
        BigDecimal(this).setScale(length, BigDecimal.ROUND_HALF_UP).toDouble()