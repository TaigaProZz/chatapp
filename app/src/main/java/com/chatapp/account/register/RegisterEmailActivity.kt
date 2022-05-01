package com.chatapp.account.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.R
import com.chatapp.account.AccountMainActivity
import com.chatapp.account.avatar.AvatarChoiceActivity
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterEmailActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    companion object {
        const val TAG = "TagRegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // TODO create acc with google on this activity
        findViewById<SignInButton>(R.id.google_button).setOnClickListener {
        }

        // create an account when register button is pressed
        findViewById<Button>(R.id.register_button).setOnClickListener {
            createAccountWithEmail()
        }


        // go back arrow
        findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }
    }


    // create account with firebase with email & password
    private fun createAccountWithEmail() {


        // collect all inputs of the user
        val getUsername = findViewById<EditText>(R.id.username_register)
        val getEmail = findViewById<EditText>(R.id.email_register)
        val getPassword = findViewById<EditText>(R.id.password_register)

        val username = getUsername.text.toString()
        val email = getEmail.text.toString()
        val password = getPassword.text.toString()

        // check if fields arent empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Vide", Toast.LENGTH_SHORT).show()
            return
        }

        // create the account with firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val user = User(username, email, password, uid!!)

                    // add users data to the database
                    val userRef = db.collection("users").document(uid)
                    userRef.set(user)
                    Toast.makeText(applicationContext, "Inscription réussie", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(applicationContext, AvatarChoiceActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Inscription failed. ", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}


class User(
    val username: String,
    val email: String?,
    val password: String,
    val uid: String,
    val avatar: String = "tbd"
)
