package com.kanzankazu.itungitungan.view.main.Hutang

import android.annotation.SuppressLint
import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import java.util.*

class HutangListPresenter(val mActivity: Activity, private val mView: HutangListContract.View) : HutangListContract.Presenter {
    override fun showProgressDialoPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        mView.showSnackbarView(message)
    }

    override fun getAllHutang() {
        mView.showProgressDialogView()
        FirebaseDatabaseHandler.getHutangs(false, object : FirebaseDatabaseUtil.ValueListenerData {
            @SuppressLint("DefaultLocale")
            override fun onSuccessData(dataSnapshot: DataSnapshot) {
                mView.dismissProgressDialogView()

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
                        }
                    }
                }

                mView.setAllHutangsMine(hutangMine)
                mView.setAllHutangsFamily(hutangFamily)
                mView.setAllHutangsLunas(hutangLunas)
            }

            override fun onFailureData(message: String?) {
                mView.dismissProgressDialogView()
                mView.showSnackbarView(message)
            }
        })
    }

    override fun approveHutangCicilanPay(hutang: Hutang) {
        for (i in hutang.hutangPembayaranSub.indices) {
            hutang.hutangPembayaranSub[i].approvalCreditor = true
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

    override fun approveHutangNew(hutang: Hutang, isCancel: Boolean) {
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

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
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false


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
        val isIPenghutang = if (!hutang.debtorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        val isIPiutang = if (!hutang.creditorId.isEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false

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
