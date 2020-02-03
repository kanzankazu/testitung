package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import retrofit2.Call
import java.util.*

class HutangListPresenter(val mActivity: Activity, private val mView: HutangListContract.View) : HutangListContract.Presenter {
    override fun showProgressDialoPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection() {
    }

    override fun getAllHutang() {
        mView.showProgressDialogView()
        FirebaseDatabaseHandler.getHutangs(false, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccess(dataSnapshot: DataSnapshot) {
                mView.dismissProgressDialogView()

                mView.setZeroHutangs()

                val hutangs = ArrayList<Hutang>()
                for (snapshot in dataSnapshot.children) {
                    val hutang = snapshot.getValue(Hutang::class.java)
                    if (hutang != null) {
                        if (hutang.debtorCreditorId.toLowerCase().contains(UserPreference.getInstance().uid.toLowerCase()) || (hutang.hutangKeluargaId.isNotEmpty() && hutang.hutangKeluargaId.toLowerCase().equals(UserPreference.getInstance().uid.toLowerCase(), true))) {
                            hutangs.add(hutang)
                            mView.setTotalPiuHutang(hutang)//from database
                        }
                    }
                }

                mView.setAllHutangs(hutangs)
            }

            override fun onFailure(message: String?) {
                mView.dismissProgressDialogView()
                mView.showSnackbarView(message)
            }
        })
    }

    override fun approveHutangNew(hutang: Hutang, isCancel: Boolean) {
        val isIInclude = if (!hutang.debtorCreditorId.isEmpty()) UserPreference.getInstance().uid.contains(hutang.debtorCreditorId, true) else false
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
        val isIFamily = if (!hutang.hutangKeluargaId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
        val isDataPenghutang = hutang.hutangRadioIndex == 0

        if (!isCancel) {
            if (isIPiutang) {
                hutang.creditorApprovalNew = true
            } else if (isIPenghutang) {
                hutang.debtorApprovalNew = true
            }

            FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {
                    mView.showToastView(message)
                }

                override fun onFailureSaveUpdate(message: String?) {
                    mView.showToastView(message)
                }
            })
        } else {
            hapusHutang(hutang)
        }
    }

    override fun approveHutangEdit(hutang: Hutang, isCancel: Boolean) {
        val isIInclude = if (!hutang.debtorCreditorId.isEmpty()) UserPreference.getInstance().uid.contains(hutang.debtorCreditorId, true) else false
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
        val isIFamily = if (!hutang.hutangKeluargaId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
        val isDataPenghutang = hutang.hutangRadioIndex == 0


        if (isIPiutang) {
            hutang.creditorApprovalEdit = true
        } else if (isIPenghutang) {
            hutang.debtorApprovalEdit = true
        }

        FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {
                mView.showToastView(message)
            }

            override fun onFailureSaveUpdate(message: String?) {
                mView.showToastView(message)
            }
        })
    }

    override fun requestHutangHapus(hutang: Hutang, isCancel: Boolean) {
        val isIInclude = if (!hutang.debtorCreditorId.isEmpty()) UserPreference.getInstance().uid.contains(hutang.debtorCreditorId, true) else false
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
        val isIFamily = if (!hutang.hutangKeluargaId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
        val isDataPenghutang = hutang.hutangRadioIndex == 0

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

        FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
            override fun onSuccessSaveUpdate(message: String?) {
                mView.showToastView(message)
            }

            override fun onFailureSaveUpdate(message: String?) {
                mView.showToastView(message)
            }
        })
    }

    override fun approveHutangHapus(hutang: Hutang) {
        val isIInclude = if (!hutang.debtorCreditorId.isEmpty()) UserPreference.getInstance().uid.contains(hutang.debtorCreditorId, true) else false
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
        val isIFamily = if (!hutang.hutangKeluargaId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
        val isDataPenghutang = hutang.hutangRadioIndex == 0

        if (isIPiutang) {
            hutang.debtorApprovalDelete = true
        } else if (isIPenghutang) {
            hutang.creditorApprovalDelete = true
        }

        FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
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

        if (hutang.hutangBuktiGambar != null) {
            FirebaseStorageUtil.deleteImages(mActivity, hutang.hutangBuktiGambar, object : FirebaseStorageUtil.DoneRemoveListener {
                override fun isFinised() {
                    hapusHutang(hutang)
                }

                override fun isFailed(message: String?) {
                    mView.showSnackbarView(message)
                }
            })
        } else {
            hapusHutang(hutang)
        }

    }

    private fun hapusHutang(hutang: Hutang) {
        FirebaseDatabaseHandler.removeHutang(mActivity, hutang.hId, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate, FirebaseDatabaseUtil.ValueListenerString {
            override fun onSuccessSaveUpdate(message: String?) {}

            override fun onFailureSaveUpdate(message: String?) {}

            override fun onSuccess(message: String?) {
                mView.dismissProgressDialogView()
                mView.showSnackbarView(message)
            }

            override fun onFailure(message: String?) {
                mView.dismissProgressDialogView()
                mView.showSnackbarView(message)
            }
        })
    }
}
