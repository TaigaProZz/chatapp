package com.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java) )
        }
    }
}