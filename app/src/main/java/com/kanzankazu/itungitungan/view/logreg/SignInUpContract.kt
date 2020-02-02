package com.kanzankazu.itungitungan.view.logreg

import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseView

interface SignInUpContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun signInUpControl(isSignUp: Boolean, isFirst: Boolean, user: User, name: String, listener: FirebaseDatabaseUtil.ValueListenerString)

    }

    interface SignUp {
    }

}
