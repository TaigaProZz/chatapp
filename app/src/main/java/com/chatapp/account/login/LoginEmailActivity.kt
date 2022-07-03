package com.chatapp.account.login

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.mainActivity.MainActivity
import com.chatapp.R
import com.chatapp.account.register.RegisterEmailActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginEmailActivity : AppCompatActivity() {

    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)


        /*          BUTTONS           */
        // Login with email Button
        findViewById<Button>(R.id.login_button).setOnClickListener {
            loginWithEmail()
        }

        // goto register activity button
        findViewById<TextView>(R.id.goto_register_button).setOnClickListener {
            startActivity(Intent(applicationContext, RegisterEmailActivity::class.java))
        }
        

    }

    // login to Firebase with email & password
    private fun loginWithEmail() {

        // collect all inputs of the user
        val getUsername = findViewById<EditText>(R.id.username_login)
        val getPassword = findViewById<EditText>(R.id.password_login)
        val username = getUsername.text.toString()
        val password = getPassword.text.toString()

        // check if fields arent empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Vide", Toast.LENGTH_SHORT).show()
            return
        }

        // login to account with firebase
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(applicationContext, "Connexion réussie $username",Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Connexion échouée", Toast.LENGTH_SHORT).show()
            }
        }
    }
}