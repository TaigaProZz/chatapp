package com.chatapp.models

class ChatMessage(
    val messageId: String, val text: String, val userUid: String,
    val toUid: String, val time: String, val seen: Boolean
)  {
constructor() : this("", "","", "", "", false)

}