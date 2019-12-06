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

    private lateinit var loginUtil: FirebaseLoginUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initContent()

        Log.d("Lihat", "onCreate SplashActivity " + FirebaseInstanceId.getInstance().token)
        UserPreference.getInstance().fcmToken = FirebaseInstanceId.getInstance().token
    }

    override fun uiSignInSuccess(firebaseUser: FirebaseUser) {

    }

    override fun uiSignInFailed(errorMessage: String?) {
    }

    override fun uiSignOutSuccess() {
    }

    override fun uiSignOutFailed(message: String?) {
    }

    override fun uiConnectionError(messageError: String?, typeError: String?) {
    }

    private fun initContent() {
        loginUtil = FirebaseLoginUtil(this, this)

        if (loginUtil.isSignIn) {
            startActivity(Intent(this, SignInUpActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, SampleCrudMainActivity::class.java))
            finish()
        }
    }
}
