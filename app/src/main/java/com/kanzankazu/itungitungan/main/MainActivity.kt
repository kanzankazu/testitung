package com.kanzankazu.itungitungan.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kanzankazu.itungitungan.R

class MainActivity : AppCompatActivity() {

    private lateinit var bnvMain: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnvMain = findViewById(R.id.bnv_main)

    }
}
