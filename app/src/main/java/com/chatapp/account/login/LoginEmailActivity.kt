package com.chatapp.account.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.chatapp.MainActivity
import com.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginEmailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

        auth = Firebase.auth

        
        val loginButton = findViewById<Button>(R.id.login_button).setOnClickListener {

            val username = findViewById<EditText>(R.id.username_login).text.toString()
            val password = findViewById<EditText>(R.id.password_login).text.toString()

            try {
                auth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()
                    }
                    else {

                        Toast.makeText(applicationContext, "Connexion échouée", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            catch (e: Exception){
                Toast.makeText(applicationContext, "Vide", Toast.LENGTH_SHORT).show()

            }

        }


        // back arrow
        findViewById<ImageView>(R.id.backArrow).setOnClickListener{
            startActivity(Intent(applicationContext, LoginMainActivity::class.java))
        }

    }

}