package com.kanzankazu.itungitungan.view.main.Hutang

import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface HutangPayContract {
    interface View : BaseViewContract {
        fun checkData(isFocus: Boolean): Boolean
        fun setSuggestNote(list: ArrayList<String>)
        fun onSuggestItemClick(s: String)
    }

    interface Presenter {
        fun getSuggestNote()
        fun getSuggestNoteManual(): ArrayList<String>

        fun saveData(hutang: Hutang)
        fun deleteImage()
    }

}
