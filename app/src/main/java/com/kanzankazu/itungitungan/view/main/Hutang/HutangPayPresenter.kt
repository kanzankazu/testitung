package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.TextView
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangPembayaran
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import java.util.*

class HutangPayPresenter(val mActivity: Activity, val mView: HutangPayContract.View) : HutangPayContract.Presenter {
    val mInteractor = HutangPayInteractor(mActivity, this)

    override fun showProgressDialoPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        mView.showSnackbarView(message)
    }

    override fun initImageAdapter(rv_hutang_pay_image: RecyclerView, listener: ImageListAdapter.ImageListContract): ImageListAdapter {
        val linearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        val imageListAdapter = ImageListAdapter(mActivity, listener)
        rv_hutang_pay_image.layoutManager = linearLayoutManager
        rv_hutang_pay_image.adapter = imageListAdapter

        return imageListAdapter
    }

    override fun initSuggestAdapter(rv_hutang_pay_note: RecyclerView): HutangPayNoteAdapter {
        val linearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        val payNoteAdapter = HutangPayNoteAdapter(mActivity, mView)
        rv_hutang_pay_note.layoutManager = linearLayoutManager
        rv_hutang_pay_note.adapter = payNoteAdapter

        return payNoteAdapter
    }

    override fun getSuggestNote() {
        val list = mutableListOf<NoteModel>()
        list.addAll(list)
        list.addAll(getSuggestNoteManual())

        mView.setSuggestNote(list)
    }

    override fun addImage(): PictureUtil2 {
        val permissionUtil = AndroidPermissionUtil(mActivity, *AndroidPermissionUtil.permCameraGallery)
        val pictureUtil2 = PictureUtil2(mActivity)
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            pictureUtil2.chooseGetImageDialog(mActivity)
        }
        return pictureUtil2

    }

    override fun saveSubHutangValidate(isNew: Boolean, huCil: HutangPembayaran, hutang: Hutang, tv_hutang_pay_cicilan_ke: TextView, et_hutang_pay_nominal: EditText, et_hutang_pay_note: EditText, imageListAdapter: ImageListAdapter, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        if (mView.checkData(true)) {
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

            //Start checkNominal
            val nominalTotalPembayaran: Int = if (hutang.hutangCicilanIs) {
                hutang.hutangCicilanNominal.toInt() * hutang.hutangCicilanBerapaKali.toInt()
            } else {
                hutang.hutangNominal.toInt()
            }

            var nominalSudahDiBayarkan = 0
            for (models in hutang.hutangPembayaranSub) {
                nominalSudahDiBayarkan += models.paymentNominal.toInt()
            }

            val nominalYangDiBayarkan = Utils.getRupiahToString(et_hutang_pay_nominal).toInt()

            var status = ""
            if (nominalSudahDiBayarkan + nominalYangDiBayarkan == nominalTotalPembayaran) {
                hutang.statusLunas = true
                status = Constants.Hutang.Status.Lunas
            } else if (nominalSudahDiBayarkan + nominalYangDiBayarkan > nominalTotalPembayaran) {
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

                    val tempDebtorFamilyId = hutang.creditorFamilyId
                    val tempDebtorFamilyName = hutang.creditorFamilyName
                    val tempCreditorFamilyId = hutang.debtorFamilyId
                    val tempCreditorFamilyName = hutang.debtorFamilyName
                    hutang.debtorFamilyId = tempDebtorFamilyId
                    hutang.debtorFamilyName = tempDebtorFamilyName
                    hutang.creditorFamilyId = tempCreditorFamilyId
                    hutang.creditorFamilyName = tempCreditorFamilyName
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

                    val tempDebtorFamilyId = hutang.creditorFamilyId
                    val tempDebtorFamilyName = hutang.creditorFamilyName
                    val tempCreditorFamilyId = hutang.debtorFamilyId
                    val tempCreditorFamilyName = hutang.debtorFamilyName
                    hutang.debtorFamilyId = tempDebtorFamilyId
                    hutang.debtorFamilyName = tempDebtorFamilyName
                    hutang.creditorFamilyId = tempCreditorFamilyId
                    hutang.creditorFamilyName = tempCreditorFamilyName
                }

                val nominalBaru = (nominalSudahDiBayarkan + nominalYangDiBayarkan) - nominalTotalPembayaran
                hutang.hutangNominal = nominalBaru.toString()

                hutang.hutangCicilanIs = false
                hutang.hutangCicilanBerapaKali = ""
                hutang.hutangCicilanBerapaKaliType = ""
                hutang.hutangCicilanBerapaKaliPosisi = 0
                hutang.hutangCicilanNominal = ""
                hutang.hutangCicilanIsBayarKapanSaja = true
                hutang.hutangCicilanTanggalAkhir = ""

                status = Constants.Hutang.Status.Berlebih
            }
            //End checkNominal

            DialogUtil.makeDialogStandart(
                    mActivity,
                    "Info",
                    when (status) {
                        Constants.Hutang.Status.Lunas -> "Selamat, Hutang anda sudah LUNAS.\n gunakan terus aplikasi itung-itungan ini untuk mencatat keuangan anda terutama hutang."
                        Constants.Hutang.Status.Berlebih -> "Selamat, Hutang anda sudah LUNAS.\n tapi anda membayar berlebih, apakah kelebihan tersebut dicatat sebagai hutang atau bonus?."
                        else -> "anda sudah membayar Rp.$nominalYangDiBayarkan dari hutang anda tinggal Rp." + (nominalTotalPembayaran - nominalSudahDiBayarkan - nominalYangDiBayarkan)
                    },
                    false) {
                saveValidateImageData(imageListAdapter, isNew, hutang, huCil, listener)
            }
        } else {
            mView.showSnackbarView(mActivity.getString(R.string.message_validation_failed))
        }
    }

    override fun saveValidateImageData(imageListAdapter: ImageListAdapter, isNew: Boolean, hutang: Hutang, huCil: HutangPembayaran, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        if (imageListAdapter.isNotEmptyData()) {
            val imageRemove = imageListAdapter.getRemoveDataString(true)
            val imageLocalUri = imageListAdapter.getDatasUri()
            if (!isNew) {
                if (imageRemove.size != 0) {
                    FirebaseStorageUtil.deleteImages(mActivity, imageRemove, object : FirebaseStorageUtil.DoneRemoveListener {
                        override fun isFinised() {
                            saveImageData(hutang, huCil, imageLocalUri, listener)
                        }

                        override fun isFailed(message: String?) {
                            mView.showSnackbarView(message)
                        }
                    })
                } else {
                    saveImageData(hutang, huCil, imageLocalUri, listener)
                }
            } else {
                saveImageData(hutang, huCil, imageLocalUri, listener)
            }
        } else {
            saveUpdateData(hutang, huCil, listener)
        }
    }

    override fun saveImageData(hutang: Hutang, huCil: HutangPembayaran, imageLocalUri: MutableList<Uri>, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        FirebaseStorageUtil.uploadImages(mActivity, "HUTANG_PAY", imageLocalUri as ArrayList<Uri>?, object : FirebaseStorageUtil.DoneListener {
            override fun isFinised(imageDownloadUrls: ArrayList<String>) {
                huCil.paymentProofImage = imageDownloadUrls
                mView.showSnackbarView(mActivity.getString(R.string.message_image_save_success))
                saveUpdateData(hutang, huCil, listener)
            }

            override fun isFailed(message: String?) {
                mView.showSnackbarView(message)
            }
        })
    }

    override fun saveUpdateData(hutang: Hutang, huCil: HutangPembayaran, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        hutang.hutangEditableis = false
        hutang.hutangPembayaranSub.add(huCil)

        mInteractor.UpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {
                mView.showSnackbarView(message)
                mActivity.finish()
            }

            override fun onFailureSaveUpdate(message: String?) {
                mView.showSnackbarView(message)
            }
        })
    }

    private fun getSuggestNoteManual(): MutableList<NoteModel> {
        val list = mutableListOf<NoteModel>()
        list.add(NoteModel().apply { title = "Terima kasih" })
        list.add(NoteModel().apply { title = "Maaf telat" })
        return list
    }
}
