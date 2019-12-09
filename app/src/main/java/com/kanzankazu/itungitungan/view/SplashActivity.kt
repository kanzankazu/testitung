package com.kanzankazu.itungitungan.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity
import com.kanzankazu.itungitungan.view.sample.SampleCrudMainActivity

class SplashActivity :
    BaseActivity(),
    FirebaseLoginUtil.FirebaseLoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initContent()

        Log.d("Lihat", "onCreate SplashActivity " + FirebaseInstanceId.getInstance().token)
        UserPreference.getInstance().fcmToken = FirebaseInstanceId.getInstance().token
    }

    private fun initContent() {
        if (loginUtil.isSignIn) {
            startActivity(Intent(this, SignInUpActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, SampleCrudMainActivity::class.java))
            finish()
        }
    }
}
