package com.kanzankazu.itungitungan.view.main

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.NetworkUtil

class DashboardFragmentPresenter(var mActivity: Activity, var mView: DashboardFragmentContract.View) : DashboardFragmentContract.Presenter {
    var mInteractor = DashboardFragmentinteractor(this)

    override fun showProgressDialogPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
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
                override fun onSuccessData(dataSnapshot: DataSnapshot?) {
                    mView.showHideHutangProgressView(false)
                    mView.setHutangData(dataSnapshot!!)
                }

                override fun onFailureData(message: String?) {
                    mView.showHideHutangProgressView(false)
                    mView.showSnackbarView(message)
                }
            })
        } else {
            mView.showHideHutangProgressView(false)
            onNoConnection(mActivity.getString(R.string.message_no_internet_network))
        }
    }

    override fun getKeuangan() {}


}
