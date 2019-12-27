package com.kanzankazu.itungitungan.view.logreg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.iid.FirebaseInstanceId
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.Firebase.*
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil.RC_SIGN_IN
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.util.google.GooglePhoneNumberValidation
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.main.MainActivity
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
        FirebaseDatabaseUtil.ValueListenerString {

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
            showProgressDialog()
            loginGoogleUtil.signInActivityResult(requestCode, resultCode, data)
        } else if (requestCode == GooglePhoneNumberValidation.REQ_CODE_G_PHONE_VALIDATION && resultCode == Activity.RESULT_OK) {
            if (GooglePhoneNumberValidation.onActivityResults(requestCode, resultCode, data, true)) {
                showProgressDialog()
                User.setPhoneNumberUser(
                        databaseUtil.getRootRef(false, false),
                        UserPreference.getInstance().uid,
                        GooglePhoneNumberValidation.onActivityResults(requestCode, resultCode, data),
                        object : FirebaseDatabaseUtil.ValueListenerString {
                            override fun onSuccess(message: String?) {
                                dismissProgressDialog()
                                UserPreference.getInstance().otpStatus = true
                                moveToMain()
                            }

                            override fun onFailure(message: String?) {
                                dismissProgressDialog()
                                showSnackbar(message)
                            }
                        }
                )
            } else {
                showSnackbar(getString(R.string.message_phone_failed))
            }
        } else {
            loginFacebookUtil.signInActivityResult(requestCode, resultCode, data)
        }
    }

    override fun uiSignInSuccess(firebaseUser: FirebaseUser) {
        dismissProgressDialog()
        val userFirebaseSignIn = User(firebaseUser)
        User.getUserByUid(databaseUtil.getRootRef(false, false), userFirebaseSignIn.getuId(), true, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccess(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userDatabase = dataSnapshot.getValue(User::class.java) as User
                    signInUpControl(isSignUp = false, isFirst = false, user = userDatabase)
                } else {
                    userFirebaseSignIn.emailVerified = true
                    signInUpControl(isSignUp = false, isFirst = true, user = userFirebaseSignIn)
                }
            }

            override fun onFailure(message: String?) {
                showSnackbar(message)
            }
        })
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
    override fun uiSignUpSuccess(userFirebaseSignUp: User) {
        dismissProgressDialog()
        etSignUpName.setText("")
        etSignUpEmail.setText("")
        etSignUpPassword.setText("")

        User.isExistUser(databaseUtil.getRootRef(false, false), userFirebaseSignUp, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccess(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    showSnackbar(getString(R.string.message_database_data_exist))
                } else {
                    signInUpControl(isSignUp = true, isFirst = true, user = userFirebaseSignUp)
                }
            }

            override fun onFailure(message: String?) {
                showSnackbar(message)
            }
        })
    }

    override fun uiSignUpFailed(errorMessage: String?) {
        dismissProgressDialog()
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
        showSnackbar(message)
        moveToValidate()
    }

    override fun onFailure(message: String) {
        dismissProgressDialog()
        showSnackbar(message)
    }

    fun initContent() {
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

        moveToValidate()
    }

    fun moveToValidate() {
        if (loginGoogleUtil.isEmailVerfied(mAuth.currentUser) && UserPreference.getInstance().otpStatus == false) {
            moveToPhoneValidation()
        } else if (loginGoogleUtil.isEmailVerfied(mAuth.currentUser) && UserPreference.getInstance().otpStatus == true) {
            moveToMain()
        } else if(!loginGoogleUtil.isEmailVerfied(mAuth.currentUser) && UserPreference.getInstance().otpStatus == false){
            signUpEmailSendVerify()
        }
    }

    fun moveToPhoneValidation() {
        GooglePhoneNumberValidation.startPhoneNumberValidation(this)
    }

    fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun signInByGoogle() {
        loginGoogleUtil.signIn()
    }

    fun signInByFacebook() {
        loginFacebookUtil.signIn()
    }

    fun signInEmailPass(email: String, password: String) {
        showProgressDialog()
        loginEmailPasswordUtil.signIn(email, password)
    }

    fun signUpEmailPass(name: String, email: String, password: String) {
        showProgressDialog()
        this.name = name
        loginEmailPasswordUtil.createAccount(email, password)
    }

    private fun signUpEmailSendVerify() {
        loginEmailPasswordUtil.sendEmailVerification()
    }

    fun signInUpControl(isSignUp: Boolean, isFirst: Boolean, user: User) {
        showProgressDialog()

        if (!UserPreference.getInstance().fcmToken.isNullOrEmpty()) {
            user.tokenFcm = UserPreference.getInstance().fcmToken
        } else {
            user.tokenFcm = FirebaseInstanceId.getInstance().token
            UserPreference.getInstance().fcmToken = user.tokenFcm
        }

        if (isSignUp) {
            user.name = name
            user.signIn = false
        } else {
            user.signIn = true
        }

        if (isFirst) {
            user.firstSignIn = DateTimeUtil.getCurrentDate().toString()
        }

        user.lastSignIn = DateTimeUtil.getCurrentDate().toString()
        User.setUser(databaseUtil.getRootRef(false, false), this, user, this)
    }

}
