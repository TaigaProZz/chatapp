package com.chatapp.conversation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chatapp.MainActivity
import com.chatapp.R

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java) )
        }



        // val recyclerView= findViewById<RecyclerView>(R.id.recyclerViewChat_xml)


    }
}