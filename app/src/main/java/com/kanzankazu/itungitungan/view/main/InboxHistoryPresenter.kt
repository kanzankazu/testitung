package com.kanzankazu.itungitungan.view.main

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class InboxHistoryPresenter(private var mActivity: Activity, private var mView: InboxHistoryContract.View) : InboxHistoryContract.Presenter {
    private var mInteractor = InboxHistoryInteractor(mActivity, this)

    override fun showProgressDialogPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        mView.showRetryDialogView()
    }

    override fun getInboxHistoryData(isReload: Boolean) {
        FirebaseDatabaseHandler.getInboxHistorys(true, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccessData(dataSnapshot: DataSnapshot?) {
                mView.setInboxHistoryData(dataSnapshot,isReload)
            }

            override fun onFailureData(message: String?) {
                mView.showSnackbarView(message)
            }
        })
    }

}
