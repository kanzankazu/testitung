package com.kanzankazu.itungitungan.view.logreg

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseUser
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.contract.SignInContract
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginEmailPasswordUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signin.*

/**
 * Created by Faisal Bahri on 2019-11-08.
 */
class SignInFragment : BaseFragment(), SignInContract.View, FirebaseLoginUtil.FirebaseLoginListener, TextWatcher {

    private lateinit var initLoginUtil: FirebaseLoginEmailPasswordUtil

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
        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        initComponent()
        initParam()
        initSession()
        initContent()
        initListener()

        return view
    }

    /*Widget*/
    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        checkData()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    /**/
    override fun loginProgressShow() {
        showProgressDialog()
    }

    override fun loginProgressDismiss() {
        dismissProgressDialog()
    }

    override fun uiSignInSuccess(user: FirebaseUser?) {

    }

    override fun uiSignOutSuccess() {
    }

    override fun uiDisableButton() {
    }

    override fun uiEnableButton() {
    }

    override fun uiSignInFailure(s: String?) {
    }

    override fun uiSignInFailed(s: String?) {
    }

    override fun uiRevokeSuccess() {
    }

    override fun uiPhoneInitialize() {
    }

    override fun uiPhoneCodeSent() {
    }

    override fun uiPhoneCodeNotSent() {
    }

    override fun uiPhoneVerifyFailed(message: String?) {
    }

    override fun uiPhoneVerifySuccess(smsCode: String?) {
    }

    /**/
    override fun checkData(): Boolean {
        return if (InputValidUtil.isEmptyField(getString(R.string.message_empty_field), tilLoginEmail, etLoginEmail)) false
        else if (!InputValidUtil.isValidateEmail("Format email salah", tilLoginEmail, etLoginEmail)) false
        else !InputValidUtil.isEmptyField(getString(R.string.message_empty_field), tilLoginPassword, etLoginPassword)
    }

    private fun initComponent() {

    }

    private fun initParam() {

    }

    private fun initSession() {

    }

    private fun initContent() {
        initLoginUtil = FirebaseLoginEmailPasswordUtil(activity, this)
    }

    private fun initListener() {
        etLoginEmail.addTextChangedListener(this)
        etLoginPassword.addTextChangedListener(this)

        cvSignInSubmit.setOnClickListener { }
        cvSignInGoogle.setOnClickListener { }
        cvSignInFacebook.setOnClickListener { }
    }
}
