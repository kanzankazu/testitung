package com.kanzankazu.itungitungan.view.main.Hutang

import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract
import java.util.ArrayList

interface HutangListContract {
    interface View : BaseViewContract {
        fun onHutangUbahClick(hutang: Hutang)
        fun onHutangBayarClick(hutang: Hutang)
        fun onHutangHapusClick(hutang: Hutang, position: Int)
        fun onHutangFilter(hutangs: MutableList<Hutang>)
        fun onHutangLihatClick(hutang: Hutang)
        fun onHutangApproveNewClick(hutang: Hutang)
        fun onHutangApproveEditClick(hutang: Hutang)
        fun onHutangApproveDeleteClick(hutang: Hutang)
        fun setAllHutangs(hutangs: ArrayList<Hutang>)
        fun setTotalPiuHutang(hutang: Hutang)
        fun setZeroHutangs()

    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun getAllHutang()
        fun hapusHutang(hutang: Hutang)
        fun approveHutangNew(hutang: Hutang)
        fun approveHutangEdit(hutang: Hutang)
        fun requestHutangHapus(hutang: Hutang, isCancel: Boolean)
        fun approveHutangHapus(hutang: Hutang)
    }

    interface Interactor {

    }

}
