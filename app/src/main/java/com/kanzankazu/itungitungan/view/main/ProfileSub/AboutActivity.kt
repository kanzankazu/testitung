package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Utils
import kotlinx.android.synthetic.main.app_toolbar.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        Utils.setupAppToolbarForActivity(this,toolbar,getString(R.string.about))
        setView()
        setListener()
    }

    private fun setView() {}

    private fun setListener() {}
}
