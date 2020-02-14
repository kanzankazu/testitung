package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class AccountPresenter(var activity: Activity, var mView: AccountContract.View):AccountContract.Presenter {
    var mInteractor = AccountInteractor(activity,this)

    override fun showProgressDialogPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        mView.showRetryDialogView()
    }

    fun getUserData() {
        mInteractor.getUserData(object : FirebaseDatabaseUtil. ValueListenerDataTrueFalse {
            override fun onSuccessDataExist(dataSnapshot: DataSnapshot?, isExsist: Boolean?) {
                if (isExsist!!){
                    val user = dataSnapshot!!.getValue(User::class.java)
                    mView.setDataUser(user!!)
                }
            }

            override fun onFailureDataExist(message: String?) {
                mView.showSnackbarView(message)
            }
        })
    }

}
