package com.kanzankazu.itungitungan.view.main

import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface DashboardFragmentContract {
    interface View :BaseViewContract{

    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun getHutang(listener: FirebaseDatabaseUtil.ValueListenerData)

    }

    interface Interactor {

    }
}
