package com.chatapp.models

import java.util.*

class ChatMessage(
    val messageId: String, val text: String, val userUid: String,
    val toUid: String, val time: String
)  {
constructor() : this("", "","", "", "")

}