package com.kanzankazu.itungitungan.view.main.Hutang

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangPembayaran
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_pay.*
import java.io.File


class HutangPayActivity : BaseActivity(), HutangPayContract.View, View.OnClickListener, FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
    private var mPresenter: HutangPayPresenter = HutangPayPresenter(this, this)
    private var isNew: Boolean = false
    private var huCil: HutangPembayaran = HutangPembayaran()
    private var hutang: Hutang = Hutang()
    private var mCurrentPhotoPath: String = ""
    private var mCurrentPhotoPathUri: Uri? = null

    private var nominalYangDiBayarkan: Int = 0
    private var nominalTotalPembayaran: Int = 0
    private var nominalSudahDiBayarkan: Int = 0

    private lateinit var pictureUtil2: PictureUtil2
    private lateinit var payNoteAdapter: HutangPayNoteAdapter
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_pay)

        setView()
        setListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pictureUtil2.REQUEST_IMAGE_CAMERA || requestCode == pictureUtil2.REQUEST_IMAGE_GALLERY) {
            mCurrentPhotoPath = pictureUtil2.onActivityResult(requestCode, resultCode, data)
            mCurrentPhotoPathUri = Uri.fromFile(File(mCurrentPhotoPath))

            imageListAdapter.addData(ImageModel(mCurrentPhotoPath, ""))
        }
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

    override fun onClick(v: View?) {
        if (v == tv_hutang_pay) dialogConfirmSave()
    }

    private fun dialogConfirmSave() {
        DialogUtil.makeDialogStandart2Button(
            this@HutangPayActivity,
            "Konfirmasi",
            "Apa data ini sudah benar, dan ingin melanjutkan pembayaran?",
            false,
            "Iya",
            "Tidak",
            object : DialogUtil.DialogStandart2Listener {
                override fun onClickButton1() {
                    mPresenter.saveSubHutangValidate(
                        isNew,
                        huCil,
                        hutang,
                        tv_hutang_pay_cicilan_ke,
                        et_hutang_pay_nominal,
                        et_hutang_pay_note,
                        imageListAdapter,
                        this@HutangPayActivity
                    )
                }

                override fun onClickButton2() {}
            })

    }


    override fun checkData(isFocus: Boolean): Boolean {
        return if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_pay_nominal, et_hutang_pay_nominal, isFocus)) false
        else InputValidUtil.isLenghtCharOver("Data kurang dari 6", til_hutang_pay_nominal, et_hutang_pay_nominal, 5)
    }

    override fun setSuggestNote(list: MutableList<NoteModel>) {
        payNoteAdapter.setData(list)
    }

    override fun onSuggestItemClick(s: String) {
        et_hutang_pay_note.setText(s)
    }

    override fun onSuggestItemRemoveClick(model: NoteModel) {

    }

    override fun onSuccessSaveUpdate(message: String?) {
        showToast(message)
        finish()
    }

    override fun onFailureSaveUpdate(message: String?) {
        showToast(message)
    }

    private fun setView() {
        payNoteAdapter = mPresenter.initSuggestAdapter(rv_hutang_pay_note)
        imageListAdapter = mPresenter.initImageAdapter(rv_hutang_pay_image, object : ImageListAdapter.ImageListContract {
            override fun onImageListRemove(data: ImageModel, position: Int) {
                imageListAdapter.removeAt(position)
            }

            override fun onImageListAdd(data: ImageModel, position: Int) {
                mPresenter.addImage()
            }
        })

        mPresenter.getSuggestNote()

        getBundle()
    }

    private fun getBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(Constants.Bundle.HUTANG)) {
                hutang = bundle.getParcelable(Constants.Bundle.HUTANG) as Hutang
                isNew = bundle.getBoolean(Constants.Bundle.HUTANG_NEW, false)
            }

            setBundleData()
        }
    }

    private fun setBundleData() {
        nominalTotalPembayaran = if (hutang.hutangCicilanIs) {
            hutang.hutangCicilanNominal.toInt() * hutang.hutangCicilanBerapaKali.toInt()
        } else {
            hutang.hutangNominal.toInt()
        }

        for (models in hutang.hutangPembayaranSub) {
            nominalSudahDiBayarkan += models.paymentNominal.toInt()
        }

        civ_hutang_pay_user.setImageDrawable(Utils.getInitialNameDrawable(hutang.debtorName))
        et_hutang_pay_nominal.setText(if (hutang.hutangCicilanIs) Utils.setRupiah(hutang.hutangCicilanNominal) else Utils.setRupiah((nominalTotalPembayaran - nominalSudahDiBayarkan).toString()))
        if (hutang.hutangCicilanIs) {
            tv_hutang_pay_total_nominal.text = "* total hutang = " + Utils.setRupiah(nominalTotalPembayaran.toString()) + " & total sudah di bayarkan = " + Utils.setRupiah(nominalSudahDiBayarkan.toString())
            ll_hutang_pay_cicilan.visibility = View.VISIBLE
            tv_hutang_pay_cicilan_ke.text = getString(R.string.hutang_pay_to, (hutang.hutangPembayaranSub.size + 1).toString(), hutang.hutangCicilanBerapaKali)
            tv_hutang_pay_cicilan_type.text = hutang.hutangCicilanBerapaKaliType
            if (!hutang.hutangCicilanIsBayarKapanSaja) {
                ll_hutang_pay_due_dt.visibility = View.VISIBLE
                tv_hutang_pay_due_dt.text = hutang.hutangCicilanTanggalAkhir
            } else {
                ll_hutang_pay_due_dt.visibility = View.GONE
            }
        } else {
            tv_hutang_pay_total_nominal.text = "* total hutang = " + Utils.setRupiah(nominalTotalPembayaran.toString()) + " & total sudah di bayarkan = " + Utils.setRupiah(nominalSudahDiBayarkan.toString())
            ll_hutang_pay_cicilan.visibility = View.GONE
        }
    }

    private fun setListener() {
        tv_hutang_pay.setOnClickListener(this)
        iv_hutang_pay_image_add.setOnClickListener { pictureUtil2 = mPresenter.addImage() }
    }
}
