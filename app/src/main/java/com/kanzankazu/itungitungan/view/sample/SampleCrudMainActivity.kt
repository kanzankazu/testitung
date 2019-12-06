package com.kanzankazu.itungitungan.view.sample

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity
import kotlinx.android.synthetic.main.activity_sample_crud_main.*

class SampleCrudMainActivity : BaseActivity(), FirebaseLoginUtil.FirebaseLoginListener {

    private lateinit var loginUtil: FirebaseLoginUtil
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crud_main)

        loginUtil = FirebaseLoginUtil(this, this)

        initContent()
        initListener()
    }

    private fun initContent() {
        firebaseUser = loginUtil.userProfile
        val userName = if (firebaseUser != null) firebaseUser!!.displayName else ""
        tv_title.text = getString(R.string.message_welcome, userName)

        if (!loginUtil.isSignIn) {
            moveToLogin()
        }
    }

    private fun initListener() {
        bt_createdata.setOnClickListener { startActivity(Intent(this, SampleCrudCreateActivity::class.java)) }
        bt_viewdata.setOnClickListener { startActivity(Intent(this, SampleCrudReadActivity::class.java)) }
        bt_logout.setOnClickListener {
            showProgressDialog()
            loginUtil.signOut(databaseUtil)
        }
    }

    override fun uiSignInSuccess(firebaseUser: FirebaseUser) {
        dismissProgressDialog()
    }

    override fun uiSignInFailed(errorMessage: String?) {
        dismissProgressDialog()
        showSnackbar(errorMessage)
    }

    override fun uiSignOutSuccess() {
        dismissProgressDialog()
        showSnackbar(getString(R.string.message_signout_success))
        moveToLogin()
    }

    override fun uiSignOutFailed(message: String?) {
        dismissProgressDialog()
        showSnackbar(message)
    }

    override fun uiConnectionError(messageError: String?, typeError: String?) {
        dismissProgressDialog()
        showSnackbar("$messageError , $typeError")
    }

    private fun moveToLogin() {
        startActivity(Intent(this, SignInUpActivity::class.java))
        finish()
    }
}
