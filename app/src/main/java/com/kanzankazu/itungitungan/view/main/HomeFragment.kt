package com.kanzankazu.itungitungan.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseFragment
import com.kanzankazu.itungitungan.view.main.Hutang.HutangListActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), HomeContract.View {

    private lateinit var homeAdapter: HomeAdapter
    private var mParam1: String? = ""
    private var mParam2: String? = ""

    companion object {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }

        fun newInstance(): Fragment {
            return HomeFragment()
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponent()
        initParam()
        initSession()
        initContent()
        initListener()
    }

    override fun itemAdapterClick(position: HomeModel) {
        when {
            position.title.equals(Constants.HOME.Anggaran, true) -> {
            }
            position.title.equals(Constants.HOME.Banding, true) -> {
            }
            position.title.equals(Constants.HOME.Keuangan, true) -> {
            }
            position.title.equals(Constants.HOME.Hutang, true) -> {
                Utils.intentTo(mActivity, HutangListActivity::class.java, false)
            }
            position.title.equals(Constants.HOME.Stok, true) -> {
            }
        }
    }

    private fun initComponent() {

    }

    private fun initParam() {

    }

    private fun initSession() {
    }

    private fun initContent() {

        homeAdapter = HomeAdapter(mActivity, this)
        rv_home.adapter = homeAdapter
        rv_home.isNestedScrollingEnabled = false
        rv_home.layoutManager = GridLayoutManager(mActivity, 3, GridLayoutManager.VERTICAL, false)

        setHomeList()
    }

    private fun initListener() {

    }

    private fun setHomeList() {

        val homeModels = arrayListOf<HomeModel>()
        homeModels.add(HomeModel(R.drawable.ic_anggaran, Constants.HOME.Anggaran, isShow = true, isComingSoon = true))
        homeModels.add(HomeModel(R.drawable.ic_arisan, Constants.HOME.Arisan, isShow = true, isComingSoon = true))
        homeModels.add(HomeModel(R.drawable.ic_best_price, Constants.HOME.Banding, isShow = true, isComingSoon = true))
        homeModels.add(HomeModel(R.drawable.ic_hutang, Constants.HOME.Hutang, isShow = true, isComingSoon = false))
        homeModels.add(HomeModel(R.drawable.ic_keuangan, Constants.HOME.Keuangan, isShow = true, isComingSoon = true))
        homeModels.add(HomeModel(R.drawable.ic_stok, Constants.HOME.Stok, isShow = true, isComingSoon = true))
        homeAdapter.setData(homeModels)

    }
}