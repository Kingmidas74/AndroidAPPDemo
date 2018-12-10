package com.demo.online.payments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.demo.online.R
import java.util.ArrayList

class PaymentListAdapter(_mValues: ArrayList<PaymentItem>) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {

    fun clear() {
        mValues.clear()
        notifyDataSetChanged()
    }

    fun addAll(list: ArrayList<PaymentItem>) {
        mValues = list
        notifyDataSetChanged()
    }

    fun add(list: ArrayList<PaymentItem>) {
        mValues.addAll(list)
        notifyDataSetChanged()
    }

    private var mValues: ArrayList<PaymentItem> = _mValues

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_payment_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mItem = mValues[position]
        holder.mTitleView.text = mValues[position].payment.Title
        holder.mTaskCost.text = mValues[position].payment.Cost.toString()
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val mTitleView = mView.findViewById<View>(R.id.payment_title) as TextView
        val mTaskCost = mView.findViewById<View>(R.id.payment_cost) as TextView
        lateinit var mItem: PaymentItem

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}