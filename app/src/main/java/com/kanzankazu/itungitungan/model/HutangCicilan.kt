package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HutangCicilan(
        var hIdSub: String = "",
        var hId: String = "",
        var paymentInstallmentTo: Int = 1,
        var paymentNominal: String = "0",
        var paymentDesc: String = "",
        var hutangCicilanBuktiGambar: MutableList<String> = mutableListOf(),
        var approvalCreditor: Boolean = false,
        var approvalDebtor: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = ""
) : Parcelable
