package com.kanzankazu.itungitungan.view.main

import com.kanzankazu.itungitungan.model.Home

interface HomeContract {
    interface View {
        fun itemAdapterClick(position: Home)
    }

}
