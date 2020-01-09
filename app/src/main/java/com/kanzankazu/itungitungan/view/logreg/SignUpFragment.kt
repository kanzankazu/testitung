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
import com.kanzankazu.itungitungan.R.string.message_field_email_wrong_format
import com.kanzankazu.itungitungan.R.string.message_field_empty
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signup.*


/**
 * Created by Faisal Bahri on 2019-11-08.
 */
class SignUpFragment : BaseFragment(),
        SignUpContract.View,
        TextWatcher {

    private lateinit var mContext: SignInUpActivity
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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContent()
        initListener()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val accountResult = AndroidUtil.pickEmailAccountResult(requestCode, resultCode, data)
            etSignUpEmail.setText(accountResult)
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
     * Contract*/
    override fun checkData(): Boolean {
        return when {
            InputValidUtil.isEmptyField(getString(message_field_empty), tilSignUpName, etSignUpName, false, ibSignUpNameClear) -> false
            InputValidUtil.isEmptyField(getString(message_field_empty), tilSignUpEmail, etSignUpEmail, false, ibSignUpEmailClear) -> false
            InputValidUtil.isEmptyField(getString(message_field_empty), tilSignUpPassword, etSignUpPassword, false, ibSignUpPasswordClear) -> false
            else -> InputValidUtil.isEmail(getString(message_field_email_wrong_format), tilSignUpEmail, etSignUpEmail, false)
        }
    }

    private fun initContent() {}

    private fun initListener() {
        cvSignUpMoveMasuk.setOnClickListener { mContext.changePager(0) }

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
        etSignUpPassword.setOnEditorActionListener { _, actionId, _ ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                submitManualLogin()
            }
            handled
        }

        cvSignUpSubmit.setOnClickListener { submitManualLogin() }
        cvSignUpGoogle.setOnClickListener { mContext.signInByGoogle() }
        cvSignUpFacebook.setOnClickListener { mContext.signInByFacebook() }
    }

    private fun submitManualLogin() {
        if (checkData()) mContext.signUpEmailPass(etSignUpName.text.toString().trim(), etSignUpEmail.text.toString().trim(), etSignUpPassword.text.toString().trim())
    }


}
