package com.kanzankazu.itungitungan.view.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var fragmentUtil: FragmentUtil
    private lateinit var arrayOfMenuId: IntArray
    private lateinit var arrayOfFragments: Array<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initContent()
        initListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Lihat", "onActivityResult MainActivity1 $requestCode , $resultCode")
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Lihat", "onActivityResult MainActivity2 $requestCode , $resultCode")
    }

    private fun initContent() {
        fragmentUtil = FragmentUtil(this, R.id.fl_main)

        arrayOfMenuId = fragmentUtil.intArray(R.id.menuHome, R.id.menuDash, R.id.menuNotif, R.id.menuProfile)
        arrayOfFragments = fragmentUtil.intFragment(HomeFragment.newInstance(), DashboardFragment.newInstance(), NotificationsFragment.newInstance(), ProfileFragment.newInstance())
        fragmentUtil.setupBottomNavigationView(bnv_main, arrayOfMenuId, HomeFragment.newInstance(), true, *arrayOfFragments)
    }

    private fun initListener() {

    }


}
