package com.kanzankazu.itungitungan.view.main.Hutang

import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface HutangListContract {
    interface View : BaseViewContract {
        fun onHutangUbahClick(hutang: Hutang)
        fun onHutangBayarClick(hutang: Hutang)
        fun onHutangHapusClick(hutang: Hutang, position: Int)
        fun onHutangFilter(hutangs: MutableList<Hutang>)
        fun onAgreementApproveClick(hutang: Hutang)
        fun onHutangLihatClick(hutang: Hutang)

    }

    interface Presenter {

    }

    interface Interactor {

    }

}
