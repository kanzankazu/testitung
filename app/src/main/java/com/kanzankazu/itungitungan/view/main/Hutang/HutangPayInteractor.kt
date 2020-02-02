package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.NetworkUtil

class HutangPayInteractor(var mActivity: Activity, var mPresenter: HutangPayContract.Presenter) : HutangPayContract.Interactor {
    override fun UpdateHutang(hutang: Hutang, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        val connected = NetworkUtil.isConnected(mActivity)
        if (connected) {
            FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {
                    listener.onSuccessSaveUpdate(message)
                }

                override fun onFailureSaveUpdate(message: String?) {
                    listener.onFailureSaveUpdate(message)
                }
            })
        } else {
            mPresenter.onNoConnection(mActivity.getString(R.string.message_no_internet_network))
        }
    }

}
