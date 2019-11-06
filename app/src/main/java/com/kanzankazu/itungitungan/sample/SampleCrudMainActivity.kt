package com.kanzankazu.itungitungan.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kanzankazu.itungitungan.R
import kotlinx.android.synthetic.main.activity_sample_crud_main.*

class SampleCrudMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crud_main)

        bt_createdata.setOnClickListener(View.OnClickListener { startActivity(Intent(this, SampleCrudCreateActivity::class.java)) })
        bt_viewdata.setOnClickListener(View.OnClickListener { startActivity(Intent(this, SampleCrudReadActivity::class.java)) })
    }
}
