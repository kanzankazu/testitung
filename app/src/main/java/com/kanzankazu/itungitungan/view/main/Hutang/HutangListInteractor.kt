package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseConnectionUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class HutangListInteractor(var mActivity: Activity, var mPresenter: HutangListPresenter) : HutangListContract.Interactor {
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

}
