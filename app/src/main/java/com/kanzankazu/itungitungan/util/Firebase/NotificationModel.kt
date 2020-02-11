package com.kanzankazu.itungitungan.util.Firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Faisal Bahri on 2020-02-11.
 */
@Parcelize
data class NotificationModel(
    var TITLE: String = "",
    var MESSAGE: String = "",
    var TYPE: String = ""
) : Parcelable

