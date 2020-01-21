package com.kanzankazu.itungitungan.view.main.Hutang

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangCicilan
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_pay.*
import retrofit2.Call


class HutangPayActivity : BaseActivity(), HutangPayContract.View, View.OnClickListener {
    private var isEdit: Boolean = false
    private var huCil: HutangCicilan = HutangCicilan()
    private lateinit var hutang: Hutang
    private lateinit var payNoteAdapter: HutangPayNoteAdapter
    var mPresenter: HutangPayPresenter = HutangPayPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_pay)

        setView()
        setListener()
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView(call: Call<*>?) {}

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_hutang_pay -> {
                saveSubHutangValidate()
            }
        }
    }

    override fun checkData(isFocus: Boolean): Boolean {
        return if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_pay_nominal, et_hutang_pay_nominal, isFocus)) false
        else InputValidUtil.isLenghtCharOver("Data kurang dari 6", til_hutang_pay_nominal, et_hutang_pay_nominal, 5)

    }

    override fun setSuggestNote(suggestNote: ArrayList<String>) {
        payNoteAdapter.setData(suggestNote)
    }

    private fun setView() {
        initSuggestAdapter()

        mPresenter.getSuggestNote()

        getBundle()
    }

    private fun getBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            isEdit = true
            if (bundle.containsKey(Constants.Bundle.HUTANG)) {
                hutang = bundle.getParcelable(Constants.Bundle.HUTANG) as Hutang
            }

            setBundleData()
        }
    }

    private fun setBundleData() {

    }

    private fun setListener() {
        tv_hutang_pay.setOnClickListener(this)
    }

    private fun initSuggestAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        payNoteAdapter = HutangPayNoteAdapter(this, this)
        rv_hutang_pay_note.layoutManager = linearLayoutManager
        rv_hutang_pay_note.adapter = payNoteAdapter
    }

    private fun saveSubHutangValidate() {
        if (checkData(true)){
            huCil.hId = hutang.hId
            huCil.paymentInstallmentTo = 1
            huCil.paymentNominal = Utils.getRupiahToString(et_hutang_pay_nominal).toString().trim()
            huCil.paymentDesc = et_hutang_pay_note.text.toString().trim()
            huCil.hutangCicilanBuktiGambar
            huCil.approvalCreditor = false
            huCil.approvalDebtor = true
        }else{
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }
}
