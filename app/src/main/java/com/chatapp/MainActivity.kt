package com.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.account.login.LoginEmailActivity
import com.chatapp.account.login.LoginMainActivity
import com.chatapp.conversation.Chat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ****************** TOOLBAR ****************** \\
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val text= findViewById<TextView>(R.id.text)
        text.setOnClickListener{
            startActivity(Intent(applicationContext, Chat::class.java))
        }



        val text1= findViewById<TextView>(R.id.text1)
        text1.setOnClickListener{
            startActivity(Intent(applicationContext, LoginMainActivity::class.java))
        }

    }
}