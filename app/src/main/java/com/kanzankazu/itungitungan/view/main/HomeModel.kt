package com.kanzankazu.itungitungan.view.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeModel(
    var image: Int = 0,
    var title: String = "",
    var isShow: Boolean,
    var isComingSoon: Boolean
) : Parcelable
