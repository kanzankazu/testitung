package com.kanzankazu.itungitungan.view.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Faisal Bahri on 2019-12-05.
 */
@Parcelize
data class ProfileAccountModel(
        var icon: Int = 0,
        var title: String = "",
        var desc: String = "",
        var type: String = "",
        var isShow: Boolean
) : Parcelable