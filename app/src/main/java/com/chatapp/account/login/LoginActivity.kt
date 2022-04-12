package com.chatapp.account.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.chatapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        
        val loginButton = findViewById<Button>(R.id.login_button).setOnClickListener {

            val username = findViewById<EditText>(R.id.username_login).text
            val password = findViewById<EditText>(R.id.password_login).text


        }

    }

}