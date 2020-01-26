package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.TextView
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangCicilan
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import java.util.*

class HutangPayPresenter(val mActivity: Activity, val mView: HutangPayContract.View) : HutangPayContract.Presenter {
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

    override fun checkOverPay(etHutangPayNominal: TextView, hutang: Hutang, isNew: Boolean) {
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

    override fun addImage(): PictureUtil2 {
        val permissionUtil = AndroidPermissionUtil(mActivity, *AndroidPermissionUtil.permCameraGallery)
        val pictureUtil2 = PictureUtil2(mActivity)
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            pictureUtil2.chooseGetImageDialog(mActivity)
        }
        return pictureUtil2

    }

    override fun saveSubHutangValidate(isNew: Boolean, huCil: HutangCicilan, hutang: Hutang, tv_hutang_pay_cicilan_ke: TextView, et_hutang_pay_nominal: EditText, et_hutang_pay_note: EditText, imageListAdapter: ImageListAdapter, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
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
                huCil.paymentInstallmentTo = tv_hutang_pay_cicilan_ke.text.toString().split(" dari ")[0].toInt()
            }
            huCil.paymentNominal = Utils.getRupiahToString(et_hutang_pay_nominal)
            huCil.paymentDesc = et_hutang_pay_note.text.toString().trim()
            huCil.approvalCreditor = false
            huCil.approvalDebtor = true

            checkOverPay(et_hutang_pay_nominal, hutang, isNew)

            if (imageListAdapter.isNotEmptyData()) {
                val imageRemove = imageListAdapter.getRemoveDataString(true)
                val imageLocalUri = imageListAdapter.getDatasUri()
                val imageLocal = imageListAdapter.getDatasString(false)
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

        } else {
            mView.showSnackbarView(mActivity.getString(R.string.message_validation_failed))
        }
    }

    override fun saveImageData(hutang: Hutang, huCil: HutangCicilan, imageLocalUri: MutableList<Uri>, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        FirebaseStorageUtil.uploadImages(mActivity, "HUTANG_PAY", imageLocalUri as ArrayList<Uri>?, object : FirebaseStorageUtil.DoneListener {
            override fun isFinised(imageDownloadUrls: ArrayList<String>) {
                huCil.hutangCicilanBuktiGambar = imageDownloadUrls
                mView.showSnackbarView(mActivity.getString(R.string.message_image_save_success))
                saveUpdateData(hutang, huCil, listener)
            }

            override fun isFailed(message: String?) {
                mView.showSnackbarView(message)
            }
        })
    }

    override fun saveUpdateData(hutang: Hutang, huCil: HutangCicilan, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        hutang.hutangEditableis = false
        hutang.hutangCicilanSub.add(huCil)

        FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
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
