package com.chatapp.account.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.R
import com.chatapp.account.register.RegisterEmailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginEmailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

        auth = Firebase.auth

        // Login Button
        findViewById<Button>(R.id.login_button).setOnClickListener {


        }




        // goto register activity button
        findViewById<Button>(R.id.goto_register_button).setOnClickListener {
            startActivity(Intent(applicationContext, RegisterEmailActivity::class.java))
        }


        // back arrow
        findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }

    }

    private fun loginWithEmail(){

        val getUsername = findViewById<EditText>(R.id.username_login)
        val getPassword = findViewById<EditText>(R.id.password_login)
        val username = getUsername.text.toString()
        val password = getPassword.text.toString()

        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Vide", Toast.LENGTH_SHORT).show()
            return
        }

        // login to account with firebase
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Connexion réussie $username $password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                Toast.makeText(applicationContext, "Connexion échouée", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}