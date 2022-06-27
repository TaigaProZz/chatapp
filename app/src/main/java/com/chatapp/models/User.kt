package com.chatapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    var username: String = "",
    val email: String = "",
    val password: String = "",
    val uid: String = "",
    val avatar: String = "tbd"
) : Parcelable

