package com.kanzankazu.itungitungan.view.logreg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil.*
import com.kanzankazu.itungitungan.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signin.*

/**
 * Created by Faisal Bahri on 2019-11-08.
 */
class SignInFragment :
    BaseFragment(),
    SignInContract.View,
    TextWatcher {

    private lateinit var mContext: SignInUpActivity

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context as SignInUpActivity
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
        }
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
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), tilSignInEmail, etSignInEmail, false, ibSignInEmailClear) -> false
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), tilSignInPassword, etSignInPassword, false, ibSignInPasswordClear) -> false
            else -> InputValidUtil.isEmail(getString(R.string.message_field_email_wrong_format), tilSignInEmail, etSignInEmail, false)
        }
    }

    private fun initContent() {}

    private fun initListener() {
        ibSignInEmailPick.setOnClickListener {
            val intent = pickEmailAccountFromFragment()
            startActivityForResult(intent, REQ_CODE_PICK_EMAIL_ACCOUNT)
        }
        ibSignInEmailClear.setOnClickListener { etSignInEmail.setText("") }
        ibSignInPasswordShowHide.setOnClickListener { InputValidUtil.showHidePassword(etSignInPassword, ibSignInPasswordShowHide) }
        ibSignInPasswordClear.setOnClickListener { etSignInPassword.setText("") }

        etSignInEmail.addTextChangedListener(this)
        etSignInPassword.addTextChangedListener(this)
        etSignInPassword.setOnEditorActionListener { _, actionId, _ ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                submitManualLogin()
            }
            handled
        }

        cvSignInSubmit.setOnClickListener { submitManualLogin() }
        cvSignInGoogle.setOnClickListener { mContext.signInByGoogle() }
        cvSignInFacebook.setOnClickListener { mContext.signInByFacebook() }
    }

    private fun submitManualLogin() {
        if (checkData()) mContext.signInEmailPass(etSignInEmail.text.toString().trim(), etSignInPassword.text.toString().trim())
    }
}
