package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InboxHistory(
        var inboxId: String = "",
        var inboxSenderUId: String = "",
        var inboxReceiverUId: String = "",
        var inboxTitle: String = "",
        var inboxMessage: String = "",
        var inboxTypeNotif: String = "",//Feature (Hutang)
        var inboxTypeView: String = "", //Single / chat
        var inboxIsRead: Boolean = false,
        var createAt: String = "",
        var createBy: String = "",
        var updateAt: String = "",
        var updateBy: String = "",
        var deleteAt: String = "",
        var deleteBy: String = ""
) : Parcelable
