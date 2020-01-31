package com.kanzankazu.itungitungan.view.main

import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import retrofit2.Call

class DashboardFragmentPresenter(var mActivity: Activity,var mView: DashboardFragmentContract.View): DashboardFragmentContract.Presenter {
    override fun showProgressDialoPresenter() {}

    override fun dismissProgressDialogPresenter() {}

    override fun onNoConnection(call: Call<*>?) {}

    override fun getHutang(listener: FirebaseDatabaseUtil.ValueListenerData) {
        mView.showProgressDialogView()
        FirebaseDatabaseHandler.getHutangs(false,object :FirebaseDatabaseUtil.ValueListenerData{
            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                mView.dismissProgressDialogView()
            }

            override fun onFailure(message: String?) {
                mView.dismissProgressDialogView()
            }
        })
    }

}
