package com.chatapp.account.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.MainActivity
import com.chatapp.R
import com.chatapp.account.AccountMainActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterEmailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth


        val googleButton = findViewById<SignInButton>(R.id.google_button).setOnClickListener {

        }

        // create an account when register button is pressed
        val registerButton = findViewById<Button>(R.id.register_button).setOnClickListener {
            createAccountWithEmail()
        }


        // goto
        findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }

        // back arrow
        findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }
    }




    // create account with firebase with email
    private fun createAccountWithEmail() {

        val getUsername = findViewById<EditText>(R.id.username_register)
        val getPassword = findViewById<EditText>(R.id.password_register)
        val username = getUsername.text.toString()
        val password = getPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Vide", Toast.LENGTH_SHORT).show()
            return
        }


        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Inscription réussie", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener {

                Toast.makeText(applicationContext, "Inscription failed.", Toast.LENGTH_SHORT)
                    .show()
            }
    }



}