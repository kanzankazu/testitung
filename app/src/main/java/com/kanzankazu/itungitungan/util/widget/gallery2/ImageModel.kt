package com.kanzankazu.itungitungan.util.widget.gallery2

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageModel(
        var path: String? = "",
        var type: String? = ""
) : Parcelable