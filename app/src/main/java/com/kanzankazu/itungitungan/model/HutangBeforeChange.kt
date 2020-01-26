package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HutangBeforeChange(val hutang: Hutang) : Parcelable
