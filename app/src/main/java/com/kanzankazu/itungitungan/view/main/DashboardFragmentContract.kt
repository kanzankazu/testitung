package com.kanzankazu.itungitungan.view.main

import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface DashboardFragmentContract {
    interface View :BaseViewContract{
        fun setHutangData(dataSnapshot: DataSnapshot)
        fun showHideHutangProgressView(isShow: Boolean)

    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun getAllStatusData()
        fun getHutang()
        fun getKeuangan()
    }

    interface Interactor {

    }
}
