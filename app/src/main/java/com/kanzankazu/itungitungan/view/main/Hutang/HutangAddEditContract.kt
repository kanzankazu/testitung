package com.kanzankazu.itungitungan.view.main.Hutang

import android.net.Uri
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface HutangAddEditContract {
    interface View : BaseViewContract {
        fun validateFieldData(isFocus: Boolean): Boolean
        fun setCheckSuggestUsers(dataSnapshot: Any?)
        fun setSuggestUserFamily(dataSnapshot: DataSnapshot?)
        fun setHutangData(dataSnapshot: DataSnapshot?)
    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun getSuggestUserValidate(resultAccount: String, textView: TextView)
        fun getSuggestUserFamily()
        fun getRadioGroupIndex(radioGroup: RadioGroup): Int
        fun setRadioGroupIndex(rg_hutang_add_user: RadioGroup, index: Int): RadioButton
        fun saveImageHutang(hutang: Hutang, isEdit: Boolean, datasUri: MutableList<Uri>)
        fun saveEditHutang(hutang: Hutang, isEdit: Boolean, isNotSilent: Boolean)
        fun sendNotifAddEditHutang()
        fun getHutangByHid(hutangId: String?)
    }

    interface Interactor {

    }
}
