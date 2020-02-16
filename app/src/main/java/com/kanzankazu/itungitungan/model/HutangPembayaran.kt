package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HutangPembayaran(
        var hIdSub: String = "",
        var hId: String = "",
        var paymentTo: Int = 0,
        var paymentProofImage: MutableList<String> = mutableListOf(),
        var paymentNominal: String = "0",
        var paymentDesc: String = "",
        var approvalCreditor: Boolean = false,
        var approvalDebtor: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
