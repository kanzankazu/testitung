package com.kanzankazu.itungitungan.view.main

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.NetworkUtil

class DashboardFragmentPresenter(var mActivity: Activity, var mView: DashboardFragmentContract.View) : DashboardFragmentContract.Presenter {
    var mInteractor = DashboardFragmentinteractor(this)

    override fun showProgressDialoPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection() {
        mView.showRetryDialogView()
    }

    override fun getAllStatusData() {
        getHutang()
        getKeuangan()
    }

    override fun getHutang() {
        mView.showHideHutangProgressView(true)
        if (NetworkUtil.isConnected(mActivity)) {
            FirebaseDatabaseHandler.getHutangs(true, object : FirebaseDatabaseUtil.ValueListenerData {
                override fun onSuccess(dataSnapshot: DataSnapshot?) {
                    mView.showHideHutangProgressView(false)
                    mView.setHutangData(dataSnapshot!!)
                }

                override fun onFailure(message: String?) {
                    mView.showHideHutangProgressView(false)
                    mView.showSnackbarView(message)
                }
            })
        } else {
            mView.showHideHutangProgressView(false)
            onNoConnection()
        }
    }

    override fun getKeuangan() {

    }


}
