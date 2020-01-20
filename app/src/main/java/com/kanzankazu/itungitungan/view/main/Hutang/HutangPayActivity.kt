package com.kanzankazu.itungitungan.view.main.Hutang

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_pay.*


class HutangPayActivity : BaseActivity(), HutangPayContract.View, View.OnClickListener {
    private lateinit var payNoteAdapter: HutangPayNoteAdapter
    var mPresenter: HutangPayPresenter = HutangPayPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_pay)

        setView()
        setListener()
    }

    override fun onClick(v: View?) {
        when (v) {
            et_hutang_pay_date -> Utility.showCalendarDialog(this, et_hutang_pay_date)
            tv_hutang_pay -> checkData(true)
        }
    }

    override fun checkData(isFocus: Boolean): Boolean {
        return if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_pay_nominal, et_hutang_pay_nominal, isFocus)) false
        else !InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_pay_date, et_hutang_pay_date, isFocus)
    }

    override fun setSuggestNote() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_hutang_pay_note.layoutManager = linearLayoutManager
        payNoteAdapter = HutangPayNoteAdapter(this, this)
        rv_hutang_pay_note.adapter = payNoteAdapter
        //swipe_refresh.setColorSchemeResources(R.color.cyan)
        //swipe_refresh.setOnRefreshListener(this)
    }

    private fun setView() {
        mPresenter.getSuggestNote()
    }

    private fun setListener() {
        et_hutang_pay_date.setOnClickListener(this)
        tv_hutang_pay.setOnClickListener(this)
    }
}
