package com.chatapp.account

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val uid: String = "",
    val avatar: String = "tbd"
) : Parcelable

