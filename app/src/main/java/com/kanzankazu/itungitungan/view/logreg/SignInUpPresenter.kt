package com.kanzankazu.itungitungan.view.logreg

import android.app.Activity
import com.google.firebase.iid.FirebaseInstanceId
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.DeviceDetailUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Utils

class SignInUpPresenter(var mActivity: Activity, var mView: SignInUpContract.View): SignInUpContract.Presenter {
    override fun showProgressDialoPresenter() {mView.showProgressDialog()}

    override fun dismissProgressDialogPresenter() {mView.dismissProgressDialog()}

    override fun onNoConnection(message: String?) {
        mView.showRetryDialog {  }
    }

    override fun signInUpControl(isSignUp: Boolean, isFirst: Boolean, user: User, name: String, listener: FirebaseDatabaseUtil.ValueListenerString) {
        mView.showProgressDialog()

        if (!UserPreference.getInstance().fcmToken.isNullOrEmpty()) {
            user.tokenFcm = UserPreference.getInstance().fcmToken
        } else {
            UserPreference.getInstance().fcmToken = user.tokenFcm
            user.tokenFcm = FirebaseInstanceId.getInstance().token.toString()
        }

        if (isSignUp) {
            user.name = name
            user.signIn = false
        } else {
            user.signIn = true
            user.phoneDetail = DeviceDetailUtil.getAllDataPhone(mActivity)
        }

        if (isFirst) {
            user.firstSignIn = DateTimeUtil.getCurrentDate().toString()
            user.phoneCode = Utils.getUniquePsuedoID()
        }

        user.lastSignIn = DateTimeUtil.getCurrentDate().toString()
        FirebaseDatabaseHandler.setUser(mActivity, user, listener)
    }
}
