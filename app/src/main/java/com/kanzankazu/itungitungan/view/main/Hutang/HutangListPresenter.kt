package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import retrofit2.Call
import java.util.ArrayList

class HutangListPresenter(val mActivity: Activity, private val mView: HutangListContract.View) : HutangListContract.Presenter {
    override fun showProgressDialoPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(call: Call<*>?) {
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
                        if (hutang.piutang_penghutang_id.toLowerCase().contains(UserPreference.getInstance().uid.toLowerCase()) || (!hutang.hutangKeluargaId.isNullOrEmpty() && hutang.hutangKeluargaId.toLowerCase().equals(UserPreference.getInstance().uid.toLowerCase(), true))) {
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

    override fun hapusHutang(hutang: Hutang) {
        mView.dismissProgressDialogView()
        FirebaseDatabaseHandler.removeHutang(mActivity, hutang.gethId(), object : FirebaseDatabaseUtil.ValueListenerString {
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

    override fun approveHutang(hutang: Hutang) {
        val isIInclude = if (!hutang.piutang_penghutang_id.isNullOrEmpty()) UserPreference.getInstance().uid.contains(hutang.piutang_penghutang_id, true) else false
        val isIPenghutang = if (!hutang.penghutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.penghutangId, true) else false
        val isIPiutang = if (!hutang.piutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.piutangId, true) else false
        val isIFamily = if (!hutang.hutangKeluargaId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
        val isDataPenghutang = hutang.hutangRadioIndex == 0

        if (isIPiutang) {
            hutang.piutangPersetujuanBaru = true
        } else if (isIPenghutang) {
            hutang.penghutangPersetujuanBaru = true
        }

        FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerString {
            override fun onSuccess(message: String?) {
                mView.showToastView(message)
            }

            override fun onFailure(message: String?) {
                mView.showToastView(message)
            }
        })

    }
}
