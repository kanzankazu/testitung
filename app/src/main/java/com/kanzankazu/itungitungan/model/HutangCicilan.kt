package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HutangCicilan(
        var hIdSub: String = "",
        var hId: String = "",
        var paymentInstallmentTo: Int = 1,
        var paymentNominal: String = "",
        var paymentDesc: String = "",
        var photo1: String = "",
        var photo2: String = "",
        var hutangCicilanBuktiGambar: MutableList<ImageModel>? = null,
        var approvalCreditor: Boolean = false,
        var approvalDebtor: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = ""
) : Parcelable
