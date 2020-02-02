package com.kanzankazu.itungitungan.view.main.Hutang

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangPembayaran
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_pay.*
import retrofit2.Call
import java.io.File


class HutangPayActivity : BaseActivity(), HutangPayContract.View, View.OnClickListener, FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
    private var mPresenter: HutangPayPresenter = HutangPayPresenter(this, this)
    private var isNew: Boolean = false
    private var huCil: HutangPembayaran = HutangPembayaran()
    private var hutang: Hutang = Hutang()
    private var mCurrentPhotoPath: String = ""
    private var mCurrentPhotoPathUri: Uri? = null
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
                mPresenter.saveSubHutangValidate(isNew, huCil, hutang,
                        tv_hutang_pay_cicilan_ke,
                        et_hutang_pay_nominal,
                        et_hutang_pay_note, imageListAdapter, this)
            }
        }
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
        civ_hutang_pay_user.setImageDrawable(Utils.getInitialNameDrawable(hutang.debtorName))
        et_hutang_pay_nominal.setText(if (hutang.hutangCicilanIs) Utils.setRupiah(hutang.hutangCicilanNominal) else Utils.setRupiah(hutang.hutangNominal))
        if (hutang.hutangCicilanIs) {
            var nominalSudahDiBayarkan = 0
            for (models in hutang.hutangPembayaranSub) {
                nominalSudahDiBayarkan += models.paymentNominal.toInt()
            }
            tv_hutang_pay_total_nominal.text = "* total hutang = " + Utils.setRupiah(hutang.hutangNominal) + " & total sudah di bayarkan = " + nominalSudahDiBayarkan

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
            tv_hutang_pay_total_nominal.text = "* total hutang = " + Utils.setRupiah(hutang.hutangNominal)

            ll_hutang_pay_cicilan.visibility = View.GONE
        }
    }

    private fun setListener() {
        tv_hutang_pay.setOnClickListener(this)
        iv_hutang_pay_image_add.setOnClickListener { pictureUtil2 = mPresenter.addImage() }
    }

    /*private fun saveSubHutangValidate() {
        if (checkData(true)) {
            if (isNew) {
                huCil.createAt = DateTimeUtil.getCurrentDate().toString()
                huCil.createBy = UserPreference.getInstance().uid
            } else {
                huCil.updateAt = DateTimeUtil.getCurrentDate().toString()
                huCil.updateBy = UserPreference.getInstance().uid
            }
            huCil.hIdSub = hutang.hId + "_" + Date()
            huCil.hId = hutang.hId
            if (hutang.hutangCicilanIs) {
                huCil.paymentTo = tv_hutang_pay_cicilan_ke.text.toString().split(" dari ")[0].toInt()
            }
            huCil.paymentNominal = Utils.getRupiahToString(et_hutang_pay_nominal)
            huCil.paymentDesc = et_hutang_pay_note.text.toString().trim()
            huCil.approvalCreditor = false
            huCil.approvalDebtor = true

            checkStatusPay(et_hutang_pay_nominal)

            if (imageListAdapter.isNotEmptyData()) {
                val imageRemove = imageListAdapter.getRemoveDataString(true)
                val imageLocalUri = imageListAdapter.getDatasUri()
                val imageLocal = imageListAdapter.getDatasString(false)
                if (!isNew) {
                    if (imageRemove.size != 0) {
                        FirebaseStorageUtil.deleteImages(this, imageRemove, object : FirebaseStorageUtil.DoneRemoveListener {
                            override fun isFinised() {
                                mPresenter.saveImageData(hutang, huCil, imageLocalUri, this@HutangPayActivity)
                            }

                            override fun isFailed(message: String?) {
                                showSnackbar(message)
                            }
                        })
                    } else {
                        mPresenter.saveImageData(hutang, huCil, imageLocalUri, this)
                    }
                } else {
                    mPresenter.saveImageData(hutang, huCil, imageLocalUri, this)
                }
            } else {
                mPresenter.saveUpdateData(hutang, huCil, this)
            }
        } else {
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }

    private fun checkStatusPay(etHutangPayNominal: CurrencyEditText) {
        val nominalYangDiBayarkan = Utils.getRupiahToString(etHutangPayNominal).toInt()
        val nominalPembayaran: Int = if (hutang.hutangCicilanIs) {
            hutang.hutangCicilanNominal.toInt() * hutang.hutangCicilanBerapaKali.toInt()
        } else {
            hutang.hutangNominal.toInt()
        }

        if (nominalYangDiBayarkan == nominalPembayaran) {
            hutang.statusLunas = true
        } else if (nominalYangDiBayarkan > nominalPembayaran) {
            if (hutang.debtorId.equals(UserPreference.getInstance().uid, true)) {
                hutang.hutangRadioIndex = 1
                hutang.creditorId = UserPreference.getInstance().uid
                hutang.creditorName = UserPreference.getInstance().name
                hutang.creditorEmail = UserPreference.getInstance().email
                hutang.creditorApprovalNew = true
                hutang.creditorApprovalEdit = true
                hutang.creditorApprovalDelete = true

                hutang.debtorId = if (hutang.creditorId.isNotEmpty()) hutang.creditorId else ""
                hutang.debtorEmail = if (hutang.creditorEmail.isNotEmpty()) hutang.creditorEmail else ""
                hutang.debtorName = hutang.creditorName
                hutang.debtorApprovalNew = if (isNew) hutang.debtorApprovalNew else hutang.debtorId.isEmpty()
                hutang.debtorApprovalEdit = if (!isNew) !isNew else hutang.debtorId.isEmpty()
                hutang.debtorApprovalDelete = true
            } else {
                hutang.hutangRadioIndex = 0
                hutang.debtorId = UserPreference.getInstance().uid
                hutang.debtorName = UserPreference.getInstance().name
                hutang.debtorEmail = UserPreference.getInstance().email
                hutang.debtorApprovalNew = true
                hutang.debtorApprovalEdit = true
                hutang.debtorApprovalDelete = true

                hutang.creditorId = if (hutang.debtorId.isNotEmpty()) hutang.debtorId else ""
                hutang.creditorEmail = if (hutang.debtorEmail.isNotEmpty()) hutang.debtorEmail else ""
                hutang.creditorName = hutang.debtorName
                hutang.creditorApprovalNew = if (isNew) hutang.creditorApprovalNew else hutang.creditorId.isEmpty()
                hutang.creditorApprovalEdit = if (!isNew) !isNew else hutang.creditorId.isEmpty()
                hutang.creditorApprovalDelete = true
            }

        }

    }

    private fun initImageAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageListAdapter = ImageListAdapter(this, object : ImageListAdapter.ImageListContract {
            override fun onImageListRemove(data: ImageModel, position: Int) {
                imageListAdapter.removeAt(position)
            }

            override fun onImageListAdd(data: ImageModel, position: Int) {
                pictureUtil2 = mPresenter.addImage()
            }
        })
        rv_hutang_pay_image.layoutManager = linearLayoutManager
        rv_hutang_pay_image.adapter = imageListAdapter
    }

    private fun initSuggestAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        payNoteAdapter = HutangPayNoteAdapter(this, this)
        rv_hutang_pay_note.layoutManager = linearLayoutManager
        rv_hutang_pay_note.adapter = payNoteAdapter

        mPresenter.getSuggestNote()
    }

    private fun addImage() {
        val permissionUtil = AndroidPermissionUtil(this@HutangPayActivity, *AndroidPermissionUtil.permCameraGallery)
        pictureUtil2 = PictureUtil2(this@HutangPayActivity)
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            pictureUtil2.chooseGetImageDialog(this@HutangPayActivity)
        }
    }*/
}
