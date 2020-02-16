package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Donate(
        var id: String = "",
        var uid: String = "",
        var accoutNumber: String = "",
        var accoutNumberName: String = "",
        var accoutNumberImage: String = "",
        var type: String = "",
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable