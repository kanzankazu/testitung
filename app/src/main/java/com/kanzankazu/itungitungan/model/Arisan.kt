package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Arisan(
        var arisanId: String = "",
        var arisanName: String = "",
        var arisanDesc: String = "",
        var arisanType: String = "",//(uang atau barang)
        var arisanNominal: String = "",
        var arisanWinner: MutableList<ArisanWinner> = mutableListOf(),
        var arisanUsersUid: MutableList<ArisanUser> = mutableListOf(),
        var arisanStatus: String = "",
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
