package com.demo.online.payments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.demo.api.API
import com.demo.api.User
import com.demo.online.ActivityActions
import com.demo.online.MainActivity
import com.demo.online.R
import com.demo.online.helpers.sytemResourse.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_payments.*
import kotlinx.android.synthetic.main.fragment_payments.view.*

class PaymentListFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<PaymentItem>>, SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        loaderManager.getLoader<PaymentLoader>(4).forceLoad()
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                PaymentListFragment().apply {
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActivityActions) {
            mListener = context as MainActivity

        } else {
            throw RuntimeException(context!!.toString() + " must implement ActivityActions")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener=null
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<PaymentItem>> {
        val preference = PreferenceHelper.defaultPrefs(context)
        val user = Gson().fromJson(preference.getString("user",null), User::class.java)
        val api = API(user.Id, user.Hash)
        return PaymentLoader(context, api)
    }


    override fun onLoadFinished(loader: Loader<ArrayList<PaymentItem>>?, data: ArrayList<PaymentItem>) {
        (payments_list.adapter as PaymentListAdapter).addAll(data)
        (payments_list.parent as SwipeRefreshLayout).isRefreshing = false
    }

    override fun onLoaderReset(loader: Loader<ArrayList<PaymentItem>>?) {
        loader?.reset()
    }

    private lateinit var llm: LinearLayoutManager


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val sview = inflater!!.inflate(R.layout.fragment_payments, container, false) as SwipeRefreshLayout

        val context = sview.context
        val view = sview.payments_list
        view.layoutManager = LinearLayoutManager(context)
        llm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.layoutManager = llm
        view.adapter = PaymentListAdapter(ArrayList<PaymentItem>())
        loaderManager.initLoader(4, null, this)
        loaderManager.getLoader<PaymentLoader>(4).forceLoad()
        val itemAnimator: RecyclerView.ItemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 1000
        itemAnimator.removeDuration = 1000
        view.itemAnimator = itemAnimator
        sview.setOnRefreshListener({loaderManager.getLoader<PaymentLoader>(4).forceLoad()})

        return sview
    }



}