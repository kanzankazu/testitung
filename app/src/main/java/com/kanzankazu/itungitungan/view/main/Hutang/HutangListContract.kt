package com.kanzankazu.itungitungan.view.main.Hutang

import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract
import java.util.*

interface HutangListContract {
    interface View : BaseViewContract {
        fun onHutangUbahClick(hutang: Hutang)
        fun onHutangBayarClick(hutang: Hutang)
        fun onHutangHapusClick(hutang: Hutang, position: Int, isHasReqDelete: Boolean)
        fun onHutangFilter(hutangs: MutableList<Hutang>)
        fun onHutangLihatClick(hutang: Hutang)
        fun onHutangApproveNewClick(hutang: Hutang)
        fun onHutangApproveEditClick(hutang: Hutang)
        fun onHutangApproveDeleteClick(hutang: Hutang)
        fun onHutangApprovePayClick(hutang: Hutang)
        fun setAllHutangsMine(hutangs: ArrayList<Hutang>)
        fun setAllHutangsFamily(hutangs: ArrayList<Hutang>)
        fun setAllHutangsLunas(hutangs: ArrayList<Hutang>)
        fun setTotalPiuHutang(hutang: Hutang)
        fun setZeroHutangs()

    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun getAllHutang()
        fun hapusHutangCheckImage(hutang: Hutang)
        fun approveHutangNew(hutang: Hutang, isCancel: Boolean)
        fun approveHutangEdit(hutang: Hutang, isCancel: Boolean)
        fun requestHutangHapus(hutang: Hutang, isCancel: Boolean)
        fun approveHutangHapus(hutang: Hutang)
        fun approveHutangCicilanPay(hutang: Hutang)
    }

    interface Interactor {
        fun updateHutang(hutang: Hutang, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
    }

}
