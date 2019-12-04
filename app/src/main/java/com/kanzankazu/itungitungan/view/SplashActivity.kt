package com.kanzankazu.itungitungan.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity

class SplashActivity : AppCompatActivity() {

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initContent()

        Log.d("Lihat", "onCreate SplashActivity " + FirebaseInstanceId.getInstance().token)
        UserPreference.getInstance().fcmToken = FirebaseInstanceId.getInstance().token
    }

    private fun initContent() {
        startActivity(Intent(this, SignInUpActivity::class.java))
        finish()
    }
}
