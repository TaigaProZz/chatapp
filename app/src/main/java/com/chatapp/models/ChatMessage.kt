package com.chatapp.models

class ChatMessage(val messageId: String, val text: String, val userUid: String,val toUid: String, val time: Long) {
constructor() : this("", "","", "", -1)
}