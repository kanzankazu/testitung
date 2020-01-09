package com.kanzankazu.itungitungan.view.main.Hutang

import com.kanzankazu.itungitungan.model.Hutang
import java.util.ArrayList

interface HutangListContract {
    interface View {
        fun onHutangUbahClick(hutang: Hutang)
        fun onHutangBayarClick(hutang: Hutang)
        fun onHutangHapusClick(hutang: Hutang, position: Int)
        fun onHutangFilter(hutangs: MutableList<Hutang>)

    }

    interface Presenter {

    }

    interface Interactor {

    }

}
