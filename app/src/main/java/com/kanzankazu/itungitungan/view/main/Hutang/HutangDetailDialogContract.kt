package com.kanzankazu.itungitungan.view.main.Hutang

import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface HutangDetailDialogContract {
    interface View : BaseViewContract {

    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun approveHutangNew(hutang: Hutang, isCancel: Boolean)
        fun approveHutangEdit(hutang: Hutang, isCancel: Boolean)
        fun requestHutangHapus(hutang: Hutang, isCancel: Boolean)
        fun approveHutangHapus(hutang: Hutang)
        fun approveHutangCicilanPay(hutang: Hutang)
        fun hapusHutangCheckImage(hutang: Hutang)
    }

    interface Interactor {

    }
}
