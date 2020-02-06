package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangPembayaran
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.widget.gallery2.DepthPageTransformer
import com.kanzankazu.itungitungan.util.widget.gallery2.GalleryDetailPagerAdapter
import kotlinx.android.synthetic.main.layout_hutang_detail_dialog.*
import java.util.*


/**
 * Created by Faisal Bahri on 2020-02-04.
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HutangDetailDialog : DialogFragment(), HutangDetailDialogContract.View {

    private var isApproveNew: Boolean = false
    private var isApproveEdit: Boolean = false
    private var isApproveDelete: Boolean = false
    private var isApprovePay: Boolean = false
    private var hutang = Hutang()
    private lateinit var detailDialogPayAdapter: HutangDetailDialogPayAdapter

    private lateinit var mPresenter: HutangDetailDialogPresenter

    private val showHidePreview = { isShow: Boolean ->
        if (isShow) {
            fl_hutang_detail_dialog_image.visibility = View.VISIBLE
            ll_hutang_detail_dialog.visibility = View.GONE
        } else {
            fl_hutang_detail_dialog_image.visibility = View.GONE
            ll_hutang_detail_dialog.visibility = View.VISIBLE
        }
    }

    private val imageClickShow = View.OnClickListener {
        showHidePreview(true)

        val posData = if (it == iv_hutang_detail_dialog_piutang_0) 0 else 1

        val mGalleryDetailPagerAdapter = GalleryDetailPagerAdapter(activity, hutang.hutangBuktiGambar as ArrayList<String>?)
        iv_hutang_detail_dialog_piutang_preview.setPageTransformer(true, DepthPageTransformer())
        iv_hutang_detail_dialog_piutang_preview.adapter = mGalleryDetailPagerAdapter
        iv_hutang_detail_dialog_piutang_preview.offscreenPageLimit = 2
        iv_hutang_detail_dialog_piutang_preview.currentItem = posData
    }

    companion object {
        private const val HUTANG_PARAM = "hutang_param"
        private const val IS_HUTANG_APPROVE_NEW = "is_hutang_approve_new"
        private const val IS_HUTANG_APPROVE_EDIT = "is_hutang_approve_edit"
        private const val IS_HUTANG_APPROVE_DELETE = "is_hutang_approve_delete"
        private const val IS_HUTANG_APPROVE_PAY = "is_hutang_approve_pay"

        fun newInstance(hutang: Hutang, isApproveNew: Boolean, isApproveEdit: Boolean, isApproveDelete: Boolean, isApprovePay: Boolean): HutangDetailDialog {
            val fragment = HutangDetailDialog()
            val args = Bundle()
            args.putParcelable(HUTANG_PARAM, hutang)
            args.putBoolean(IS_HUTANG_APPROVE_NEW, isApproveNew)
            args.putBoolean(IS_HUTANG_APPROVE_EDIT, isApproveEdit)
            args.putBoolean(IS_HUTANG_APPROVE_DELETE, isApproveDelete)
            args.putBoolean(IS_HUTANG_APPROVE_PAY, isApprovePay)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): Fragment {
            return HutangDetailDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // request a window without the title
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.attributes.windowAnimations = R.style.AnimationRTL
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            hutang = arguments!!.getParcelable(HUTANG_PARAM)
            isApproveNew = arguments!!.getBoolean(IS_HUTANG_APPROVE_NEW)
            isApproveEdit = arguments!!.getBoolean(IS_HUTANG_APPROVE_EDIT)
            isApproveDelete = arguments!!.getBoolean(IS_HUTANG_APPROVE_DELETE)
            isApprovePay = arguments!!.getBoolean(IS_HUTANG_APPROVE_PAY)
        }

        // Pick a style based on the num.
        var style = STYLE_NORMAL
        var theme = 0
        val mNum = 0
        when ((mNum - 1) % 6) {
            1 -> style = STYLE_NO_TITLE
            2 -> style = STYLE_NO_FRAME
            3 -> style = STYLE_NO_INPUT
            4 -> style = STYLE_NORMAL
            5 -> style = STYLE_NORMAL
            6 -> style = STYLE_NO_TITLE
            7 -> style = STYLE_NO_FRAME
            8 -> style = STYLE_NORMAL
        }
        when ((mNum - 1) % 6) {
            4 -> theme = android.R.style.Theme_Holo
            5 -> theme = android.R.style.Theme_Holo_Light_Dialog
            6 -> theme = android.R.style.Theme_Holo_Light
            7 -> theme = android.R.style.Theme_Holo_Light_Panel
            8 -> theme = android.R.style.Theme_Holo_Light
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_hutang_detail_dialog, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = HutangDetailDialogPresenter(activity as AppCompatActivity, this)

        initView()
        initListener()
    }

    override fun showToastView(message: String?) {}

    override fun showSnackbarView(message: String?) {}

    override fun showRetryDialogView() {}

    override fun showProgressDialogView() {}

    override fun dismissProgressDialogView() {}

    private fun initView() {
        when {
            isApproveNew -> {
                tv_hutang_detail_dialog_Title.text = "Persetujuan HUTANG Piutang Baru"
                tv_hutang_detail_dialog_submit_tidak.visibility = View.VISIBLE
                tv_hutang_detail_dialog_submit_setuju.visibility = View.VISIBLE
            }
            isApproveEdit -> {
                tv_hutang_detail_dialog_Title.text = "Persetujuan HUTANG Piutang Ubah"
                tv_hutang_detail_dialog_submit_tidak.visibility = View.VISIBLE
                tv_hutang_detail_dialog_submit_setuju.visibility = View.VISIBLE
            }
            isApproveDelete -> {
                tv_hutang_detail_dialog_Title.text = "Persetujuan HUTANG Piutang Hapus"
                tv_hutang_detail_dialog_submit_tidak.visibility = View.VISIBLE
                tv_hutang_detail_dialog_submit_setuju.visibility = View.VISIBLE
            }
            isApprovePay -> {
                tv_hutang_detail_dialog_Title.text = "Persetujuan Pembayaran Hutang"
                tv_hutang_detail_dialog_submit_tidak.visibility = View.VISIBLE
                tv_hutang_detail_dialog_submit_setuju.visibility = View.VISIBLE
            }
            else -> {
                tv_hutang_detail_dialog_Title.text = "Detail HUTANG Piutang"
                tv_hutang_detail_dialog_submit_tidak.text = "TUTUP"
                tv_hutang_detail_dialog_submit_setuju.text = "DETAIL"
                if (!hutang.hutangEditableis || hutang.statusLunas) {
                    tv_hutang_detail_dialog_submit_setuju.isClickable = false
                    tv_hutang_detail_dialog_submit_setuju.isEnabled = false
                }
            }
        }

        when {
            hutang.statusLunas -> {
                tv_hutang_detail_dialog_status.text = "LUNAS"
                tv_hutang_detail_dialog_status.setTextColor(resources.getColor(R.color.green))
            }
            hutang.hutangPembayaranSub.size > 0 -> {
                tv_hutang_detail_dialog_status.text = "PROSES PEMBAYARAN"
                tv_hutang_detail_dialog_status.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            }
            else -> {
                tv_hutang_detail_dialog_status.text = "PROSES MENUNGGU PEMBAYARAN"
                tv_hutang_detail_dialog_status.setTextColor(resources.getColor(R.color.red))
            }
        }

        tv_hutang_detail_dialog_nominal.text = Utils.setRupiah(hutang.hutangNominal)
        tv_hutang_detail_dialog_pinjam_date.text = hutang.hutangPinjam
        if (hutang.hutangCicilanIs) {
            ll_hutang_detail_dialog_cicilan.visibility = View.VISIBLE
            tv_hutang_detail_dialog_cicilan_nominal.text = Utils.setRupiah(hutang.hutangCicilanNominal)
            tv_hutang_detail_dialog_cicilan.text = getString(R.string.installment_count, hutang.hutangCicilanBerapaKali, hutang.hutangCicilanBerapaKaliType)

            if (!hutang.hutangCicilanIsBayarKapanSaja) {
                tv_hutang_detail_dialog_cicilan_duedate.text = hutang.hutangCicilanTanggalAkhir
            } else if (hutang.hutangCicilanIsBayarKapanSaja) {
                tv_hutang_detail_dialog_cicilan_duedate.visibility = View.GONE
            }
        } else {
            ll_hutang_detail_dialog_cicilan.visibility = View.GONE
        }
        tv_hutang_detail_dialog_keperluan.text = hutang.hutangKeperluan
        tv_hutang_detail_dialog_catatan.text = hutang.hutangCatatan
        tv_hutang_detail_dialog_piutang_name.text = hutang.creditorName
        tv_hutang_detail_dialog_piutang_email.text = hutang.creditorEmail
        tv_hutang_detail_dialog_penghutang_name.text = hutang.debtorName
        tv_hutang_detail_dialog_penghutang_email.text = hutang.debtorEmail

        if (!hutang.hutangPembayaranSub.isNullOrEmpty()) {
            cv_hutang_detail_pay.visibility = View.VISIBLE
            cv_hutang_detail_pay_nominal_sudah.visibility = View.VISIBLE
            setPembayaranAdapter(hutang.hutangPembayaranSub)
            setPembayaranTotal(hutang.hutangPembayaranSub)
        } else {
            cv_hutang_detail_pay.visibility = View.GONE
            cv_hutang_detail_pay_nominal_sudah.visibility = View.GONE
        }

        if (hutang.hutangBuktiGambar != null) {
            cv_hutang_detail_dialog_piutang_image.visibility = View.VISIBLE
            when {
                hutang.hutangBuktiGambar!!.size == 1 -> {
                    Glide.with(this).load(hutang.hutangBuktiGambar!![0]).into(iv_hutang_detail_dialog_piutang_0)
                    iv_hutang_detail_dialog_piutang_0.visibility = View.VISIBLE
                    iv_hutang_detail_dialog_piutang_1.visibility = View.GONE
                }
                hutang.hutangBuktiGambar!!.size == 2 -> {
                    iv_hutang_detail_dialog_piutang_0.visibility = View.VISIBLE
                    iv_hutang_detail_dialog_piutang_1.visibility = View.VISIBLE
                    Glide.with(this).load(hutang.hutangBuktiGambar!![0]).into(iv_hutang_detail_dialog_piutang_0)
                    Glide.with(this).load(hutang.hutangBuktiGambar!![1]).into(iv_hutang_detail_dialog_piutang_1)
                }
                else -> {
                    iv_hutang_detail_dialog_piutang_0.visibility = View.GONE
                    iv_hutang_detail_dialog_piutang_1.visibility = View.GONE
                }
            }
        } else {
            cv_hutang_detail_dialog_piutang_image.visibility = View.GONE
        }


    }

    private fun setPembayaranAdapter(hutangPembayaranSub: MutableList<HutangPembayaran>) {
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_hutang_detail_pembayaran.layoutManager = linearLayoutManager
        detailDialogPayAdapter = HutangDetailDialogPayAdapter(activity, this, hutangPembayaranSub)
        rv_hutang_detail_pembayaran.adapter = detailDialogPayAdapter
    }

    private fun setPembayaranTotal(hutangPembayaranSub: MutableList<HutangPembayaran>) {
        var totalPembayaran = 0
        for (data in hutangPembayaranSub) {
            totalPembayaran += data.paymentNominal.toInt()
        }
        tv_hutang_list_detail_pay_nominal_sudah.text = Utils.setRupiah(totalPembayaran.toString())
    }

    private fun initListener() {
        iv_hutang_detail_dialog_piutang_preview_close.setOnClickListener { showHidePreview(false) }
        iv_hutang_detail_dialog_piutang_0.setOnClickListener(imageClickShow)
        iv_hutang_detail_dialog_piutang_1.setOnClickListener(imageClickShow)
        tv_hutang_detail_dialog_submit_setuju.setOnClickListener {
            dismiss()
            when {
                isApproveNew -> mPresenter.approveHutangNew(hutang, false)
                isApproveEdit -> mPresenter.approveHutangEdit(hutang, false)
                isApproveDelete -> mPresenter.approveHutangHapus(hutang)
                isApprovePay -> mPresenter.approveHutangCicilanPay(hutang)
                else -> moveToHutangAdd(hutang)
            }
        }
        tv_hutang_detail_dialog_submit_tidak.setOnClickListener {
            dismiss()
            when {
                isApproveDelete -> mPresenter.requestHutangHapus(hutang, true)
            }
        }
    }

    private fun moveToHutangAdd(hutang: Hutang) {
        val intent = Intent(activity, HutangAddEditActivity::class.java)
        intent.putExtra(Constants.Bundle.HUTANG, hutang)
        startActivity(intent)
    }

}
