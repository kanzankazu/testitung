package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.widget.gallery2.DepthPageTransformer
import com.kanzankazu.itungitungan.util.widget.gallery2.GalleryDetailPagerAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_list.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.util.*

class HutangListActivity : BaseActivity(), HutangListContract.View {

    private var hutangNominal: Int = 0
    private var piutangNominal: Int = 0
    private var mPresenter: HutangListPresenter = HutangListPresenter(this, this)
    private lateinit var hutangListAdapter: HutangListAdapter

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
        removeDeleteDialog(hutang, position, isHasReqDelete)
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

    override fun setAllHutangs(hutangs: ArrayList<Hutang>) {
        hutangListAdapter.setData(hutangs)
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
        intent.putExtra(Constants.Bundle.HUTANG, hutang)
        startActivity(intent)
    }

    private fun removeDeleteDialog(hutang: Hutang, position: Int, isHasReqDelete: Boolean) {
        Utils.showIntroductionDialog(
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
            object : Utils.IntroductionButtonListener {
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
        try {
            //val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialog.setContentView(R.layout.layout_hutang_detail_dialog)
            //dialog.setCancelable(false)
            val alertDialog: AlertDialog
            val dialogView = layoutInflater.inflate(R.layout.layout_hutang_detail_dialog, null)
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)

            val flHutangDetailDialogImage = dialogView.findViewById<FrameLayout>(R.id.fl_hutang_detail_dialog_image)
            val ivHutangDetailDialogPiutangPreview = dialogView.findViewById<ViewPager>(R.id.iv_hutang_detail_dialog_piutang_preview)
            val ivHutangDetailDialogPiutangPreviewClose = dialogView.findViewById<ImageView>(R.id.iv_hutang_detail_dialog_piutang_preview_close)
            val llHutangDetailDialog = dialogView.findViewById<LinearLayout>(R.id.ll_hutang_detail_dialog)
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
            val cvHutangDetailDialogPenghutangImage = dialogView.findViewById<CardView>(R.id.cv_hutang_detail_dialog_piutang_image)
            val ivHutangDetailDialogPenghutang0 = dialogView.findViewById<ImageView>(R.id.iv_hutang_detail_dialog_piutang_0)
            val ivHutangDetailDialogPenghutang1 = dialogView.findViewById<ImageView>(R.id.iv_hutang_detail_dialog_piutang_1)
            val tvHutangDetailDialogSubmitTidak = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_submit_tidak)
            val tvHutangDetailDialogSubmitSetuju = dialogView.findViewById<TextView>(R.id.tv_hutang_detail_dialog_submit_setuju)

            when {
                isApproveNew -> {
                    tvHutangDetailDialogTitle.text = "Persetujuan HUTANG Piutang Baru"
                    tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                    tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
                }
                isApproveEdit -> {
                    tvHutangDetailDialogTitle.text = "Persetujuan HUTANG Piutang Ubah"
                    tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                    tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
                }
                isApproveDelete -> {
                    tvHutangDetailDialogTitle.text = "Persetujuan HUTANG Piutang Hapus"
                    tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                    tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
                }
                isApprovePay -> {
                    tvHutangDetailDialogTitle.text = "Persetujuan Pembayaran Hutang"
                    tvHutangDetailDialogSubmitTidak.visibility = View.VISIBLE
                    tvHutangDetailDialogSubmitSetuju.visibility = View.VISIBLE
                }
                else -> {
                    tvHutangDetailDialogTitle.text = "Detail HUTANG Piutang"
                    tvHutangDetailDialogSubmitTidak.text = "TUTUP"
                    tvHutangDetailDialogSubmitSetuju.text = "DETAIL"
                    if (!hutang.hutangEditableis) {
                        tvHutangDetailDialogSubmitSetuju.isClickable = false
                        tvHutangDetailDialogSubmitSetuju.isEnabled = false
                    }
                }
            }

            tvHutangDetailDialogNominal.text = Utils.setRupiah(hutang.hutangNominal)
            tvHutangDetailDialogPinjamDate.text = hutang.hutangPinjam
            if (hutang.hutangCicilanIs) {
                llHutangDetailDialogCicilan.visibility = View.VISIBLE
                tvHutangDetailDialogCicilanNominal.text = Utils.setRupiah(hutang.hutangCicilanNominal)
                tvHutangDetailDialogCicilan.text = getString(R.string.installment_count, hutang.hutangCicilanBerapaKali, hutang.hutangCicilanBerapaKaliType)

                if (!hutang.hutangCicilanIsBayarKapanSaja) {
                    tvHutangDetailDialogCicilanDuedate.text = hutang.hutangCicilanTanggalAkhir
                } else if (hutang.hutangCicilanIsBayarKapanSaja) {
                    tvHutangDetailDialogCicilanDuedate.visibility = View.GONE
                }
            } else {
                llHutangDetailDialogCicilan.visibility = View.GONE
            }
            tvHutangDetailDialogKeperluan.text = hutang.hutangKeperluan
            tvHutangDetailDialogCatatan.text = hutang.hutangCatatan
            tvHutangDetailDialogPiutangName.text = hutang.creditorName
            tvHutangDetailDialogPiutangEmail.text = hutang.creditorEmail
            tvHutangDetailDialogPenghutangName.text = hutang.debtorName
            tvHutangDetailDialogPenghutangEmail.text = hutang.debtorEmail

            if (hutang.hutangBuktiGambar != null) {
                cvHutangDetailDialogPenghutangImage.visibility = View.VISIBLE
                when {
                    hutang.hutangBuktiGambar!!.size == 1 -> {
                        Glide.with(this).load(hutang.hutangBuktiGambar!![0]).into(ivHutangDetailDialogPenghutang0)
                        ivHutangDetailDialogPenghutang0.visibility = View.VISIBLE
                        ivHutangDetailDialogPenghutang1.visibility = View.GONE
                    }
                    hutang.hutangBuktiGambar!!.size == 2 -> {
                        ivHutangDetailDialogPenghutang0.visibility = View.VISIBLE
                        ivHutangDetailDialogPenghutang1.visibility = View.VISIBLE
                        Glide.with(this).load(hutang.hutangBuktiGambar!![0]).into(ivHutangDetailDialogPenghutang0)
                        Glide.with(this).load(hutang.hutangBuktiGambar!![1]).into(ivHutangDetailDialogPenghutang1)
                    }
                    else -> {
                        ivHutangDetailDialogPenghutang0.visibility = View.GONE
                        ivHutangDetailDialogPenghutang1.visibility = View.GONE
                    }
                }
            } else {
                cvHutangDetailDialogPenghutangImage.visibility = View.GONE
            }

            val showHidePreview = { isShow: Boolean ->
                if (isShow) {
                    flHutangDetailDialogImage.visibility = View.VISIBLE
                    llHutangDetailDialog.visibility = View.GONE
                } else {
                    flHutangDetailDialogImage.visibility = View.GONE
                    llHutangDetailDialog.visibility = View.VISIBLE
                }
            }

            val imageClickShow = View.OnClickListener {
                showHidePreview(true)

                val posData = if (it == ivHutangDetailDialogPenghutang0) 0 else 1

                val mGalleryDetailPagerAdapter = GalleryDetailPagerAdapter(this, hutang.hutangBuktiGambar as ArrayList<String>?)
                ivHutangDetailDialogPiutangPreview.setPageTransformer(true, DepthPageTransformer())
                ivHutangDetailDialogPiutangPreview.adapter = mGalleryDetailPagerAdapter
                ivHutangDetailDialogPiutangPreview.offscreenPageLimit = 2
                ivHutangDetailDialogPiutangPreview.currentItem = posData
            }

            alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)

            if (alertDialog.window != null)
                alertDialog.window.attributes.windowAnimations = R.style.PauseDialogAnimation

            alertDialog.show()

            ivHutangDetailDialogPiutangPreviewClose.setOnClickListener { showHidePreview(false) }
            ivHutangDetailDialogPenghutang0.setOnClickListener(imageClickShow)
            ivHutangDetailDialogPenghutang1.setOnClickListener(imageClickShow)
            tvHutangDetailDialogSubmitSetuju.setOnClickListener {
                alertDialog.dismiss()
                when {
                    isApproveNew -> mPresenter.approveHutangNew(hutang, false)
                    isApproveEdit -> mPresenter.approveHutangEdit(hutang, false)
                    isApproveDelete -> mPresenter.approveHutangHapus(hutang)
                    isApprovePay -> mPresenter.approveHutangCicilanPay(hutang)
                    else -> moveToHutangAdd(hutang)
                }
            }
            tvHutangDetailDialogSubmitTidak.setOnClickListener {
                alertDialog.dismiss()
                when {
                    isApproveDelete -> mPresenter.requestHutangHapus(hutang, true)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}
