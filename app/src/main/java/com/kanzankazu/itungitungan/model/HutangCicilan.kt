package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HutangCicilan(
        var hSubId: String = "",
        var hId: String = "",
        var paymentTo: String = "",
        var paymentNominal: String = "",
        var paymentDesc: String = "",
        var photo1: String = "",
        var photo2: String = "",
        var approvalGiver: Boolean = false,
        var approvalReceiver: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = ""
) : Parcelable
