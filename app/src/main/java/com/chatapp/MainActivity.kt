package com.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.account.AccountMainActivity
import com.chatapp.conversation.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onStart() {
        super.onStart()

        val auth= Firebase.auth
        val currentUser = auth.currentUser
        // if the user is not connected, force login activity
        if (currentUser == null){
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth= Firebase.auth
        auth.currentUser

        // show the username of the user
        val username = findViewById<TextView>(R.id.username_connected)
        val user = auth.currentUser?.displayName
        username.text = user



        /*             BUTTONS           */

        // sign out button
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            auth.signOut()

            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }


        //               TOOLBAR             \\
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        val text= findViewById<TextView>(R.id.text)
        text.setOnClickListener{
            startActivity(Intent(applicationContext, Chat::class.java))
        }

        val text1= findViewById<TextView>(R.id.text1)
        text1.setOnClickListener{
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }

    }
}