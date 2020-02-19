package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.kanzankazu.itungitungan.BuildConfig
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.main.ProfileAccountModel
import com.kanzankazu.itungitungan.view.main.ProfileAccountOptionAdapter
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.app_toolbar.*

class AboutActivity : AppCompatActivity() {

    private lateinit var aboutAdapter: ProfileAccountOptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        Utils.setupAppToolbarForActivity(this, toolbar, getString(R.string.about))
        setView()
        setListener()
    }

    private fun setView() {
        tv_about_app_name.text = BuildConfig.APP_NAME
        tv_about_app_version.text = BuildConfig.VERSION_NAME

        setAboutAdapter(rv_about)
        setAboutList()
    }

    private fun setListener() {
    }

    private fun setAboutAdapter(recyclerView: RecyclerView) {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        aboutAdapter = ProfileAccountOptionAdapter(this, object : ProfileAccountOptionAdapter.ProfileAccountAdapterListener {
            override fun onItemAdapterClick(position: Int, data: ProfileAccountModel) {

            }
        })
        recyclerView.adapter = aboutAdapter
    }

    private fun setAboutList() {
        val list = mutableListOf<ProfileAccountModel>()
        list.add(ProfileAccountModel(0, "Check Pembaharuan Aplikasi", "", "", true))
        list.add(ProfileAccountModel(0, "Isi Aplikasi", "Aplikasi perhitungan untuk kelancaran kehidupan berkeluarga, bertetangga, dan berteman", "", true))
        aboutAdapter.setData(list)
    }
}
