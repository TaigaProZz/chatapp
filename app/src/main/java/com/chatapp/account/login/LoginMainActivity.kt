package com.chatapp.account.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.chatapp.R

class LoginMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)


        val loginEmailButton = findViewById<Button>(R.id.login_email_button).setOnClickListener {
            startActivity(Intent(applicationContext, LoginEmailActivity::class.java))
        }

    }
}