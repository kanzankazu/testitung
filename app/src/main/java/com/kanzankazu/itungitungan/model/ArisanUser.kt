package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArisanUser(
        var arisanUserid: String = "",
        var arisanUseruid: String = "",
        var arisanUserName: String = "",
        var arisanUserEmail: String = "",
        var arisanUserPhone: String = "",
        var arisanUserIsPay: Boolean = false,
        var arisanUserIsWinner: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
