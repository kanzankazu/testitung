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
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_list.*
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import java.util.*

class HutangListActivity : BaseActivity(), HutangListContract.View {

    private lateinit var hutangListAdapter: HutangListAdapter
    private var hutangNominal: Int = 0
    private var piutangNominal: Int = 0

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

    override fun showprogressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    override fun onHutangUbahClick(hutang: Hutang) {
        moveToHutangAdd(hutang)
    }

    private fun moveToHutangAdd(hutang: Hutang) {
        val intent = Intent(this, HutangAddEditActivity::class.java)
        intent.putExtra(Constants.BUNDLE.Hutang, hutang)
        startActivity(intent)
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
                    showProgressDialog()
                    FirebaseDatabaseHandler.removeHutang(databaseUtil.rootRef, this@HutangListActivity, hutang.gethId(), object : FirebaseDatabaseUtil.ValueListenerString {
                        override fun onSuccess(message: String?) {
                            dismissProgressDialog()
                            showSnackbar(message)
                        }

                        override fun onFailure(message: String?) {
                            dismissProgressDialog()
                            showSnackbar(message)
                        }
                    })
                }

                override fun onSecondButtonClick() {}
            }
        )
    }

    override fun onHutangFilter(hutangs: MutableList<Hutang>) {
        hutangNominal = 0
        piutangNominal = 0

        for (hutang in hutangs) {
            totalPiuHutang(hutang)//from filter
        }
    }

    override fun onAgreementApproveClick(hutang: Hutang) {
        detailDialog(hutang, true)//approve
    }

    override fun onHutangLihatClick(hutang: Hutang) {
        detailDialog(hutang, false)//lihat
    }

    private fun setView() {
        setRecyclerView()
        getHutang()
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

    private fun getHutang() {
        showProgressDialog()
        FirebaseDatabaseHandler.getHutangs(databaseUtil.rootRef, false, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccess(dataSnapshot: DataSnapshot) {
                dismissProgressDialog()
                val hutangs = ArrayList<Hutang>()
                hutangNominal = 0
                piutangNominal = 0

                for (snapshot in dataSnapshot.children) {
                    val hutang = snapshot.getValue(Hutang::class.java)
                    if (hutang != null) {
                        if (hutang.piutang_penghutang_id.toLowerCase().contains(UserPreference.getInstance().uid.toLowerCase()) || (!hutang.hutangKeluargaId.isNullOrEmpty() && hutang.hutangKeluargaId.toLowerCase().contains(UserPreference.getInstance().uid.toLowerCase()))) {
                            hutangs.add(hutang)
                            totalPiuHutang(hutang)//from database
                        }
                    }
                }

                hutangListAdapter.setData(hutangs)
            }

            override fun onFailure(message: String?) {
                dismissProgressDialog()
                showSnackbar(message)
            }
        })
    }

    private fun totalPiuHutang(hutang: Hutang) {
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

    private fun detailDialog(hutang: Hutang, isApprove: Boolean) {
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

            if (isApprove) {
                tvHutangDetailDialogTitle.text = "Persetujuan Hutang Piutang"
                tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
            } else {
                tvHutangDetailDialogTitle.text = "Detail Hutang Piutang"
                tvHutangDetailDialogSubmitTidak.text = "TUTUP"
                tvHutangDetailDialogSubmitSetuju.text = "DETAIL"
            }

            tvHutangDetailDialogNominal.text = "Nominal Dibayarkan : " + Utils.setRupiah(hutang.hutangNominal)
            tvHutangDetailDialogPinjamDate.text = hutang.hutangPinjam
            if (hutang.hutangIsCicilan != null && hutang.hutangIsCicilan) {
                tvHutangDetailDialogCicilanNominal.text = "Nominal Cicilan : " + Utils.setRupiah(hutang.hutangCicilanNominal)
                tvHutangDetailDialogCicilan.text = getString(R.string.installment_count, hutang.hutangCicilanBerapaKali, hutang.hutangCicilanBerapaKaliType)
            } else {
                tvHutangDetailDialogCicilanNominal.visibility = View.GONE
                tvHutangDetailDialogCicilan.visibility = View.GONE
            }
            if (hutang.hutangisBayarKapanSaja != null && !hutang.hutangisBayarKapanSaja) {
                tvHutangDetailDialogCicilanDuedate.text = hutang.hutangCicilanTanggalAkhir
            } else {
                tvHutangDetailDialogCicilanDuedate.visibility = View.GONE
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
                if (isApprove) {
                    approveSubmit(hutang)
                } else {
                    moveToHutangAdd(hutang)
                }

            }
            tvHutangDetailDialogSubmitTidak.setOnClickListener {
                alertDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun approveSubmit(hutang: Hutang) {
        val isIInclude = if (!hutang.piutang_penghutang_id.isNullOrEmpty()) UserPreference.getInstance().uid.contains(hutang.piutang_penghutang_id, true) else false
        val isIPenghutang = if (!hutang.penghutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.penghutangId, true) else false
        val isIPiutang = if (!hutang.piutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.piutangId, true) else false
        val isIFamily = if (!hutang.hutangKeluargaId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
        val isDataPenghutang = hutang.hutangRadioIndex == 0

        if (isIPiutang) {
            hutang.piutangPersetujuan = true
        } else if (isIPenghutang) {
            hutang.penghutangPersetujuan = true
        }

        FirebaseDatabaseHandler.updateHutang(databaseUtil.rootRef, this, hutang, object : FirebaseDatabaseUtil.ValueListenerString {
            override fun onSuccess(message: String?) {
                showToast(message)
            }

            override fun onFailure(message: String?) {
                showToast(message)
            }
        })
    }
}
