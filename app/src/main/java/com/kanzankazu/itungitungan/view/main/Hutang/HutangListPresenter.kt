package com.kanzankazu.itungitungan.view.main.Hutang

import android.annotation.SuppressLint
import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.InboxHistory
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import java.util.*

class HutangListPresenter(val mActivity: Activity, private val mView: HutangListContract.View) : HutangListContract.Presenter, FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
    private var hutang: Hutang = Hutang()
    private var mInteractor = HutangListInteractor(mActivity, this)
    override fun showProgressDialogPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        dismissProgressDialogPresenter()
        mView.showSnackbarView(message)
    }

    override fun onSuccessSaveUpdate(message: String?) {
        dismissProgressDialogPresenter()
        mView.showSnackbarView(message)
    }

    override fun onFailureSaveUpdate(message: String?) {
        dismissProgressDialogPresenter()
        mView.showSnackbarView(message)
    }

    override fun showSnackBarPresenter(message: String) {
        dismissProgressDialogPresenter()
        mView.showSnackbarView(message)
    }

    override fun sendReminder(hutang: Hutang, adapterPosition: Int) {
        this.hutang = hutang
        val inboxHistory = InboxHistory().apply {
            inboxSenderUId = UserPreference.getInstance().uid
            inboxReceiverUId = hutang.debtorId
            inboxSenderReceiverUId = inboxSenderUId + "_" + inboxReceiverUId
            inboxTitle = "Pengingat Pembayaran Pinjaman"
            inboxMessage = if (hutang.hutangCicilanIs) {
                if (hutang.hutangPembayaranSub.isEmpty()) {
                    "anda mempunyai pembayaran dengan nominal Rp.${hutang.hutangNominal} yang di cicil dengan nominal Rp.${hutang.hutangCicilanNominal}, silahkan lakukan pembayaran dan konfirmasi dengan aplikasi ini."
                } else {
                    "anda mempunyai pembayaran dengan nominal Rp.${hutang.hutangNominal} yang di cicil dengan nominal Rp.${hutang.hutangCicilanNominal} dan anda sudah melakukan ${hutang.hutangPembayaranSub.size}x , silahkan lakukan pembayaran dan konfirmasi dengan aplikasi ini."
                }
            } else {
                if (hutang.hutangPembayaranSub.isEmpty()) {
                    "anda mempunyai pembayaran dengan nominal Rp.${hutang.hutangNominal}, silahkan lakukan pembayaran dan konfirmasi dengan aplikasi ini."
                } else {
                    "anda mempunyai pembayaran dengan nominal Rp.${hutang.hutangNominal} dan anda sudah melakukan ${hutang.hutangPembayaranSub.size}x , silahkan lakukan pembayaran dan konfirmasi dengan aplikasi ini."
                }
            }
            inboxTypeNotif = Constants.FirebasePushNotif.TypeNotif.hutangList
            inboxTypeView = Constants.InboxHistory.TypeView.Single
            inboxIsRead = false
            inboxIsSingleDay = inboxSenderReceiverUId + "_" + DateTimeUtil.currentDateString
        }

        showProgressDialogPresenter()
        mInteractor.checkReminderIsExist(inboxHistory)
    }

    override fun isExistReminder(exists: Boolean, inboxHistory: InboxHistory) {
        dismissProgressDialogPresenter()
        if (exists) {
            showSnackBarPresenter(mActivity.getString(R.string.message_database_data_exist))
        } else {
            showProgressDialogPresenter()
            mInteractor.setReminder(inboxHistory)
        }
    }

    override fun onSuccessSaveReminder(message: String, inboxHistory: InboxHistory) {
        dismissProgressDialogPresenter()
        showSnackBarPresenter(message)
        sendPushNotif(inboxHistory, hutang)
    }

    override fun getAllHutang() {
        showProgressDialogPresenter()
        FirebaseDatabaseHandler.getHutangs(false, object : FirebaseDatabaseUtil.ValueListenerData {
            @SuppressLint("DefaultLocale")
            override fun onSuccessData(dataSnapshot: DataSnapshot) {
                dismissProgressDialogPresenter()
                mView.setZeroHutangs()

                val hutangs = ArrayList<Hutang>()
                val hutangMine = ArrayList<Hutang>()
                val hutangFamily = ArrayList<Hutang>()
                val hutangLunas = ArrayList<Hutang>()
                for (snapshot in dataSnapshot.children) {
                    val hutang = snapshot.getValue(Hutang::class.java)
                    if (hutang != null) {
                        if (
                            hutang.debtorCreditorId.toLowerCase().contains(UserPreference.getInstance().uid.toLowerCase()) ||
                            (hutang.creditorFamilyId.isNotEmpty() && hutang.creditorFamilyId.toLowerCase().equals(UserPreference.getInstance().uid.toLowerCase(), true)) ||
                            (hutang.debtorFamilyId.isNotEmpty() && hutang.debtorFamilyId.toLowerCase().equals(UserPreference.getInstance().uid.toLowerCase(), true))
                        ) {
                            if (hutang.debtorCreditorId.toLowerCase().contains(UserPreference.getInstance().uid.toLowerCase())) {
                                if (hutang.statusLunas) {
                                    hutangLunas.add(hutang)
                                } else {
                                    hutangMine.add(hutang)
                                }
                            } else {
                                if (hutang.statusLunas) {
                                    hutangLunas.add(hutang)
                                } else {
                                    hutangFamily.add(hutang)
                                }
                            }
                            mView.setTotalPiuHutang(hutang)//from database
                            hutangs.add(hutang)
                        }
                    }
                }

                mView.setAllhutangs(hutangMine, hutangFamily, hutangLunas, hutangs)
            }

            override fun onFailureData(message: String?) {
                dismissProgressDialogPresenter()
                mView.showSnackbarView(message)
            }
        })
    }

    override fun approveHutangCicilanPay(hutang: Hutang) {
        for (i in hutang.hutangPembayaranSub.indices) {
            hutang.hutangPembayaranSub[i].approvalCreditor = true
        }

        showProgressDialogPresenter()
        mInteractor.updateHutang(hutang, this)
    }

    override fun approveHutangNew(hutang: Hutang, isCancel: Boolean) {
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

        if (!isCancel) {
            if (isIPiutang) {
                hutang.creditorApprovalNew = true
            } else if (isIPenghutang) {
                hutang.debtorApprovalNew = true
            }

            showProgressDialogPresenter()
            mInteractor.updateHutang(hutang, this)
        } else {
            hapusHutang(hutang)
        }
    }

    override fun approveHutangEdit(hutang: Hutang, isCancel: Boolean) {
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false


        if (isIPiutang) {
            hutang.creditorApprovalEdit = true
        } else if (isIPenghutang) {
            hutang.debtorApprovalEdit = true
        }

        showProgressDialogPresenter()
        mInteractor.updateHutang(hutang, this)
    }

    override fun requestHutangHapus(hutang: Hutang, isCancel: Boolean) {
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

        if (isCancel) {
            hutang.debtorApprovalDelete = true
            hutang.creditorApprovalDelete = true
        } else {
            if (isIPiutang) {
                hutang.debtorApprovalDelete = false
            } else if (isIPenghutang) {
                hutang.creditorApprovalDelete = false
            }
        }

        showProgressDialogPresenter()
        mInteractor.updateHutang(hutang, this)
    }

    override fun approveHutangHapus(hutang: Hutang) {
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

        if (isIPiutang) {
            hutang.debtorApprovalDelete = true
        } else if (isIPenghutang) {
            hutang.creditorApprovalDelete = true
        }

        showProgressDialogPresenter()
        mInteractor.updateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {
                hapusHutangCheckImage(hutang)
                mView.showToastView(message)
            }

            override fun onFailureSaveUpdate(message: String?) {
                mView.showToastView(message)
            }
        })
    }

    override fun hapusHutangCheckImage(hutang: Hutang) {
        mView.dismissProgressDialogView()

        if (hutang.hutangProofImage.isNotEmpty()) {
            FirebaseStorageUtil.deleteImages(mActivity, hutang.hutangProofImage, object : FirebaseStorageUtil.DoneRemoveListenerMultiple {
                override fun isFinised() {
                    hapusHutang(hutang)
                }

                override fun isFailed(message: String) {
                    mView.showSnackbarView(message)
                }
            })
        } else {
            hapusHutang(hutang)
        }

    }

    private fun hapusHutang(hutang: Hutang) {
        FirebaseDatabaseHandler.removeHutang(mActivity, hutang.hId, object : FirebaseDatabaseUtil.ValueListenerString {
            override fun onSuccessString(message: String?) {
                mView.dismissProgressDialogView()
                mView.showSnackbarView(message)
            }

            override fun onFailureString(message: String?) {
                mView.dismissProgressDialogView()
                mView.showSnackbarView(message)
            }
        })
    }

    private fun sendPushNotif(inboxHistory: InboxHistory, hutang: Hutang) {
        FirebaseDatabaseHandler.sendPushNotif(mActivity, hutang.debtorId, inboxHistory.inboxTitle, inboxHistory.inboxMessage, inboxHistory.inboxTypeNotif, this.hutang.hId, "")
    }
}
