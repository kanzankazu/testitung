package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.InboxHistory
import com.kanzankazu.itungitungan.util.Firebase.FirebaseConnectionUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class HutangListInteractor(private var mActivity: Activity, private var mPresenter: HutangListContract.Presenter) : HutangListContract.Interactor {
    override fun updateHutang(hutang: Hutang, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.updateHutang(mActivity, hutang, listener)
            }

            override fun noInternet(message: String?) {
                mPresenter.onNoConnection(message)
            }
        })
    }

    override fun checkReminderIsExist(inboxHistory: InboxHistory) {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.isExistSingleDayInboxHistory(inboxHistory, object : FirebaseDatabaseUtil.ValueListenerData {
                    override fun onSuccessData(dataSnapshot: DataSnapshot?) {
                        mPresenter.isExistReminder(dataSnapshot!!.exists(), inboxHistory)
                    }

                    override fun onFailureData(message: String?) {
                        mPresenter.showSnackBarPresenter(message!!)
                    }
                })
            }

            override fun noInternet(message: String?) {
                mPresenter.onNoConnection(message)
            }
        })
    }

    override fun setReminder(inboxHistory: InboxHistory) {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.setInboxHistory(mActivity, inboxHistory, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                    override fun onSuccessSaveUpdate(message: String?) {
                        mPresenter.onSuccessSaveReminder(message!!, inboxHistory)
                    }

                    override fun onFailureSaveUpdate(message: String?) {
                        mPresenter.showSnackBarPresenter(message!!)
                    }
                })
            }

            override fun noInternet(message: String?) {
                mPresenter.onNoConnection(message)
            }
        })
    }
}
