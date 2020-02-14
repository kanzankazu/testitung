package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseConnectionUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class HutangPayInteractor(var mActivity: Activity, var mPresenter: HutangPayContract.Presenter) : HutangPayContract.Interactor {
    override fun UpdateHutang(hutang: Hutang, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate) {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil. FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                    override fun onSuccessSaveUpdate(message: String?) {
                        listener.onSuccessSaveUpdate(message)
                    }

                    override fun onFailureSaveUpdate(message: String?) {
                        listener.onFailureSaveUpdate(message)
                    }
                })
            }

            override fun noInternet(message: String?) {
                mPresenter.onNoConnection(message)
            }
        })
    }

}
