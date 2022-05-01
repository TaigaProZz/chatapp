package com.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class NewConversationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation)

        // recycler view settings
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_newconversation)
        recyclerView.adapter


    }
}