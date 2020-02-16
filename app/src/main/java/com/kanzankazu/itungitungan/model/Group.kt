package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Faisal Bahri on 2019-11-22.
 */
@Parcelize
data class Group(
        var grId: String = "",
        var grName: String = "",
        var users: List<User> = mutableListOf(),
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
