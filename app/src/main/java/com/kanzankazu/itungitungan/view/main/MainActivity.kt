package com.kanzankazu.itungitungan.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.FragmentUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentUtil: FragmentUtil
    private lateinit var arrayOfMenuId: IntArray
    private lateinit var arrayOfFragments: Array<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initContent()
        initListener()
    }

    private fun initContent() {
        arrayOfMenuId = fragmentUtil.intArray(R.id.menuHome, R.id.menuDash, R.id.menuNotif, R.id.menuProfile)
        arrayOfFragments = fragmentUtil.intFragment(HomeFragment.newInstance(), DashboardFragment.newInstance(), NotificationsFragment.newInstance(), ProfileFragment.newInstance())

        fragmentUtil = FragmentUtil(this, R.id.fl_main)
        fragmentUtil.setupBottomNavigationView(bnv_main, arrayOfMenuId, HomeFragment.newInstance(), true, *arrayOfFragments)
    }

    private fun initListener() {

    }


}
