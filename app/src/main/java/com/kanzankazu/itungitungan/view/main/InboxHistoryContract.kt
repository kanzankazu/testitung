package com.kanzankazu.itungitungan.view.main

import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface InboxHistoryContract {
    interface View : BaseViewContract {
        fun toggleEmptyDataLayout(isVisible: Boolean)
        fun setInboxHistoryData(dataSnapshot: DataSnapshot?, isReload: Boolean)
        fun onListItemClick()
    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun getInboxHistoryData(isReload: Boolean)

    }

    interface Interactor {

    }
}
