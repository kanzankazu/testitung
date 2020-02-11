package com.kanzankazu.itungitungan.view.main.Hutang

import android.support.v7.app.AppCompatActivity
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil

class HutangDetailDialogPresenter(var mActivity: AppCompatActivity, var mView: HutangDetailDialogContract.View) : HutangDetailDialogContract.Presenter {
    override fun showProgressDialoPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        mView.showSnackbarView(message)
    }

    override fun approveHutangNew(hutang: Hutang, isCancel: Boolean) {
        val isIPenghutang = if (hutang.debtorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (hutang.creditorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

        if (!isCancel) {
            if (isIPiutang) {
                hutang.creditorApprovalNew = true
            } else if (isIPenghutang) {
                hutang.debtorApprovalNew = true
            }
            saveUpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {}
                override fun onFailureSaveUpdate(message: String?) {}
            })
        } else {
            hapusHutang(hutang)
        }
    }

    override fun approveHutangEdit(hutang: Hutang, isCancel: Boolean) {
        val isIPenghutang = if (hutang.debtorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (hutang.creditorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

        if (!isCancel) {
            if (isIPiutang) {
                hutang.creditorApprovalEdit = true
            } else if (isIPenghutang) {
                hutang.debtorApprovalEdit = true
            }
            saveUpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {}
                override fun onFailureSaveUpdate(message: String?) {}
            })
        } else {
            cancelChange(hutang)
        }
    }

    override fun approveHutangCicilanPay(hutang: Hutang, isCancel: Boolean) {
        val hutangPembayaranSub = hutang.hutangPembayaranSub

        if (!isCancel) {
            for (i in hutangPembayaranSub.indices) {
                hutangPembayaranSub[i].approvalCreditor = true
            }
            hutang.hutangPembayaranSub = hutangPembayaranSub
            saveUpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {}
                override fun onFailureSaveUpdate(message: String?) {}
            })
        } else {
            cancelChange(hutang)
        }
    }

    override fun approveHutangHapus(hutang: Hutang) {
        val isIPenghutang = if (hutang.debtorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (hutang.creditorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

        if (isIPiutang) {
            hutang.debtorApprovalDelete = true
        } else if (isIPenghutang) {
            hutang.creditorApprovalDelete = true
        }

        saveUpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {
                hapusHutangCheckImage(hutang)
            }

            override fun onFailureSaveUpdate(message: String?) {}
        })
    }

    override fun requestHutangHapus(hutang: Hutang, isCancel: Boolean) {
        val isIPenghutang = if (hutang.debtorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (hutang.creditorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

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

        saveUpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {}
            override fun onFailureSaveUpdate(message: String?) {}
        })
    }

    override fun hapusHutangCheckImage(hutang: Hutang) {
        mView.dismissProgressDialogView()

        if (hutang.hutangBuktiGambar.isNotEmpty()) {
            FirebaseStorageUtil.deleteImages(mActivity, hutang.hutangBuktiGambar, object : FirebaseStorageUtil.DoneRemoveListenerMultiple {
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

    private fun saveUpdateHutang(hutang: Hutang?, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {
                listener.onSuccessSaveUpdate(message)
                mView.showSnackbarView(message)
            }

            override fun onFailureSaveUpdate(message: String?) {
                listener.onFailureSaveUpdate(message)
                mView.showSnackbarView(message)
            }
        })
    }

    private fun cancelChange(hutang: Hutang) {
        if (hutang.backupBeforeChange != null) {
            val hutangBackup = hutang.backupBeforeChange
            hutang.backupBeforeChange = null
            saveUpdateHutang(hutangBackup, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {}
                override fun onFailureSaveUpdate(message: String?) {}
            })
        } else {
            saveUpdateHutang(hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {}
                override fun onFailureSaveUpdate(message: String?) {}
            })
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
}
