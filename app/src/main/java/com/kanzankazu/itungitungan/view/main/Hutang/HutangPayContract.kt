package com.kanzankazu.itungitungan.view.main.Hutang

interface HutangPayContract {
    interface View {

        fun checkData(isFocus: Boolean): Boolean
        fun setSuggestNote()
    }

    interface Presenter {
        fun getSuggestNote()

    }

}
