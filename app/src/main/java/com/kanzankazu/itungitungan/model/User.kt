package com.kanzankazu.itungitungan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Faisal Bahri on 2019-11-18.
 */
@Parcelize
data class User(
        var key: String = "",
        var uId: String = "",
        var level: String = "",
        var tokenAccess: String = "",
        var tokenFcm: String = "",
        var token: String = "",
        var name: String = "",
        var email: String = "",
        var phone: String = "",
        var photoUrl: String = "",
        var photoDt: String = "",
        var emailVerified: Boolean = false,
        var signIn: Boolean = false,
        var phoneCode: String = "",
        var phoneDetail: String = "",
        var firstSignIn: String = "",
        var lastSignIn: String = "",
        var lastSignOut: String = "",
        var bySignIn: String = ""
) : Parcelable
