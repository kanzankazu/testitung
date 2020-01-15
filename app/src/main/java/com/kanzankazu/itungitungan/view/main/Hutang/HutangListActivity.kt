package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_list.*
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import java.util.*

class HutangListActivity : BaseActivity(), HutangListContract.View {

    private var hutangNominal: Int = 0
    private var piutangNominal: Int = 0
    private var mPresenter: HutangListPresenter = HutangListPresenter(this, this)
    private lateinit var hutangListAdapter: HutangListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_list)

        Utils.setupAppToolbarForActivity(this, toolbar, "Hutang")

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

    override fun onHutangUbahClick(hutang: Hutang) {
        moveToHutangAdd(hutang)
    }

    override fun onHutangBayarClick(hutang: Hutang) {
        val intent = Intent(this, HutangPayActivity::class.java)
        intent.putExtra(Constants.BUNDLE.Hutang, hutang)
        startActivity(intent)
    }

    override fun onHutangHapusClick(hutang: Hutang, position: Int) {
        Utils.showIntroductionDialog(
            this,
            "",
            "Konfirmasi",
            "Apakah anda yakin ingin menghapus data ini?",
            "Ya",
            "Tidak",
            false,
            -1,
            object : Utils.IntroductionButtonListener {
                override fun onFirstButtonClick() {
                    if (!hutang.piutangId.isNullOrEmpty() && !hutang.penghutangId.isNullOrEmpty()) {
                        mPresenter.requestHutangHapus(hutang)
                    } else {
                        mPresenter.hapusHutang(hutang)
                    }
                }

                override fun onSecondButtonClick() {}
            }
        )
    }

    override fun onHutangFilter(hutangs: MutableList<Hutang>) {
        setZeroHutangs()
        for (hutang in hutangs) {
            setTotalPiuHutang(hutang)//from filter
        }
    }

    override fun onHutangLihatClick(hutang: Hutang) {
        detailDialog(hutang, isApproveNew = false, isApproveEdit = false, isApproveDelete = false)//lihat
    }

    override fun onHutangApproveNewClick(hutang: Hutang) {
        detailDialog(hutang, isApproveNew = true, isApproveEdit = false, isApproveDelete = false)//approve
    }

    override fun onHutangApproveEditClick(hutang: Hutang) {
        detailDialog(hutang, isApproveNew = false, isApproveEdit = true, isApproveDelete = false)//approve
    }

    override fun onHutangApproveDeleteClick(hutang: Hutang) {
        detailDialog(hutang, isApproveNew = false, isApproveEdit = false, isApproveDelete = true)//approve
    }

    override fun setAllHutangs(hutangs: ArrayList<Hutang>) {
        hutangListAdapter.setData(hutangs)
    }

    override fun setZeroHutangs() {
        hutangNominal = 0
        piutangNominal = 0
    }

    override fun setTotalPiuHutang(hutang: Hutang) {
        val isIInclude = if (!hutang.piutang_penghutang_id.isNullOrEmpty()) UserPreference.getInstance().uid.contains(hutang.piutang_penghutang_id, true) else false
        val isIPenghutang = if (!hutang.penghutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.penghutangId, true) else false
        val isIPiutang = if (!hutang.piutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.piutangId, true) else false
        val isIFamily = if (!hutang.hutangKeluargaId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
        val isDataPenghutang = hutang.hutangRadioIndex == 0

        if (!hutang.hutangNominal.isNullOrEmpty()) {
            if (isIPenghutang) {
                hutangNominal += hutang.hutangNominal.toInt()
            } else if (isIPiutang) {
                piutangNominal += hutang.hutangNominal.toInt()
            } else if (isIFamily) {
                if (isDataPenghutang) {
                    hutangNominal += hutang.hutangNominal.toInt()
                } else {
                    piutangNominal += hutang.hutangNominal.toInt()
                }
            }

            tv_hutang_list_hutang.text = Utils.setRupiah(hutangNominal.toString())
            tv_hutang_list_piutang.text = Utils.setRupiah(piutangNominal.toString())
        }
    }

    private fun setView() {
        setRecyclerView()

        mPresenter.getAllHutang()
    }

    private fun setListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rv_hutang_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (recyclerView.canScrollVertically(-1)) {
                        toolbar.elevation = 0f
                    } else {
                        toolbar.elevation = 50f
                    }
                }
            })
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

                hutangListAdapter.getFilter().filter(s.toString().trim())
            }
        })

        ib_hutang_list_search_clear.setOnClickListener { et_hutang_list_search.text.clear() }
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_hutang_list.layoutManager = linearLayoutManager
        //toggleEmptyDataLayout(false)
        hutangListAdapter = HutangListAdapter(this, this)
        rv_hutang_list.adapter = hutangListAdapter
        //swipe_refresh.setColorSchemeResources(R.color.cyan)
        //swipe_refresh.setOnRefreshListener(this)
    }

    private fun moveToHutangAdd(hutang: Hutang) {
        val intent = Intent(this, HutangAddEditActivity::class.java)
        intent.putExtra(Constants.BUNDLE.Hutang, hutang)
        startActivity(intent)
    }

    private fun detailDialog(hutang: Hutang, isApproveNew: Boolean, isApproveEdit: Boolean, isApproveDelete: Boolean) {
        try {
            //val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialog.setContentView(R.layout.layout_hutang_detail_dialog)
            //dialog.setCancelable(false)
            val alertDialog: AlertDialog
            val dialogView = layoutInflater.inflate(R.layout.layout_hutang_detail_dialog, null)
            val popupPromo = AlertDialog.Builder(this)
            popupPromo.setView(dialogView)

            val tvHutangDetailDialogTitle = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_Title)
            val tvHutangDetailDialogNominal = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_nominal)
            val tvHutangDetailDialogCicilanNominal = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_cicilan_nominal)
            val tvHutangDetailDialogPinjamDate = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_pinjam_date)
            val llHutangDetailDialogCicilan = dialogView.findViewById<LinearLayout>(R.id.ll_hutang_detail_dialog_cicilan)
            val tvHutangDetailDialogCicilan = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_cicilan)
            val tvHutangDetailDialogCicilanDuedate = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_cicilan_duedate)
            val tvHutangDetailDialogKeperluan = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_keperluan)
            val tvHutangDetailDialogCatatan = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_catatan)
            val tvHutangDetailDialogPiutangName = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_piutang_name)
            val tvHutangDetailDialogPiutangEmail = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_piutang_email)
            val tvHutangDetailDialogPenghutangName = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_penghutang_name)
            val tvHutangDetailDialogPenghutangEmail = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_penghutang_email)
            val tvHutangDetailDialogSubmitTidak = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_submit_tidak)
            val tvHutangDetailDialogSubmitSetuju = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_submit_setuju)

            when {
                isApproveNew -> {
                    tvHutangDetailDialogTitle.text = "Persetujuan Hutang Piutang Baru"
                    tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                    tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
                }
                isApproveEdit -> {
                    tvHutangDetailDialogTitle.text = "Persetujuan Hutang Piutang Ubah"
                    tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                    tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
                }
                isApproveDelete -> {
                    tvHutangDetailDialogTitle.text = "Persetujuan Hutang Piutang Hapus"
                    tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                    tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
                }
                else -> {
                    tvHutangDetailDialogTitle.text = "Detail Hutang Piutang"
                    tvHutangDetailDialogSubmitTidak.text = "TUTUP"
                    tvHutangDetailDialogSubmitSetuju.text = "DETAIL"
                }
            }

            tvHutangDetailDialogNominal.text = Utils.setRupiah(hutang.hutangNominal)
            tvHutangDetailDialogPinjamDate.text = hutang.hutangPinjam
            if (hutang.hutangIsCicilan != null && hutang.hutangIsCicilan) {
                llHutangDetailDialogCicilan.visibility = View.VISIBLE
                tvHutangDetailDialogCicilanNominal.text = Utils.setRupiah(hutang.hutangCicilanNominal)
                tvHutangDetailDialogCicilan.text = getString(R.string.installment_count, hutang.hutangCicilanBerapaKali, hutang.hutangCicilanBerapaKaliType)

                if (hutang.hutangisBayarKapanSaja != null && !hutang.hutangisBayarKapanSaja) {
                    tvHutangDetailDialogCicilanDuedate.text = hutang.hutangCicilanTanggalAkhir
                }
            } else {
                llHutangDetailDialogCicilan.visibility = View.GONE
            }
            tvHutangDetailDialogKeperluan.text = hutang.hutangKeperluan
            tvHutangDetailDialogCatatan.text = hutang.hutangCatatan
            tvHutangDetailDialogPiutangName.text = hutang.piutangNama
            tvHutangDetailDialogPiutangEmail.text = hutang.piutangEmail
            tvHutangDetailDialogPenghutangName.text = hutang.penghutangNama
            tvHutangDetailDialogPenghutangEmail.text = hutang.penghutangEmail

            alertDialog = popupPromo.create()
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()

            tvHutangDetailDialogSubmitSetuju.setOnClickListener {
                alertDialog.dismiss()
                when {
                    isApproveNew -> mPresenter.approveHutangNew(hutang)
                    isApproveEdit -> mPresenter.approveHutangEdit(hutang)
                    isApproveDelete -> mPresenter.approveHutangHapus(hutang, true)
                    else -> moveToHutangAdd(hutang)
                }
            }
            tvHutangDetailDialogSubmitTidak.setOnClickListener {
                alertDialog.dismiss()
                when {
                    isApproveDelete -> mPresenter.approveHutangHapus(hutang, false)
                    else -> moveToHutangAdd(hutang)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}
