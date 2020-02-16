package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Anggaran(
        var uId: String = "",
        var grId: String = "",
        var angId: String = "",
        var catId: String = "",
        var catDesc: String = "",
        var nominal: String = "",
        var desc: String = "",
        var isMonthly: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
