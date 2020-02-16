package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Home(
        var image: Int = 0,
        var title: String = "",
        var isShow: Boolean = false,
        var isComingSoon: Boolean = false
) : Parcelable {
    //constructor(image: Int) : this(image, "", false, false)
}
