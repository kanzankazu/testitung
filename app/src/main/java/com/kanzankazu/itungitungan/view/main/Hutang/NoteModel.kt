package com.kanzankazu.itungitungan.view.main.Hutang

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteModel(
        var nId: String = "",
        var title: String = "",
        var uId: String = ""
) : Parcelable {

}
