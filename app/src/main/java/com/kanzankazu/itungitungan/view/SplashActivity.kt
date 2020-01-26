package com.kanzankazu.itungitungan.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.kanzankazu.itungitungan.BuildConfig
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseRemoteConfigOnNext
import com.kanzankazu.itungitungan.util.Firebase.FirebaseRemoteConfigUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity(), FirebaseLoginUtil.FirebaseLoginListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setView()

        val firebaseRemoteConfiguration = FirebaseRemoteConfigUtil(this, object : FirebaseRemoteConfigOnNext {
            override fun onContinueOnClick() {}

            override fun onNextAction() {
                initContent()
            }

            override fun onDismissProgressDialog() {}

            override fun onNoConnectionAction() {}

            override fun onFirebaseFetchFailed() {}
        })
        firebaseRemoteConfiguration.setupFirebaseRemoteConfig()

        Log.d("Lihat", "onCreate SplashActivity " + FirebaseInstanceId.getInstance().token)
        UserPreference.getInstance().fcmToken = FirebaseInstanceId.getInstance().token
    }

    private fun setView() {
        tv_splash_app_name.text = BuildConfig.APP_NAME
        tv_splash_version.text = getString(R.string.app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE.toString())
    }

    private fun initContent() {
        /*if (loginUtil.isSignIn) {
            startActivity(Intent(this, SignInUpActivity::class.java))
            finish()
        }*/

        startActivity(Intent(this, SignInUpActivity::class.java))
        finish()
    }
}
