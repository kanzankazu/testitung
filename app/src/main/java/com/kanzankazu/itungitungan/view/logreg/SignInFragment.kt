package com.kanzankazu.itungitungan.view.logreg

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.google.firebase.auth.FirebaseUser
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginEmailPasswordUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil.*
import com.kanzankazu.itungitungan.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signin.*

/**
 * Created by Faisal Bahri on 2019-11-08.
 */
class SignInFragment : BaseFragment(), SignInContract.View, FirebaseLoginUtil.FirebaseLoginListener, FirebaseLoginUtil.FirebaseLoginListener.EmailPass, FirebaseLoginUtil.FirebaseLoginListener.Google, TextWatcher {

    private lateinit var loginEmailPasswordUtil: FirebaseLoginEmailPasswordUtil
    private lateinit var loginGoogleUtil: FirebaseLoginGoogleUtil

    private var mParam1: String? = null
    private var mParam2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): SignInFragment {
            val fragment = SignInFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): Fragment {
            return SignInFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContent()
        initListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val accountResult = pickEmailAccountResult(requestCode, resultCode, data)
            etSignInEmail.setText(accountResult)
        } else if (requestCode == FirebaseLoginGoogleUtil.RC_SIGN_IN) {
            loginGoogleUtil.signInActivityResult(requestCode, resultCode, data)
        }
    }

    /**/
    override fun loginProgressShow() {
        showProgressDialog()
    }

    override fun loginProgressDismiss() {
        dismissProgressDialog()
    }

    override fun uiSignInSuccess(user: FirebaseUser?) {
        Log.d("Lihat", "uiSignInSuccess SignInFragment $user")
        showSnackbar(getString(R.string.message_login_success))

        moveToNext()
    }

    override fun uiSignOutSuccess() {
        showSnackbar(getString(R.string.message_logout_success))
    }

    override fun uiConnectionError(messageError: String?, typeError: String?) {
        showSnackbar(messageError)
    }

    override fun uiDisableEmailPassSubmitButton() {
    }

    override fun uiEnableEmailPassSubmitButton() {
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

    /**/
    override fun afterTextChanged(s: Editable?) {
        checkData()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    /**/
    override fun checkData(): Boolean {
        return when {
            InputValidUtil.isEmptyField(getString(R.string.message_empty_field), tilSignInEmail, etSignInEmail, false, ibSignInEmailClear) -> false
            InputValidUtil.isEmptyField(getString(R.string.message_empty_field), tilSignInPassword, etSignInPassword, false, ibSignInPasswordClear) -> false
            else -> InputValidUtil.isValidateEmail(getString(R.string.message_email_wrong_format), tilSignInEmail, etSignInEmail, false)
        }
    }

    override fun moveToNext() {
        val signInUpActivity = mActivity as SignInUpActivity
        signInUpActivity.moveToNext()
    }

    private fun initContent() {
        loginEmailPasswordUtil = FirebaseLoginEmailPasswordUtil(mActivity, this, this)
        loginGoogleUtil = FirebaseLoginGoogleUtil(mActivity, this, this)
    }

    private fun initListener() {
        ibSignInEmailPick.setOnClickListener {
            val intent = pickEmailAccountFromFragment()
            startActivityForResult(intent, REQ_CODE_PICK_EMAIL_ACCOUNT)
        }
        ibSignInEmailClear.setOnClickListener {
            etSignInEmail.setText("")
        }
        ibSignInPasswordShowHide.setOnClickListener {
            InputValidUtil.showHidePassword(etSignInPassword, ibSignInPasswordShowHide)
        }
        ibSignInPasswordClear.setOnClickListener {
            etSignInPassword.setText("")
        }

        etSignInEmail.addTextChangedListener(this)
        etSignInPassword.addTextChangedListener(this)
        etSignInPassword.setOnEditorActionListener { v, actionId, event ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                submitManualLogin()
            }
            handled
        }

        cvSignInSubmit.setOnClickListener { submitManualLogin() }
        cvSignInGoogle.setOnClickListener {
            val intent = loginGoogleUtil.signInFromFragment()
            startActivityForResult(intent, FirebaseLoginGoogleUtil.RC_SIGN_IN)
        }
        cvSignInFacebook.setOnClickListener { }
    }

    private fun submitManualLogin() {
        if (checkData())
            loginEmailPasswordUtil.signIn(etSignInEmail.toString(), etSignInPassword.toString())
    }
}
