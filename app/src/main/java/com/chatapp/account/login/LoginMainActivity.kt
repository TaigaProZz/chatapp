package com.chatapp.account.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.chatapp.MainActivity
import com.chatapp.R

class LoginMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)


        // back arrow
        findViewById<ImageView>(R.id.backArrow).setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        findViewById<Button>(R.id.login_email_button).setOnClickListener {
            startActivity(Intent(applicationContext, LoginEmailActivity::class.java))
        }

    }
}