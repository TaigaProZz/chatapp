package com.chatapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserFriend(
    val username: String = "",
    val uid: String = ""
) : Parcelable