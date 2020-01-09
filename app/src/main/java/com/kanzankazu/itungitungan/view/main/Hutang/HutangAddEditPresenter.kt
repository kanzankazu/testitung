package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import retrofit2.Call
import java.lang.Exception

class HutangAddEditPresenter(private val mActivity: Activity, private val mView: HutangAddEditContract.View) : HutangAddEditContract.Presenter {
    override fun showProgressDialoPresenter() {}

    override fun dismissProgressDialogPresenter() {}

    override fun onNoConnection(call: Call<*>?) {}

    override fun saveEditHutang(hutang: Hutang, databaseUtil: FirebaseDatabaseUtil, isEdit: Boolean) {
        mView.dismissProgressDialogView()

        if (!isEdit) {
            Hutang.setHutang(databaseUtil.rootRef, mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerString {
                override fun onSuccess(message: String?) {
                    mView.dismissProgressDialogView()
                    mView.showSnackbarView(message)
                    mActivity.finish()
                }

                override fun onFailure(message: String?) {
                    mView.dismissProgressDialogView()
                    mView.showSnackbarView(message)
                }
            })
        } else {
            Hutang.updateHutang(databaseUtil.rootRef, mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerString {
                override fun onSuccess(message: String?) {
                    mView.dismissProgressDialogView()
                    mView.showSnackbarView(message)
                    mActivity.finish()
                }

                override fun onFailure(message: String?) {
                    mView.dismissProgressDialogView()
                    mView.showSnackbarView(message)
                }
            })
        }

    }

    override fun setRadioGroupIndex(rg_hutang_add_user: RadioGroup, index: Int): RadioButton {
        return (rg_hutang_add_user.getChildAt(index) as RadioButton)
    }

    override fun getRadioGroupIndex(radioGroup: RadioGroup): Int {
        val radioButtonID = radioGroup.checkedRadioButtonId
        val view = radioGroup.findViewById<View>(radioButtonID)
        val index: Int = radioGroup.indexOfChild(view)
        val radioButton = radioGroup.getChildAt(index) as RadioButton
        radioButton.text.toString()
        return index
    }
}
