package com.kanzankazu.itungitungan.view.logreg

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.Firebase.*
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil.RC_SIGN_IN
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.sample.SampleCrudMainActivity
import kotlinx.android.synthetic.main.fragment_signup.*


/**
 * Created by Faisal Bahri on 2019-11-05.
 */
class SignInUpActivity :
    BaseActivity(),
    SignInUpContract.View,
    FirebaseLoginUtil.FirebaseLoginListener,
    FirebaseLoginUtil.FirebaseLoginListener.Google,
    FirebaseLoginUtil.FirebaseLoginListener.EmailPass,
    FirebaseDatabaseUtil.valueListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseUtil: FirebaseDatabaseUtil
    private lateinit var loginUtil: FirebaseLoginUtil
    private lateinit var loginGoogleUtil: FirebaseLoginGoogleUtil
    private lateinit var loginFacebookUtil: FirebaseLoginFacebookUtil
    private lateinit var loginEmailPasswordUtil: FirebaseLoginEmailPasswordUtil
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var name: String

    private lateinit var fragmentUtil: FragmentUtil
    private lateinit var viewPager: ViewPager
    private lateinit var slidePagerAdapter: FragmentUtil.SlidePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initContent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            loginGoogleUtil.signInActivityResult(requestCode, resultCode, data)
        } else {
            loginFacebookUtil.signInActivityResult(requestCode, resultCode, data)
        }
    }

    /*LOGREG*/
    override fun uiSignInSuccess(user: User) {
        showProgressDialog()

        val mUser = User.getUser(mDatabaseUtil.rootRef, user.getuId(), true)
        if (mUser != null) {
            mUser.lastLogin = DateTimeUtil.getCurrentDate().toString()
            User.setUser(mDatabaseUtil.rootRef, this, mUser, this)
        } else {
            showSnackbar(getString(R.string.message_database_save_failed))
        }
    }

    override fun uiSignInFailed(errorMessage: String?) {
        showSnackbar(errorMessage)
    }

    override fun uiSignOutSuccess() {
        showSnackbar(getString(R.string.message_signout_success))
    }

    override fun uiConnectionError(messageError: String?, typeError: String?) {
        showSnackbar("$typeError, $messageError")
    }

    /**/
    override fun uiSignInGoogleFailed(messageError: String?) {
        dismissProgressDialog()
        showSnackbar(messageError)
    }

    override fun uiSignInGoogleFailure(messageError: String?) {
        dismissProgressDialog()
        showSnackbar(messageError)
    }

    override fun uiRevokeGoogleSuccess(string: String) {
    }

    override fun uiRevokeGoogleFailed(message: String) {
    }

    /**/
    override fun uiSignUpSuccess(user: User) {
        showProgressDialog()

        etSignUpName.setText("")
        etSignUpEmail.setText("")
        etSignUpPassword.setText("")

        if (User.isUserDataExist(mDatabaseUtil.rootRef, user)) {
            dismissProgressDialog()
            showSnackbar(getString(R.string.message_database_data_exist))
        } else {
            user.name = name
            user.firstLogin = DateTimeUtil.getCurrentDate().toString()
            user.lastLogin = DateTimeUtil.getCurrentDate().toString()
            User.setUser(mDatabaseUtil.rootRef, this, user, this)
        }
    }

    override fun uiSignUpFailed(errorMessage: String?) {
        showSnackbar(errorMessage)
    }

    override fun uiEnableEmailPassSubmitButton() {
    }

    override fun uiDisableEmailPassSubmitButton() {
    }

    override fun uiEmailVerifySentSuccess(firebaseUser: FirebaseUser?) {
        showSnackbar(getString(R.string.message_email_sent_success, firebaseUser?.email))
    }

    override fun uiEmailVerifySentFailed() {
        showSnackbar(getString(R.string.message_email_sent_failed))
    }

    /*DATABASE_FIREBASE*/
    override fun onSuccess(message: String) {
        dismissProgressDialog()

        if (loginGoogleUtil.isEmailVerfied(mAuth.currentUser)) {
            showSnackbar(message)
            moveToNext()
        } else {
            signUpEmailSendVerify()
        }
    }

    override fun onFailure(message: String) {
        dismissProgressDialog()
        showSnackbar(message)
    }

    private fun initContent() {
        mAuth = FirebaseAuth.getInstance()
        mDatabaseUtil = FirebaseDatabaseUtil()
        loginUtil = FirebaseLoginUtil(this, this)
        loginGoogleUtil = FirebaseLoginGoogleUtil(this, this, this)
        loginFacebookUtil = FirebaseLoginFacebookUtil(this, this, this)
        loginEmailPasswordUtil = FirebaseLoginEmailPasswordUtil(this, this, this)

        fragmentUtil = FragmentUtil(this, -1)
        viewPager = findViewById(R.id.vp_signInUp)
        slidePagerAdapter = fragmentUtil.setupTabLayoutViewPager(
            null,
            null,
            null,
            viewPager,
            SignInFragment.newInstance(),
            SignUpFragment.newInstance()
        )

        Log.d("Lihat", "initContent SignInUpActivity " + viewPager.currentItem)
        Log.d("Lihat", "initContent SignInUpActivity " + slidePagerAdapter.count)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect() // To remove to previously selected user's account so that the choose account UI will show
                }

                override fun onConnectionSuspended(i: Int) {

                }
            })
            .build()

        if (loginGoogleUtil.isEmailVerfied(mAuth.currentUser)) {
            moveToNext()
        }
    }

    fun moveToNext() {
        startActivity(Intent(this, SampleCrudMainActivity::class.java))
        finish()
    }

    fun signInByGoogle() {
        loginGoogleUtil.signIn(mAuth)
    }

    fun signInByFacebook() {
        loginFacebookUtil.signIn()
    }

    fun signInEmailPass(email: String, password: String) {
        loginEmailPasswordUtil.signIn(email, password)
    }

    fun signUpEmailPass(name: String, email: String, password: String) {
        this.name = name
        loginEmailPasswordUtil.createAccount(email, password)
    }

    fun signUpEmailSendVerify() {
        loginEmailPasswordUtil.sendEmailVerification()
    }

}
