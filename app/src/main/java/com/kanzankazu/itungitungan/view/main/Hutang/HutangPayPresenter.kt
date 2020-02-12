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

    override fun showProgressDialogPresenter() {
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
            huCil.paymentTo = hutang.hutangPembayaranSub.size + 1
            huCil.paymentNominal = Utils.getRupiahToString(et_hutang_pay_nominal)
            huCil.paymentDesc = et_hutang_pay_note.text.toString().trim()
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

            val nominalYangDiBayarkanSekarang = Utils.getRupiahToString(et_hutang_pay_nominal).toInt()

            var status = ""
            if (nominalSudahDiBayarkan + nominalYangDiBayarkanSekarang == nominalTotalPembayaran) {
                hutang.statusLunas = true
                status = Constants.Hutang.Status.Lunas
            } else if (nominalSudahDiBayarkan + nominalYangDiBayarkanSekarang > nominalTotalPembayaran) {
                if (hutang.debtorId.equals(UserPreference.getInstance().uid, true)) {
                    val tempHutangRadioIndex = 1
                    val tempCreditorId = UserPreference.getInstance().uid
                    val tempCreditorEmail = UserPreference.getInstance().name
                    val tempCreditorName = UserPreference.getInstance().email
                    val tempCreditorApprovalNew = true
                    val tempCreditorApprovalEdit = true
                    val tempCreditorApprovalDelete = true

                    val tempDebtorId = if (hutang.creditorId.isNotEmpty()) hutang.creditorId else ""
                    val tempDebtorName = if (hutang.creditorEmail.isNotEmpty()) hutang.creditorEmail else ""
                    val tempDebtorEmail = hutang.creditorName
                    val tempDebtorApprovalNew = if (isNew) hutang.debtorApprovalNew else hutang.debtorId.isEmpty()
                    val tempDebtorApprovalEdit = if (!isNew) !isNew else hutang.debtorId.isEmpty()
                    val tempDebtorApprovalDelete = true

                    val tempDebtorFamilyId = hutang.creditorFamilyId
                    val tempDebtorFamilyName = hutang.creditorFamilyName
                    val tempCreditorFamilyId = hutang.debtorFamilyId
                    val tempCreditorFamilyName = hutang.debtorFamilyName

                    hutang.hutangRadioIndex = tempHutangRadioIndex
                    hutang.creditorId = tempCreditorId
                    hutang.creditorName = tempCreditorEmail
                    hutang.creditorEmail = tempCreditorName
                    hutang.creditorApprovalNew = tempCreditorApprovalNew
                    hutang.creditorApprovalEdit = tempCreditorApprovalEdit
                    hutang.creditorApprovalDelete = tempCreditorApprovalDelete

                    hutang.debtorId = tempDebtorId
                    hutang.debtorEmail = tempDebtorName
                    hutang.debtorName = tempDebtorEmail
                    hutang.debtorApprovalNew = tempDebtorApprovalNew
                    hutang.debtorApprovalEdit = tempDebtorApprovalEdit
                    hutang.debtorApprovalDelete = tempDebtorApprovalDelete

                    hutang.debtorFamilyId = tempDebtorFamilyId
                    hutang.debtorFamilyName = tempDebtorFamilyName
                    hutang.creditorFamilyId = tempCreditorFamilyId
                    hutang.creditorFamilyName = tempCreditorFamilyName
                } else {
                    val tempHutangRadioIndex = 0
                    val tempDebtorId = UserPreference.getInstance().uid
                    val tempDebtorName = UserPreference.getInstance().name
                    val tempDebtorEmail = UserPreference.getInstance().email
                    val tempDebtorApprovalNew = true
                    val tempDebtorApprovalEdit = true
                    val tempDebtorApprovalDelete = true

                    val tempCreditorId = if (hutang.debtorId.isNotEmpty()) hutang.debtorId else ""
                    val tempCreditorEmail = if (hutang.debtorEmail.isNotEmpty()) hutang.debtorEmail else ""
                    val tempCreditorName = hutang.debtorName
                    val tempCreditorApprovalNew = if (isNew) hutang.creditorApprovalNew else hutang.creditorId.isEmpty()
                    val tempCreditorApprovalEdit = if (!isNew) !isNew else hutang.creditorId.isEmpty()
                    val tempCreditorApprovalDelete = true

                    val tempDebtorFamilyId = hutang.creditorFamilyId
                    val tempDebtorFamilyName = hutang.creditorFamilyName
                    val tempCreditorFamilyId = hutang.debtorFamilyId
                    val tempCreditorFamilyName = hutang.debtorFamilyName

                    hutang.hutangRadioIndex = tempHutangRadioIndex
                    hutang.debtorId = tempDebtorId
                    hutang.debtorName = tempDebtorName
                    hutang.debtorEmail = tempDebtorEmail
                    hutang.debtorApprovalNew = tempDebtorApprovalNew
                    hutang.debtorApprovalEdit = tempDebtorApprovalEdit
                    hutang.debtorApprovalDelete = tempDebtorApprovalDelete

                    hutang.creditorId = tempCreditorId
                    hutang.creditorEmail = tempCreditorEmail
                    hutang.creditorName = tempCreditorName
                    hutang.creditorApprovalNew = tempCreditorApprovalNew
                    hutang.creditorApprovalEdit = tempCreditorApprovalEdit
                    hutang.creditorApprovalDelete = tempCreditorApprovalDelete

                    hutang.debtorFamilyId = tempDebtorFamilyId
                    hutang.debtorFamilyName = tempDebtorFamilyName
                    hutang.creditorFamilyId = tempCreditorFamilyId
                    hutang.creditorFamilyName = tempCreditorFamilyName
                }

                val nominalBaru = (nominalSudahDiBayarkan + nominalYangDiBayarkanSekarang) - nominalTotalPembayaran
                hutang.hutangNominal = nominalBaru.toString()

                hutang.hutangCicilanIs = false
                hutang.hutangCicilanBerapaKali = ""
                hutang.hutangCicilanBerapaKaliType = ""
                hutang.hutangCicilanBerapaKaliPosisi = 0
                hutang.hutangCicilanNominal = ""
                hutang.hutangCicilanIsBayarKapanSaja = true
                hutang.hutangCicilanTanggalAkhir = ""

                val listOfProofImages = mutableListOf<String>()
                if (hutang.hutangPembayaranSub.isNotEmpty()) {
                    for (data in hutang.hutangPembayaranSub) {
                        if (data.paymentProofImage.isNotEmpty()) {
                            listOfProofImages.addAll(data.paymentProofImage)
                        }
                    }
                }

                if (listOfProofImages.isNotEmpty()) {
                    FirebaseStorageUtil.deleteImages(mActivity, listOfProofImages, object : FirebaseStorageUtil.DoneRemoveListenerMultiple {
                        override fun isFinised() {
                            hutang.hutangPembayaranSub.clear()
                        }

                        override fun isFailed(message: String) {
                            mView.showSnackbarView(message)
                        }
                    })
                } else {
                    hutang.hutangPembayaranSub.clear()
                }

                hutang.hutangPembayaranSub.clear()

                status = Constants.Hutang.Status.Berlebih
            }
            //End checkNominal

            DialogUtil.showConfirmationDialog(
                    mActivity,
                    "Info",
                    when (status) {
                        Constants.Hutang.Status.Lunas -> "Selamat, Hutang anda sudah LUNAS.\n gunakan terus aplikasi itung-itungan ini untuk mencatat keuangan anda terutama hutang."
                        Constants.Hutang.Status.Berlebih -> "Selamat, Hutang anda sudah LUNAS.\n tapi anda membayar berlebih, apakah kelebihan tersebut dicatat sebagai hutang atau bonus?."
                        else -> "anda sudah membayar Rp.$nominalYangDiBayarkanSekarang dari hutang anda tinggal Rp." + (nominalTotalPembayaran - nominalSudahDiBayarkan - nominalYangDiBayarkanSekarang)
                    }
            ) {
                huCil.approvalCreditor = status.equals(Constants.Hutang.Status.Lunas, true)
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
                    FirebaseStorageUtil.deleteImages(mActivity, imageRemove, object : FirebaseStorageUtil.DoneRemoveListenerMultiple {
                        override fun isFinised() {
                            saveImageData(hutang, huCil, imageLocalUri, listener)
                        }

                        override fun isFailed(message: String) {
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
        FirebaseStorageUtil.uploadImages(mActivity, "HUTANG_PAY", imageLocalUri, object : FirebaseStorageUtil.DoneListenerMultiple {
            override fun isFinised(imageDownloadUrls: ArrayList<String>) {
                huCil.paymentProofImage = imageDownloadUrls
                mView.showSnackbarView(mActivity.getString(R.string.message_image_save_success))
                saveUpdateData(hutang, huCil, listener)
            }

            override fun isFailed(message: String) {
                mView.showSnackbarView(message)
            }
        })
    }

    override fun saveUpdateData(hutang: Hutang, huCil: HutangPembayaran, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        hutang.hutangEditableis = hutang.creditorId.isEmpty()
        hutang.hutangPembayaranSub.add(huCil)

        mInteractor.UpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {
                listener.onSuccessSaveUpdate(message)
            }

            override fun onFailureSaveUpdate(message: String?) {
                listener.onFailureSaveUpdate(message)
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
