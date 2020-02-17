package com.kanzankazu.itungitungan.view.main.ProfileSub

import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface AccountContract {
    interface View : BaseViewContract {
        fun setDataUser(user: User)
        fun setPhoneNumber(phoneNumber: String)

    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun showSnackbarPresenter(message: String)
        fun getUserData()
        fun isPhoneNumberExist(phoneNumber: String)
        fun onSuccessCheckPhone(phoneNumber: String)
    }

    interface Interactor {
        fun getUserData(listener : FirebaseDatabaseUtil.ValueListenerDataTrueFalse)
        fun checkPhoneNumber(phoneNumber: String)

    }
}
