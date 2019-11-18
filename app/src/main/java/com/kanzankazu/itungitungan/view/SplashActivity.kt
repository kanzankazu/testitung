package com.kanzankazu.itungitungan.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startActivity(Intent(this, SignInUpActivity::class.java))
        finish()
    }
}
