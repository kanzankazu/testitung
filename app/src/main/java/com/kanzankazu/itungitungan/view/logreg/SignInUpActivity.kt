package com.kanzankazu.itungitungan.view.logreg

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginEmailPasswordUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.util.widget.DateTimeUtils
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
    FirebaseLoginUtil.FirebaseLoginListener.EmailPass {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private lateinit var loginGoogleUtil: FirebaseLoginGoogleUtil
    private lateinit var emailPasswordUtil: FirebaseLoginEmailPasswordUtil
    private lateinit var mDatabaseUtil: FirebaseDatabaseUtil
    private lateinit var fragmentUtil: FragmentUtil

    private lateinit var viewPager: ViewPager

    private var slidePagerAdapter: FragmentUtil.SlidePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initContent()

        mAuth = FirebaseAuth.getInstance()

        mDatabaseUtil = FirebaseDatabaseUtil(this)
        userRef = mDatabaseUtil.getDataPrimaryKeyParent("user")
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        Log.d("Lihat", "onStart SignInUpActivity $currentUser")
        if (currentUser != null) {
            moveToNext()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FirebaseLoginGoogleUtil.RC_SIGN_IN) {
            loginGoogleUtil.signInActivityResult(requestCode, resultCode, data)
        }
    }

    override fun loginProgressShow() {
        showProgressDialog()
    }

    override fun loginProgressDismiss() {
        dismissProgressDialog()
    }

    override fun uiSignInSuccess(user: FirebaseUser?) {
        moveToNext()
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

    override fun uiRevokeGoogleSuccess() {
    }

    override fun uiRevokeGoogleFailed() {
    }

    override fun uiEnableEmailPassSubmitButton() {
    }

    override fun uiDisableEmailPassSubmitButton() {
    }

    private fun initContent() {
        loginGoogleUtil = FirebaseLoginGoogleUtil(this, this, this);
        emailPasswordUtil = FirebaseLoginEmailPasswordUtil(this, this, this);

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
    }

    fun moveToNext() {
        startActivity(Intent(this, SampleCrudMainActivity::class.java))
        finish()
    }

    fun signInByGoogle() {
        loginGoogleUtil.signIn()
    }

    fun signInByFacebook() {

    }

    fun signInEmailPass(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    moveToNext()
                    showToast("Authentication success signin. ")
                } else {
                    showToast("Authentication failed signin. " + task.exception?.message)
                }
            }
    }

    fun signUpEmailPass(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val key = userRef.push().key
                    showToast("Authentication success signup. ")
                } else {
                    showToast("Authentication failed signup. " + task.exception?.message)
                }
            }
    }

}
