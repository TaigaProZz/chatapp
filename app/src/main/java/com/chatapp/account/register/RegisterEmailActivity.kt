package com.chatapp.account.register

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.chatapp.MainActivity
import com.chatapp.R
import com.chatapp.account.login.AccountMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class RegisterEmailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        auth = Firebase.auth

        val getUsername = findViewById<EditText>(R.id.username_register)
        val getPassword = findViewById<EditText>(R.id.password_register)
        println("test1 $getUsername $getPassword")

        val registerButton = findViewById<Button>(R.id.register_button).setOnClickListener {



            val username = getUsername.text.toString()
            val password = getPassword.text.toString()

            try {

                println("test12 $username $password")


                auth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener {
                    if (it.isSuccessful){
                        val user = auth.currentUser
                        updateUI(user)
                        Toast.makeText(applicationContext, "Inscription réussie", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                    else {

                        Toast.makeText(applicationContext, "Inscription failed.", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
            }

            catch (e: Exception){
                Toast.makeText(applicationContext, "Vide", Toast.LENGTH_SHORT).show()

            }

        }

        // goto
        findViewById<ImageView>(R.id.backArrow).setOnClickListener{
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }

        // back arrow
        findViewById<ImageView>(R.id.backArrow).setOnClickListener{
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }



    }

    private fun updateUI(user: FirebaseUser?) {

    }
}