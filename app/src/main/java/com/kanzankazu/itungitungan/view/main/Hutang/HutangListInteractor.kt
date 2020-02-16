package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
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

    override fun checkReminderIsExist(inboxHistory: InboxHistory, listener: FirebaseDatabaseUtil.ValueListenerDataTrueFalse) {
    }

    override fun sendReminder(inboxHistory: InboxHistory, hutang: Hutang, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
    }

}
