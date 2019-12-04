package com.kanzankazu.itungitungan.view.sample

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity
import kotlinx.android.synthetic.main.activity_sample_crud_main.*

class SampleCrudMainActivity : BaseActivity(), FirebaseLoginUtil.FirebaseLoginListener {

    private lateinit var loginUtil: FirebaseLoginUtil
    private lateinit var databaseUtil: FirebaseDatabaseUtil
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crud_main)

        loginUtil = FirebaseLoginUtil(this, this)
        databaseUtil = FirebaseDatabaseUtil()

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
        bt_logout.setOnClickListener { loginUtil.signOut(databaseUtil) }
    }

    override fun uiSignInSuccess(user: User) {
    }

    override fun uiSignInFailed(errorMessage: String?) {
    }

    override fun uiSignOutSuccess() {
        showSnackbar(getString(R.string.message_signout_success))
        moveToLogin()
    }

    override fun uiConnectionError(messageError: String?, typeError: String?) {
    }

    private fun moveToLogin() {
        startActivity(Intent(this, SignInUpActivity::class.java))
        finish()
    }
}
