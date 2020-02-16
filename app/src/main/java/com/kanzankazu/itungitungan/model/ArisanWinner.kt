package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArisanWinner(
        var arisanWinnerId: String = "",
        var arisanWinnerPeriod: String = "",
        var arisanWinnerUid: String = "",
        var arisanWinnerName: String = "",
        var arisanWinnerTotal: String = "",
        var arisanWinnerDesc: String = "",
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
