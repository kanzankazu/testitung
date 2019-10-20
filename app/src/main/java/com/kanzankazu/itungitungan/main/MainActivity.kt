package com.kanzankazu.itungitungan.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.utils.FragmentUtilKt
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fuk: FragmentUtilKt
    private lateinit var bnvMain: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fuk = FragmentUtilKt(this, R.id.fragmentContainer)
        fuk.setupBottomNavigationViewView(
                findViewById(R.id.bnv_main),
                intArrayOf(R.id.navigation_home,R.id.navigation_dashboard,R.id.navigation_notifications),
                HomeFragment.newInstance(),
                HomeFragment.newInstance(),
                DashboardFragment.newInstance(),
                NotificationsFragment.newInstance())

    }
}
