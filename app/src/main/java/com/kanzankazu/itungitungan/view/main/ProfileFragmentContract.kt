package com.kanzankazu.itungitungan.view.main

interface ProfileFragmentContract {
    interface View {
        fun setProfileOptionList()
        fun itemAdapterClick(data: ProfileAccountModel)
    }

    interface Presenter
}
