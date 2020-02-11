package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil

class HutangAddEditPresenter(private val mActivity: Activity, private val mView: HutangAddEditContract.View) : HutangAddEditContract.Presenter {
    override fun showProgressDialoPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        mView.showRetryDialogView()
    }

    override fun saveEditHutang(hutang: Hutang, isEdit: Boolean, isNotSilent: Boolean) {
        var inviteUid = ""
        if (hutang.hutangRadioIndex == 0) {
            if (hutang.creditorId.isNotEmpty()) {
                inviteUid = hutang.creditorId
            }
        } else {
            if (hutang.debtorId.isNotEmpty()) {
                inviteUid = hutang.debtorId
            }
        }

        showProgressDialoPresenter()
        if (!isEdit) {
            FirebaseDatabaseHandler.setHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {
                    if (isNotSilent) {
                        dismissProgressDialogPresenter()
                        mView.showSnackbarView(message)
                        mView.onSuccessSaveUpdate(hutang.hutangRadioIndex, inviteUid)
                        mActivity.finish()
                    }
                }

                override fun onFailureSaveUpdate(message: String?) {
                    if (isNotSilent) {
                        dismissProgressDialogPresenter()
                        mView.showSnackbarView(message)
                    }
                }
            })
        } else {
            FirebaseDatabaseHandler.updateHutang(mActivity, hutang, object : FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {
                override fun onSuccessSaveUpdate(message: String?) {
                    if (isNotSilent) {
                        dismissProgressDialogPresenter()
                        mView.showSnackbarView(message)
                        mView.onSuccessSaveUpdate(hutang.hutangRadioIndex, inviteUid)
                        mActivity.finish()
                    }
                }

                override fun onFailureSaveUpdate(message: String?) {
                    if (isNotSilent) {
                        dismissProgressDialogPresenter()
                    }
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
