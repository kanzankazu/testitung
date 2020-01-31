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
import com.kanzankazu.itungitungan.util.DeviceDetailUtil
import com.kanzankazu.itungitungan.util.Firebase.*
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil.RC_SIGN_IN
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.util.Utils
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

    private var viewPagerPosition: Int = 0
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

        initView()
        initListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            showProgressDialog()
            loginGoogleUtil.signInActivityResult(requestCode, resultCode, data)
        } else if (requestCode == GooglePhoneNumberValidation.REQ_CODE_G_PHONE_VALIDATION && resultCode == Activity.RESULT_OK) {
            if (GooglePhoneNumberValidation.onActivityResults(requestCode, resultCode, data, true)) {

                showProgressDialog()
                FirebaseDatabaseHandler.setPhoneNumberUserValidate(this, UserPreference.getInstance().uid, GooglePhoneNumberValidation.onActivityResults(requestCode, resultCode, data), object : FirebaseDatabaseUtil.ValueListenerString {
                    override fun onSuccess(message: String?) {
                        dismissProgressDialog()
                        UserPreference.getInstance().isOtp = true
                        moveToMain()
                    }

                    override fun onFailure(message: String?) {
                        dismissProgressDialog()
                        loginUtil.signOut(false)
                        showSnackbar(message)
                    }
                })
            } else {
                showSnackbar(getString(R.string.message_phone_failed))
            }
        } else {
            loginFacebookUtil.signInActivityResult(requestCode, resultCode, data)
        }
    }

    override fun uiSignInSuccess(firebaseUser: FirebaseUser) {
        dismissProgressDialog()

        val user = User()
        user.uId = firebaseUser.uid
        user.name = firebaseUser.displayName.toString()
        user.email = firebaseUser.email.toString()

        val userFirebaseSignIn: User = user
        FirebaseDatabaseHandler.getUserByUid(userFirebaseSignIn.uId, object : FirebaseDatabaseUtil.ValueListenerDataTrueFalse {
            override fun onSuccess(dataSnapshot: DataSnapshot, isExsist: Boolean) {
                if (isExsist) {
                    val userInDatabase = dataSnapshot.getValue(User::class.java) as User
                    signInUpControl(isSignUp = false, isFirst = false, user = userInDatabase)//SignIn
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

        FirebaseDatabaseHandler.isExistUser(userFirebaseSignUp, object : FirebaseDatabaseUtil.ValueListenerTrueFalse {
            override fun isExist(isExists: Boolean?) {
                if (isExists!!) {
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

    /*FirebaseDatabase*/
    override fun onSuccess(message: String) {
        dismissProgressDialog()
        if (loginGoogleUtil.isEmailVerfied(mAuth.currentUser)) {
            showSnackbar(message)
            moveToPhoneValidation()
        } else {
            signUpEmailSendVerify()
        }
    }

    override fun onFailure(message: String) {
        dismissProgressDialog()
        showSnackbar(message)
    }

    fun initView() {
        loginGoogleUtil = FirebaseLoginGoogleUtil(this, this, this)
        loginFacebookUtil = FirebaseLoginFacebookUtil(this, this, this)
        loginEmailPasswordUtil = FirebaseLoginEmailPasswordUtil(this, this, this)

        fragmentUtil = FragmentUtil(this, -1)
        viewPager = findViewById(R.id.vp_signInUp)
        slidePagerAdapter = fragmentUtil.setupTabLayoutViewPager(null, null, null, viewPager, SignInFragment.newInstance(), SignUpFragment.newInstance())

        Log.d("Lihat", "initView SignInUpActivity " + viewPager.currentItem)
        Log.d("Lihat", "initView SignInUpActivity " + slidePagerAdapter.count)

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

    fun initListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
                Log.d("Lihat", "onPageScrollStateChanged SignInUpActivity p0 : " + p0)
            }

            override fun onPageSelected(p0: Int) {
                viewPagerPosition = p0
                Log.d("Lihat", "onPageSelected SignInUpActivity p0 : " + p0)
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                Log.d("Lihat", "onPageScrolled SignInUpActivity p0 : " + p0)
                Log.d("Lihat", "onPageScrolled SignInUpActivity p1 : " + p1)
                Log.d("Lihat", "onPageScrolled SignInUpActivity p2 : " + p2)
            }
        })
    }

    fun moveToValidate() {
        if (UserPreference.getInstance().isLogin) {
            if (UserPreference.getInstance().isOtp) {
                moveToMain()
            } else {
                moveToPhoneValidation()
            }
        }
    }

    fun moveToPhoneValidation() {
        GooglePhoneNumberValidation.startPhoneNumberValidation(this)
    }

    fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun changePager(pos: Int) {
        viewPager.setCurrentItem(pos, true)
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

    fun signUpEmailSendVerify() {
        loginEmailPasswordUtil.sendEmailVerification()
    }

    fun signInUpControl(isSignUp: Boolean, isFirst: Boolean, user: User) {
        showProgressDialog()

        if (!UserPreference.getInstance().fcmToken.isNullOrEmpty()) {
            user.tokenFcm = UserPreference.getInstance().fcmToken
        } else {
            UserPreference.getInstance().fcmToken = user.tokenFcm
            user.tokenFcm = FirebaseInstanceId.getInstance().token.toString()
        }

        if (isSignUp) {
            user.name = name
            user.signIn = false
        } else {
            user.signIn = true
            user.phoneDetail = DeviceDetailUtil.getAllDataPhone(this)
        }

        if (isFirst) {
            user.firstSignIn = DateTimeUtil.getCurrentDate().toString()
            user.phoneCode = Utils.getUniquePsuedoID()
        }

        user.lastSignIn = DateTimeUtil.getCurrentDate().toString()
        FirebaseDatabaseHandler.setUser(this, user, this)
    }

}
