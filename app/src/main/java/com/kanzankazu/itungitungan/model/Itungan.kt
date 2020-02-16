package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Itungan(
        var uId: String = "",
        var gId: String = "",
        var itId: String = "",
        var catId: String = "",
        var catDesc: String = "",
        var total: String = "",
        var desc: String = "",
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
