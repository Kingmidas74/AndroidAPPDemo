package com.demo.online

import android.content.Context
import android.support.design.widget.TabLayout

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.*
import com.demo.online.payments.PaymentListFragment
import com.demo.online.spents.SpentListFragment
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment() {

    private var mListener: MainActivity? = null

    companion object {
        @JvmStatic
        fun newInstance(title: String/*, param2: String*/) =
                HistoryFragment().apply {
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val result:View=inflater!!.inflate(R.layout.fragment_history, container, false)

        result.history_tabs.addOnTabSelectedListener(object:
                TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab:TabLayout.Tab) {
                childFragmentManager.beginTransaction().replace(result.history_content_frame.id,getFragment(tab.position)).commit()
            }

            override fun onTabUnselected(tab:TabLayout.Tab) {

            }
            override fun onTabReselected(tab:TabLayout.Tab) {

            }
        })
        childFragmentManager.beginTransaction().replace(result.history_content_frame.id,getFragment(0)).commit()
        return (result)
    }

    private fun getFragment(sectionNumber:Int): Fragment {

        when(sectionNumber)
        {
            0 -> {
                return SpentListFragment.newInstance("Spents")
            }
            1 -> {
                return PaymentListFragment.newInstance("Payments")
            }
        }
        return SpentListFragment.newInstance("Spents")
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)



        // Set up the ViewPager with the sections adapter.
        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
