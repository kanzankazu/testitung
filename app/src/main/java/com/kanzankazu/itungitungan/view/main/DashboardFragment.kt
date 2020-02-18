package com.kanzankazu.itungitungan.view.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.view.base.BaseFragment
import com.kanzankazu.itungitungan.view.main.Hutang.HutangListActivity
import kotlinx.android.synthetic.main.fragment_dashboard_content.*

class DashboardFragment : BaseFragment(), DashboardFragmentContract.View, View.OnClickListener {

    private var mParam1: String? = ""
    private var mParam2: String? = ""
    private lateinit var mPresenter: DashboardFragmentPresenter

    companion object {

        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"
        fun newInstance(param1: String, param2: String): DashboardFragment {
            val fragment = DashboardFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
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
        return inflater.inflate(R.layout.fragment_dashboard_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = DashboardFragmentPresenter(mActivity, this)

        initView()
        initListener()

    }

    override fun onClick(v: View?) {
        when (v) {
            cv_dash_hutang -> moveTo()
            iv_dash_hutang_reload -> mPresenter.getHutang()
        }
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView() {
        DialogUtil.showRetryDialog(mActivity) {}
    }

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    override fun showHideHutangProgressView(isShow: Boolean) {
        if (isShow) {
            ll_dash_hutang_status.visibility = View.GONE
        } else {
            ll_dash_hutang_status.visibility = View.VISIBLE
        }
    }

    override fun setHutangData(dataSnapshot: DataSnapshot) {
        val hutangPenghutang = mutableListOf<Hutang>()
        val hutangPiutang = mutableListOf<Hutang>()
        val hutangPenghutangFamily = mutableListOf<Hutang>()
        val hutangPiutangFamily = mutableListOf<Hutang>()
        val hutangPay = mutableListOf<Hutang>()
        val hutangLunas = mutableListOf<Hutang>()
        for (snapshot in dataSnapshot.children) {
            val hutang = snapshot.getValue(Hutang::class.java)
            if (hutang != null) {
                if (hutang.debtorCreditorId.contains(UserPreference.getInstance().uid,true) ||
                    (hutang.debtorFamilyId.isNotEmpty() && hutang.debtorFamilyId.equals(UserPreference.getInstance().uid, true)) ||
                    (hutang.creditorFamilyId.isNotEmpty() && hutang.creditorFamilyId.equals(UserPreference.getInstance().uid, true))
                ) {
                    if (hutang.debtorId.equals(UserPreference.getInstance().uid, true)) hutangPenghutang.add(hutang)
                    if (hutang.debtorFamilyId.equals(UserPreference.getInstance().uid, true)) hutangPenghutangFamily.add(hutang)
                    if (hutang.creditorId.equals(UserPreference.getInstance().uid, true)) hutangPiutang.add(hutang)
                    if (hutang.creditorFamilyId.equals(UserPreference.getInstance().uid, true)) hutangPiutangFamily.add(hutang)
                    if (hutang.statusLunas) hutangLunas.add(hutang) else hutangPay.add(hutang)
                }
            }
        }

        tv_dash_hutang_penghutang_count.text = hutangPenghutang.size.toString()
        tv_dash_hutang_piutang_count.text = hutangPiutang.size.toString()
        tv_dash_hutang_penghutang_keluarga_count.text = hutangPenghutangFamily.size.toString()
        tv_dash_hutang_piutang_keluarga_count.text = hutangPiutangFamily.size.toString()
        tv_dash_hutang_pay_count.text = hutangPay.size.toString()
        tv_dash_hutang_lunas_count.text = hutangLunas.size.toString()

    }

    private fun initView() {
        mPresenter.getHutang()
    }

    private fun initListener() {
        cv_dash_hutang.setOnClickListener(this)
        iv_dash_hutang_reload.setOnClickListener(this)
    }

    private fun moveTo() {
        val intent = Intent(mActivity, HutangListActivity::class.java)
        startActivity(intent)
    }
}