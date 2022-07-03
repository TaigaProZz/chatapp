package com.chatapp.account.register

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.R
import com.chatapp.account.AccountMainActivity
import com.chatapp.models.User
import com.chatapp.account.avatar.AvatarChoiceActivity
import com.chatapp.account.login.LoginEmailActivity
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class RegisterEmailActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private val db = Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")

    companion object {
        const val TAG = "TagRegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        // goto login activity button
        findViewById<TextView>(R.id.goto_login_button).setOnClickListener {
            startActivity(Intent(applicationContext, LoginEmailActivity::class.java))
        }

        // create an account when register button is pressed
        findViewById<Button>(R.id.register_button).setOnClickListener {
            createAccountWithEmail()
        }

    }


    // create account with firebase with email & password
    private fun createAccountWithEmail() {
        // collect all inputs of the user
        val getUsername = findViewById<EditText>(R.id.username_register)
        val getEmail = findViewById<EditText>(R.id.email_register)
        val getPassword = findViewById<EditText>(R.id.password_register)

        val username = getUsername.text.toString().lowercase()
        val email = getEmail.text.toString()
        val password = getPassword.text.toString()

        // check if fields arent empty
        if (username.isEmpty() || username.contains(" ") || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "Vide", Toast.LENGTH_SHORT).show()
            return
        }

        // create the account with firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    saveToDatabase()
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


    // DO NOT SAVE PASSWORD IF RELEASED
    private fun saveToDatabase() {
        // collect all inputs of the user
        val getUsername = findViewById<EditText>(R.id.username_register)
        val getName = findViewById<EditText>(R.id.name_register)
        val getEmail = findViewById<EditText>(R.id.email_register)

        // never save password if public access
        val getPassword = findViewById<EditText>(R.id.password_register)

        val username = getUsername.text.toString().lowercase()
        val name = getName.text.toString()
        val email = getEmail.text.toString()

        // never save password if public access
        val password = getPassword.text.toString()

        val uid = auth.currentUser?.uid
        val user = User(username, name, email, password, uid!!)

        // add users data to the database
        val userRef = db.getReference("/users/$uid")
        userRef.setValue(user)
    }
}


