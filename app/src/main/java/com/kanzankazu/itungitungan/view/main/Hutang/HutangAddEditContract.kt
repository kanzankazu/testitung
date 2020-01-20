package com.kanzankazu.itungitungan.view.main.Hutang

import android.widget.RadioButton
import android.widget.RadioGroup
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface HutangAddEditContract {
    interface View : BaseViewContract {
    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun saveEditHutang(hutang: Hutang, isEdit: Boolean)
        fun getRadioGroupIndex(rgHutangAddUser: RadioGroup): Int
        fun setRadioGroupIndex(rg_hutang_add_user: RadioGroup, index: Int): RadioButton
    }

    interface Interactor {

    }
}
