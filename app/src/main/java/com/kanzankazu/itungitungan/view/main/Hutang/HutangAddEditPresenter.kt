package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class HutangAddEditPresenter(private val mActivity: Activity, private val mView: HutangAddEditContract.View) : HutangAddEditContract.Presenter {
    override fun showProgressDialoPresenter() {}

    override fun dismissProgressDialogPresenter() {}

    override fun onNoConnection(message: String?) {}

    override fun saveEditHutang(hutang: Hutang, isEdit: Boolean) {
        mView.showProgressDialogView()
        if (!isEdit) {
            FirebaseDatabaseHandler.setHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {
                    mView.dismissProgressDialogView()
                    mView.showSnackbarView(message)
                    mActivity.finish()
                }

                override fun onFailureSaveUpdate(message: String?) {
                    mView.dismissProgressDialogView()
                    mView.showSnackbarView(message)
                }
            })
        } else {
            FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {
                    mView.dismissProgressDialogView()
                    mView.showSnackbarView(message)
                    mActivity.finish()
                }

                override fun onFailureSaveUpdate(message: String?) {
                    mView.dismissProgressDialogView()
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
