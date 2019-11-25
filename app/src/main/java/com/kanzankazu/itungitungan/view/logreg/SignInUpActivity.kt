package com.kanzankazu.itungitungan.view.logreg

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.*
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil.RC_SIGN_IN
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.util.NetworkUtil.isConnected
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.sample.SampleCrudMainActivity


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
    private lateinit var loginGoogleUtil: FirebaseLoginGoogleUtil
    private lateinit var loginFacebookUtil: FirebaseLoginFacebookUtil
    private lateinit var loginEmailPasswordUtil: FirebaseLoginEmailPasswordUtil
    private lateinit var connectionUtil: FirebaseConnectionUtil
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var fragmentUtil: FragmentUtil
    private lateinit var viewPager: ViewPager
    private lateinit var slidePagerAdapter: FragmentUtil.SlidePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initContent()

        mAuth = FirebaseAuth.getInstance()
        mDatabaseUtil = FirebaseDatabaseUtil(this)
    }

    override fun onStart() {
        super.onStart()
        // Check if USER is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        Log.d("Lihat", "onStart SignInUpActivity $currentUser")
        if (currentUser != null) {
            moveToNext()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            /*if (requestCode == RC_SIGN_IN && isConnected(this)) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    if (task.isSuccessful) {
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account)
                    } else {
                        Log.d("Lihat", "signInActivityResult FirebaseLoginGoogleUtil : " + task.exception!!.message)
                        uiSignInGoogleFailed(task.toString())
                    }
                } catch (e: ApiException) {
                    Log.d("Lihat", "signInActivityResult FirebaseLoginGoogleUtil : $e")
                    uiSignInGoogleFailure(connectionUtil.checkException(e.statusCode))
                }

            }*/
            loginGoogleUtil.signInActivityResult(requestCode, resultCode, data)
        } else {
            loginFacebookUtil.signInActivityResult(requestCode, resultCode, data)
        }
    }

    /*LOGREG*/
    override fun loginProgressShow() {
        showProgressDialog()
    }

    override fun loginProgressDismiss() {
        dismissProgressDialog()
    }

    override fun uiSignInSuccess(user: User) {
        User.setUser(mDatabaseUtil.rootRef, this, user, this)
    }

    override fun uiSignOutSuccess() {
        showSnackbar(getString(R.string.message_logout_success))
    }

    override fun uiConnectionError(messageError: String?, typeError: String?) {
        showSnackbar("$typeError , $messageError")
    }

    override fun uiSignInGoogleFailure(messageError: String?) {
        showSnackbar(messageError)
    }

    override fun uiSignInGoogleFailed(messageError: String?) {
        showSnackbar(messageError)
    }

    override fun uiRevokeGoogleSuccess(string: String) {
    }

    override fun uiRevokeGoogleFailed(message: String) {
    }

    override fun uiEnableEmailPassSubmitButton() {
    }

    override fun uiDisableEmailPassSubmitButton() {
    }

    /*DATABASE*/
    override fun onSuccess(message: String) {
        showSnackbar(message)
        moveToNext()
    }

    override fun onFailure(message: String) {
        showSnackbar(message)
    }

    private fun initContent() {
        loginGoogleUtil = FirebaseLoginGoogleUtil(this, this, this)
        loginFacebookUtil = FirebaseLoginFacebookUtil(this, this, this)
        loginEmailPasswordUtil = FirebaseLoginEmailPasswordUtil(this, this, this)
        connectionUtil = FirebaseConnectionUtil()

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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }

    private fun googleSignIn() {
        if (isConnected(this)) {
            val signInIntent = mGoogleSignInClient.signInIntent
            //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected) {
                mGoogleApiClient.clearDefaultAccountAndReconnect()
            } else {
                mGoogleApiClient.connect()
            }

            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        Log.d("Lihat", "firebaseAuthWithGoogle:" + acct?.id)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.id)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.idToken)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.displayName)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.email)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.givenName)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.familyName)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.photoUrl)
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct?.serverAuthCode)

        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    uiSignInSuccess(User(user!!))
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    uiSignInGoogleFailed(task.exception?.message)
                }
            }
    }

    fun moveToNext() {
        startActivity(Intent(this, SampleCrudMainActivity::class.java))
        finish()
    }

    fun signInByGoogle() {
        loginGoogleUtil.signIn()
    }

    fun signInByFacebook() {
        loginFacebookUtil.signIn()
    }

    fun signInEmailPass(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val user1 = User(user)
                    uiSignInSuccess(user1)
                } else {
                    uiSignInGoogleFailed(task.exception?.message)
                }
            }
    }

    fun signUpEmailPass(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val user1 = User(user)
                    user1.name = name
                    user1.byLogin = "Manual"
                    uiSignInSuccess(user1)
                } else {
                    uiSignInGoogleFailed(task.exception?.message)
                }
            }
    }

}
