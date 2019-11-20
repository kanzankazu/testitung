package com.kanzankazu.itungitungan.view.sample

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity
import kotlinx.android.synthetic.main.activity_sample_crud_main.*

class SampleCrudMainActivity : BaseActivity(), FirebaseLoginUtil.FirebaseLoginListener {

    private lateinit var firebaseLoginGoogleUtil: FirebaseLoginUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crud_main)

        firebaseLoginGoogleUtil = FirebaseLoginUtil(this, this)

        bt_createdata.setOnClickListener { startActivity(Intent(this, SampleCrudCreateActivity::class.java)) }
        bt_viewdata.setOnClickListener { startActivity(Intent(this, SampleCrudReadActivity::class.java)) }
        bt_logout.setOnClickListener {firebaseLoginGoogleUtil.signOut()}
    }

    override fun onStart() {
        super.onStart()
        if (!firebaseLoginGoogleUtil.isSignIn) {
            moveToLogin()
        }
    }

    private fun moveToLogin() {
        startActivity(Intent(this, SignInUpActivity::class.java))
        finish()
    }

    override fun loginProgressShow() {
        showProgressDialog()
    }

    override fun loginProgressDismiss() {
        dismissProgressDialog()
    }

    override fun uiSignInSuccess(user: FirebaseUser?) {
    }

    override fun uiSignOutSuccess() {
        showSnackbar(getString(R.string.message_logout_success))
        moveToLogin()
    }

    override fun uiConnectionError(messageError: String?, typeError: String?) {
    }
}
