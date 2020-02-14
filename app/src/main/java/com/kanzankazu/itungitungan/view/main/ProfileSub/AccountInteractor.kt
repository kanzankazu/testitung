package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.app.Activity
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.util.Firebase.FirebaseConnectionUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class AccountInteractor(var mActivity: Activity, var mPresenter: AccountPresenter) : AccountContract.Interactor {
    override fun getUserData(listener :FirebaseDatabaseUtil.ValueListenerDataTrueFalse) {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.getUserByUid(UserPreference.getInstance().uid, listener)
            }

            override fun noInternet(message: String?) {
                mPresenter.onNoConnection(message)
            }
        })
    }

}
