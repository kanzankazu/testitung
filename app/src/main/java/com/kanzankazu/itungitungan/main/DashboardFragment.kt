package com.kanzankazu.itungitungan.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kanzankazu.itungitungan.R

class DashboardFragment : Fragment() {

    private var mParam1: String? = ""
    private var mParam2: String? = ""

    companion object {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): DashboardFragment {
            val fragment = DashboardFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }

        fun newInstance(): Fragment {
            return DashboardFragment()
        }
    }

    fun DashboardFragment() {
        // Required empty public constructor
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        initComponent(view)
        initParam()
        initSession()
        initContent()
        initListener()

        return view
    }

    private fun initComponent(view: View) {

    }

    private fun initParam() {

    }

    private fun initSession() {

    }

    private fun initContent() {

    }

    private fun initListener() {

    }
}