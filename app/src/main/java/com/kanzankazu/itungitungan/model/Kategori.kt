package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Faisal Bahri on 2019-11-22.
 */
@Parcelize
data class Kategori(
        var uId: String = "",
        var catId: String = "",
        var catDesc: String = "",
        var isPrivate: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
