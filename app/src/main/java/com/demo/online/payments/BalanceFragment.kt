package com.demo.online.payments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.online.ActivityActions
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.extensions.toast
import kotlinx.android.synthetic.main.fragment_balance.*
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.User
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.AdapterView
import android.widget.TextView
import com.demo.api.Rate
import com.demo.online.extensions.roundToDecimalPlaces


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BalanceFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class BalanceFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                BalanceFragment().apply {
                    arguments = Bundle().apply {
                        putString("title", title)
                        // putString(ARG_PARAM2, param2)
                    }
                }
    }
    override fun onResume() {
        super.onResume()
        // Set title
        mListener?.title = arguments?.getString("title")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mListener?.title=it.getString("title")
            // param2 = it.getString(ARG_PARAM2)
        }
    }

    private var mListener: MainActivity? = null

    inner class MoneyWatcher(private var api:API):TextWatcher {
        override fun afterTextChanged(money: Editable?) {
            try {
                if(money_value.tag==null && money!=null && !money.toString().isNullOrEmpty() && !money.toString().equals("0")) {
                    Moneyhandler.removeCallbacks(workRunnable)
                    workRunnable = Runnable{
                        Thread(Runnable {
                            var msg = Moneyhandler.obtainMessage()
                            var bundle = Bundle()
                            var m = money.toString();
                            var mD = 0.0;
                            if(m.isNullOrEmpty() || m.equals("0") || m.equals("") || m=="" || m=="0") {
                                mD=0.0
                            }
                            else {
                                mD=m.toDouble()
                            }
                            var currency = currency.selectedItem.toString()
                            if(currency.equals("USD")) mD*=rate?.USDRUB!!
                            if(currency.equals("EUR")) mD*=rate?.EURRUB!!
                            bundle.putDouble("val",api.ConvertMoneyToMinutes(mD))
                            msg.data=bundle
                            Moneyhandler.sendMessage(msg)
                        }).start()
                    }
                    Moneyhandler.postDelayed(workRunnable,500)
                }
            }
            catch (e:Exception)
            {
                money_value.text=SpannableStringBuilder("0")
                minutes_value.text=SpannableStringBuilder("0")
            }

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }

    inner class MinutesWatcher(private var api:API):TextWatcher {
        override fun afterTextChanged(minutes: Editable?) {
            try {
                if(minutes_value.tag==null && minutes!=null && !minutes.isNullOrEmpty() && !minutes.equals("0")) {
                    Minuteshandler.removeCallbacks(workRunnable)
                    workRunnable = Runnable{
                        Thread(Runnable {
                            var msg = Minuteshandler.obtainMessage()
                            var bundle = Bundle()
                            var m = minutes.toString();
                            var mD = 0.0;
                            if(m.isNullOrEmpty() || m.equals("0") || m.equals("") || m=="" || m=="0") {
                                mD=0.0
                            }
                            else {
                                mD=m.toDouble()
                            }
                            mD=api.ConvertMinutesToMoney(mD)
                            var currency = currency.selectedItem.toString()
                            if(currency.equals("USD")) mD/=rate?.USDRUB!!
                            if(currency.equals("EUR")) mD/=rate?.EURRUB!!
                            bundle.putDouble("val",mD)
                            msg.data=bundle
                            Minuteshandler.sendMessage(msg)
                        }).start()
                    }
                    Minuteshandler.postDelayed(workRunnable,500)
                }
            }
            catch (e:Exception)
            {
                minutes_value.text=SpannableStringBuilder("0")
                money_value.text=SpannableStringBuilder("0")
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }

    inner class CurrencySelector:AdapterView.OnItemSelectedListener {

        var previous: Int? = null
        var current: Int? = null

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            var currentMoney:Double = money_value.text.toString().toDouble()
            money_value.tag = "P"
            if(currentMoney.toInt()==0) {
                money_value.tag = null
                return
            }
            current = position
            if(previous!=null && current!=previous)
            {
                var current_currency = parent?.getItemAtPosition(current!!).toString()
                var previous_currency = parent?.getItemAtPosition(previous!!).toString()
                if(previous_currency.equals("USD"))
                {
                    if(current_currency.equals("RUB"))
                    {
                        money_value.text = SpannableStringBuilder((currentMoney*rate?.USDRUB!!).roundToDecimalPlaces(2).toString())
                    }
                    if(current_currency.equals("EUR"))
                    {
                        var inRubles:Double = currentMoney*rate?.USDRUB!!;
                        money_value.text = SpannableStringBuilder((inRubles/rate?.EURRUB!!).roundToDecimalPlaces(2).toString())
                    }
                }
                if(previous_currency.equals("EUR"))
                {
                    if(current_currency.equals("RUB"))
                    {
                        money_value.text = SpannableStringBuilder((currentMoney*rate?.EURRUB!!).roundToDecimalPlaces(2).toString())
                    }
                    if(current_currency.equals("USD"))
                    {
                        var inRubles:Double = currentMoney*rate?.EURRUB!!;
                        money_value.text = SpannableStringBuilder((inRubles/rate?.USDRUB!!).roundToDecimalPlaces(2).toString())
                    }
                }
                if(previous_currency.equals("RUB"))
                {
                    if(current_currency.equals("USD"))
                    {
                        money_value.text = SpannableStringBuilder((currentMoney/rate?.USDRUB!!).roundToDecimalPlaces(2).toString())
                    }
                    if(current_currency.equals("EUR"))
                    {
                        money_value.text = SpannableStringBuilder((currentMoney/rate?.EURRUB!!).roundToDecimalPlaces(2).toString())
                    }
                }
            }
            else if(previous==null) {
                var current_currency = parent?.getItemAtPosition(current!!).toString()
                if(current_currency.equals("USD"))
                {
                    money_value.text = SpannableStringBuilder((currentMoney/rate?.USDRUB!!).roundToDecimalPlaces(2).toString())
                }
                if(current_currency.equals("EUR"))
                {
                    money_value.text = SpannableStringBuilder((currentMoney/rate?.EURRUB!!).roundToDecimalPlaces(2).toString())
                }
            }
            previous=current
            money_value.tag = null
        }

    }


   /* var handler = Handler(Looper.getMainLooper()) {
        minutes_value.text=SpannableStringBuilder(it.toString())
        true
    }*/
   @SuppressLint("HandlerLeak")
   private val Moneyhandler = object : Handler() {
       override fun handleMessage(msg: Message) {
           val bundle = msg.data
           val string = bundle.getDouble("val").roundToDecimalPlaces(2)
           minutes_value.tag = "P"
           minutes_value.text = SpannableStringBuilder(string.toString())
           minutes_value.tag = null

       }
   }

    @SuppressLint("HandlerLeak")
    private val Minuteshandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val string = bundle.getDouble("val").roundToDecimalPlaces(2)
            money_value.tag = "P"
            money_value.text = SpannableStringBuilder(string.toString())
            money_value.tag = null

        }
    }

    @SuppressLint("HandlerLeak")
    private val Ratehandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val string = bundle.getString("val")
            rate = Gson().fromJson(string,Rate::class.java)

        }
    }

    @SuppressLint("HandlerLeak")
    private val Paymenthandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val string = bundle.getString("val")
            PreferenceHelper.defaultPrefs(context).edit().putString("url", string).apply()
            mListener?.replaceFragment(WebviewPaymentDialog())

        }
    }

    var workRunnable:Runnable? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_balance, container, false)
    }

    var rate: Rate?=null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = PreferenceHelper.defaultPrefs(context)
        val user = Gson().fromJson(preference.getString("user",null), User::class.java)
        val api = API(user.Id, user.Hash)

        fill_up_money.setOnClickListener({
            mListener?.runOnUiThread({
                Thread({
                    var msg = Paymenthandler.obtainMessage()
                    var bundle = Bundle()
                    var current_rate = 1.0;
                    if(currency.selectedItem.toString().equals("USD")) current_rate = rate?.USDRUB!!
                    if(currency.selectedItem.toString().equals("EUR")) current_rate = rate?.EURRUB!!
                    bundle.putString("val",api.SendPayment(currency.selectedItem.toString(),money_value.text.toString().toDouble(),current_rate,payment_system.selectedItem.toString()))
                    msg.data=bundle
                    Paymenthandler.sendMessage(msg)
                }).start()
            })
        })

        mListener?.runOnUiThread({
            Thread({
                var msg = Ratehandler.obtainMessage()
                var bundle = Bundle()
                bundle.putString("val",Gson().toJson(api.GetRate()))
                msg.data=bundle
                Ratehandler.sendMessage(msg)
            }).start()
        })
        money_value.addTextChangedListener(MoneyWatcher(api));
        minutes_value.addTextChangedListener(MinutesWatcher(api));
        currency.onItemSelectedListener = CurrencySelector()
        currency.setSelection(0)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActivityActions) {
            mListener = context as MainActivity
        } else {
            throw RuntimeException(context!!.toString() + " must implement ActivityActions") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}
