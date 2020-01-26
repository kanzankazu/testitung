package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hutang(
        var hId: String = "",
        var creditorId: String = "",
        var creditorName: String = "",
        var creditorFamilyId: String = "",
        var creditorFamilyName: String = "",
        var creditorEmail: String = "",
        var creditorApprovalNew: Boolean = false,
        var creditorApprovalEdit: Boolean = false,
        var creditorApprovalDelete: Boolean = false,
        var debtorId: String = "",
        var debtorName: String = "",
        var debtorFamilyId: String = "",
        var debtorFamilyName: String = "",
        var debtorEmail: String = "",
        var debtorApprovalNew: Boolean = false,
        var debtorApprovalEdit: Boolean = false,
        var debtorApprovalDelete: Boolean = false,
        var debtorCreditorId: String = "",
        var hutangRadioIndex: Int = 0,
        var hutangKeluargaId: String = "",
        var hutangKeluargaNama: String = "",
        var hutangNominal: String = "0",
        var hutangKeperluan: String = "",
        var hutangCatatan: String = "",
        var hutangPinjam: String = "",
        var hutangBuktiGambar: MutableList<String>? = null,
        var hutangEditableis: Boolean = false,
        var hutangCicilanIs: Boolean = false,
        var hutangCicilanBerapaKali: String = "",
        var hutangCicilanBerapaKaliType: String = "",
        var hutangCicilanBerapaKaliPosisi: Int = 0,
        var hutangCicilanNominal: String = "0",
        var hutangCicilanIsBayarKapanSaja: Boolean = true,
        var hutangCicilanTanggalAkhir: String = "",
        var hutangCicilanSub: MutableList<HutangCicilan> = mutableListOf(),
        var statusLunas: Boolean = false,
        var statusDeleted: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = ""
) : Parcelable
