package com.kanzankazu.itungitungan.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.kanzankazu.itungitungan.R

class NotificationsFragment : Fragment() {

    private var mParam1: String? = ""
    private var mParam2: String? = ""

    companion object {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): NotificationsFragment {
            val fragment = NotificationsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }

        fun newInstance(): Fragment {
            return NotificationsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponent()
        initParam()
        initSession()
        initContent()
        initListener()
    }

    private fun initComponent() {

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