package com.kanzankazu.itungitungan.view.main.ProfileSub

import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface AccountContract {
    interface View : BaseViewContract {
        fun setDataUser(user: User)

    }

    interface Presenter : BasePresenterContractWOCheckMessage {

    }

    interface Interactor {
        fun getUserData(listener : FirebaseDatabaseUtil.ValueListenerDataTrueFalse)

    }
}
