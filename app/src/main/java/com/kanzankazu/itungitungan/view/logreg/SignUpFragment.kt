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
import com.kanzankazu.itungitungan.R.string.message_email_wrong_format
import com.kanzankazu.itungitungan.R.string.message_empty_field
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginEmailPasswordUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginGoogleUtil.RC_SIGN_IN
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.android.synthetic.main.fragment_signup.*


/**
 * Created by Faisal Bahri on 2019-11-08.
 */
class SignUpFragment : BaseFragment(),
    SignUpContract.View,
    FirebaseLoginUtil.FirebaseLoginListener,
    FirebaseLoginUtil.FirebaseLoginListener.EmailPass,
    FirebaseLoginUtil.FirebaseLoginListener.Google,
    TextWatcher {

    private lateinit var loginGoogleUtil: FirebaseLoginGoogleUtil
    private lateinit var loginEmailPasswordUtil: FirebaseLoginEmailPasswordUtil
    private var mParam1: String? = null
    private var mParam2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): SignUpFragment {
            val fragment = SignUpFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): Fragment {
            return SignUpFragment()
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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContent()
        initListener()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        for (fragment in childFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val accountResult = AndroidUtil.pickEmailAccountResult(requestCode, resultCode, data)
            etSignUpEmail.setText(accountResult)
        } else if (requestCode == RC_SIGN_IN) {
            loginGoogleUtil.signInActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Widget*/
    override fun afterTextChanged(s: Editable?) {
        checkData()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    /**
     *Firebase */
    override fun loginProgressShow() {
        showProgressDialog()
    }

    override fun loginProgressDismiss() {
        dismissProgressDialog()
    }

    override fun uiSignInSuccess(user: FirebaseUser?) {
        showSnackbar(getString(R.string.message_login_success))
        Log.d("Lihat", "uiSignInSuccess SignUpFragment" + user)
    }

    override fun uiSignOutSuccess() {
        showSnackbar(getString(R.string.message_logout_success))
    }

    override fun uiConnectionError(messageError: String, typeError: String) {
        showSnackbar(messageError)
    }

    override fun uiDisableEmailPassSubmitButton() {
        cvSignUpSubmit.isEnabled = false
    }

    override fun uiEnableEmailPassSubmitButton() {
        cvSignUpSubmit.isEnabled = true
    }

    override fun uiSignInGoogleFailure(s: String?) {
        showSnackbar(s)
    }

    override fun uiSignInGoogleFailed(s: String?) {
        showSnackbar(s)
    }

    override fun uiRevokeGoogleSuccess() {
    }

    override fun uiRevokeGoogleUnSuccess() {
    }

    /**
     * Contract*/
    override fun checkData(): Boolean {
        return when {
            InputValidUtil.isEmptyField(getString(message_empty_field), tilSignUpName, etSignUpName, false, ibSignUpNameClear) -> false
            InputValidUtil.isEmptyField(getString(message_empty_field), tilSignUpEmail, etSignUpEmail, false, ibSignUpEmailClear) -> false
            InputValidUtil.isEmptyField(getString(message_empty_field), tilSignUpPassword, etSignUpPassword, false, ibSignUpPasswordClear) -> false
            else -> InputValidUtil.isValidateEmail(getString(message_email_wrong_format), tilSignUpEmail, etSignUpEmail, false)
        }
    }

    private fun submitManualLogin() {
        if (checkData())
            loginEmailPasswordUtil.createAccount(etSignUpEmail.toString(), etSignUpPassword.toString())
    }

    private fun initContent() {
        loginEmailPasswordUtil = FirebaseLoginEmailPasswordUtil(mActivity, this, this)
        loginGoogleUtil = FirebaseLoginGoogleUtil(mActivity, this, this)
    }

    private fun initListener() {
        ibSignUpNameClear.setOnClickListener { etSignUpName.setText("") }
        ibSignUpEmailClear.setOnClickListener { etSignUpEmail.setText("") }
        ibSignUpPasswordClear.setOnClickListener { etSignUpPassword.setText("") }
        ibSignUpEmailPick.setOnClickListener {
            val intent = AndroidUtil.pickEmailAccountFromFragment()
            startActivityForResult(intent, AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT)
        }
        ibSignUpPasswordShowHide.setOnClickListener { InputValidUtil.showHidePassword(etSignUpPassword, ibSignUpPasswordShowHide) }

        etSignUpName.addTextChangedListener(this)
        etSignUpEmail.addTextChangedListener(this)
        etSignUpPassword.addTextChangedListener(this)
        etSignUpPassword.setOnEditorActionListener { v, actionId, event ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                checkData()
            }
            handled
        }

        cvSignUpSubmit.setOnClickListener { checkData() }
        cvSignUpGoogle.setOnClickListener {
            val intent = loginGoogleUtil.signInFromFragment()
            startActivityForResult(intent, RC_SIGN_IN)
        }
        cvSignUpFacebook.setOnClickListener { }
    }

}
