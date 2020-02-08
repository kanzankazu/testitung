package com.kanzankazu.itungitungan.view.main.Hutang

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_list.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.util.*

class HutangListActivity : BaseActivity(), HutangListContract.View {

    private var hutangNominal: Int = 0
    private var piutangNominal: Int = 0
    private var mPresenter: HutangListPresenter = HutangListPresenter(this, this)
    private lateinit var hutangListMineAdapter: HutangListAdapter
    private lateinit var hutangListFamilyAdapter: HutangListAdapter
    private lateinit var hutangListLunasAdapter: HutangListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_list)

        Utils.setupAppToolbarForActivity(this, toolbar, "HUTANG")

        setView()
        setListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hutang_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menuHutangListAdd) {
            Utils.intentTo(this, HutangAddEditActivity::class.java, false)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Log.d("Lihat onBackPressed HutangListActivity", isFinishing.toString())
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView() {}

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    override fun onHutangUbahClick(hutang: Hutang) {
        moveToHutangAdd(hutang)
    }

    override fun onHutangBayarClick(hutang: Hutang) {
        val intent = Intent(this, HutangPayActivity::class.java)
        intent.putExtra(Constants.Bundle.HUTANG, hutang)
        intent.putExtra(Constants.Bundle.HUTANG_NEW, true)
        startActivity(intent)
    }

    override fun onHutangHapusClick(hutang: Hutang, position: Int, isHasReqDelete: Boolean) {
        removeDeleteDialog(hutang, isHasReqDelete)
    }

    override fun onHutangFilter(hutangs: MutableList<Hutang>) {
        setZeroHutangs()
        for (hutang in hutangs) {
            setTotalPiuHutang(hutang)//from filter
        }
    }

    override fun onHutangLihatClick(hutang: Hutang) {
        detailDialog(hutang, false, false, false, false)//lihat
    }

    override fun onHutangApproveNewClick(hutang: Hutang) {
        detailDialog(hutang, true, false, false, false)//approve
    }

    override fun onHutangApproveEditClick(hutang: Hutang) {
        detailDialog(hutang, false, true, false, false)//approve
    }

    override fun onHutangApproveDeleteClick(hutang: Hutang) {
        detailDialog(hutang, false, false, true, false)//approve
    }

    override fun onHutangApprovePayClick(hutang: Hutang) {
        detailDialog(hutang, false, false, false, true)
    }

    override fun setAllHutangsMine(hutangs: ArrayList<Hutang>) {
        if (hutangs.isNotEmpty()) {
            toggleEmptyDataLayout(tv_hutang_list_mine_empty, rv_hutang_list_mine, false)
            hutangListMineAdapter.setData(hutangs)
        } else {
            toggleEmptyDataLayout(tv_hutang_list_mine_empty, rv_hutang_list_mine, true)
        }
    }

    override fun setAllHutangsFamily(hutangs: ArrayList<Hutang>) {
        if (hutangs.isNotEmpty()) {
            toggleEmptyDataLayout(tv_hutang_list_family_empty, rv_hutang_list_family, false)
            hutangListFamilyAdapter.setData(hutangs)
        } else {
            toggleEmptyDataLayout(tv_hutang_list_family_empty, rv_hutang_list_family, true)
        }
    }

    override fun setAllHutangsLunas(hutangs: ArrayList<Hutang>) {
        if (hutangs.isNotEmpty()) {
            toggleEmptyDataLayout(tv_hutang_list_lunas_empty, rv_hutang_list_lunas, false)
            hutangListLunasAdapter.setData(hutangs)
            iv_hutang_list_mine_lunas_show_hide.visibility = View.VISIBLE
            Utils.setDrawableImageView(this, iv_hutang_list_mine_lunas_show_hide, R.drawable.ic_dropdown)
        } else {
            toggleEmptyDataLayout(tv_hutang_list_lunas_empty, rv_hutang_list_lunas, true)
            iv_hutang_list_mine_lunas_show_hide.visibility = View.GONE
            Utils.setDrawableImageView(this, iv_hutang_list_mine_lunas_show_hide, R.drawable.ic_dropup)
        }
    }

    override fun setZeroHutangs() {
        hutangNominal = 0
        piutangNominal = 0
    }

    override fun setTotalPiuHutang(hutang: Hutang) {
        val isIPenghutang = if (!hutang.debtorId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
        val isIPenghutangFamily = if (!hutang.debtorFamilyId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorFamilyId, true) else false
        val isIPiutangFamily = if (!hutang.creditorFamilyId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorFamilyId, true) else false

        if (!hutang.hutangNominal.isEmpty()) {
            if (isIPenghutang || isIPenghutangFamily) {
                hutangNominal += hutang.hutangNominal.toInt()
            } else if (isIPiutang || isIPiutangFamily) {
                piutangNominal += hutang.hutangNominal.toInt()
            }

            tv_hutang_list_hutang.text = Utils.setRupiah(hutangNominal.toString())
            tv_hutang_list_piutang.text = Utils.setRupiah(piutangNominal.toString())
        }
    }

    private fun setView() {
        hutangListMineAdapter = setRecyclerViewAdapter(rv_hutang_list_mine)
        hutangListFamilyAdapter = setRecyclerViewAdapter(rv_hutang_list_family)
        hutangListLunasAdapter = setRecyclerViewAdapter(rv_hutang_list_lunas)

        mPresenter.getAllHutang()
    }

    private fun setListener() {

        ll_hutang_list_mine_lunas_show_hide.setOnClickListener {
            if (hutangListLunasAdapter.getData().isNotEmpty()) {
                if (rv_hutang_list_lunas.visibility == View.VISIBLE) {
                    Utils.setDrawableImageView(this, iv_hutang_list_mine_lunas_show_hide, R.drawable.ic_dropup)
                    rv_hutang_list_lunas.visibility = View.GONE
                } else {
                    Utils.setDrawableImageView(this, iv_hutang_list_mine_lunas_show_hide, R.drawable.ic_dropdown)
                    rv_hutang_list_lunas.visibility = View.VISIBLE
                }
            }
        }

        et_hutang_list_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isNotEmpty()) {
                    ib_hutang_list_search_clear.visibility = View.VISIBLE
                } else {
                    ib_hutang_list_search_clear.visibility = View.GONE
                }

                hutangListMineAdapter.getFilter().filter(s.toString().trim())
                hutangListFamilyAdapter.getFilter().filter(s.toString().trim())
                hutangListLunasAdapter.getFilter().filter(s.toString().trim())
            }
        })

        ib_hutang_list_search_clear.setOnClickListener { et_hutang_list_search.text.clear() }
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView): HutangListAdapter {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        val hutangListAdapter = HutangListAdapter(this, this)
        recyclerView.adapter = hutangListAdapter

        return hutangListAdapter
    }

    private fun toggleEmptyDataLayout(viewEmpty: View, viewTarget: View, isEmptyData: Boolean) {
        if (isEmptyData) {
            viewEmpty.visibility = View.VISIBLE
            viewTarget.visibility = View.GONE
        } else {
            viewTarget.visibility = View.VISIBLE
            viewEmpty.visibility = View.GONE
        }
    }

    private fun moveToHutangAdd(hutang: Hutang) {
        val intent = Intent(this, HutangAddEditActivity::class.java)
        intent.putExtra(Constants.Bundle.HUTANG, hutang)
        startActivity(intent)
    }

    private fun removeDeleteDialog(hutang: Hutang, isHasReqDelete: Boolean) {
        DialogUtil.showIntroductionDialog(
                this,
                "",
                "Konfirmasi",
                if (isHasReqDelete) {
                    "Anda sudah meminta menghapus list hutang ini, apa anda ini mencabut penghapusan list ini?"
                } else {
                    "Apakah anda yakin ingin menghapus data ini?"
                }
                ,
                "Ya",
                "Tidak",
                false,
                -1,
                object : DialogUtil.IntroductionButtonListener {
                    override fun onFirstButtonClick() {
                        if (!isHasReqDelete) {
                            mPresenter.requestHutangHapus(hutang, false)
                            if (!hutang.hutangBuktiGambar.isNullOrEmpty()) {
                                mPresenter.hapusHutangCheckImage(hutang)
                            }
                        } else {
                            mPresenter.requestHutangHapus(hutang, true)
                        }
                    }

                    override fun onSecondButtonClick() {}
                }
        )

    }

    private fun detailDialog(hutang: Hutang, isApproveNew: Boolean, isApproveEdit: Boolean, isApproveDelete: Boolean, isApprovePay: Boolean) {

        val fm = supportFragmentManager
        val hutangDetailDialog = HutangDetailDialogFragment.newInstance(hutang, isApproveNew, isApproveEdit, isApproveDelete, isApprovePay)
        hutangDetailDialog.show(fm, "fragment_detail")

    }
}
