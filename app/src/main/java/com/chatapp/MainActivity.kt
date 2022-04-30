package com.chatapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toIcon
import com.bumptech.glide.Glide
import com.chatapp.account.AccountMainActivity
import com.chatapp.conversation.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    companion object {
        const val TAG = "AvatarChoice"
    }

    override fun onStart() {
        super.onStart()

        checkIfUserIsConnected()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = Firebase.auth
        val uid = auth.uid
        Log.d("MainActivityAvatar", "uid = $uid")


        // show users' email / name
        val username = findViewById<TextView>(R.id.username_connected)
        val user = auth.currentUser?.email
        username.text = user

        // show user's avatar
        val avatarDisplay = findViewById<ImageView>(R.id.user_avatar)
        db.collection("users")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val imageFromFirebase =  document.data["profileImage"]
                    Glide.with(this).load(imageFromFirebase).into(avatarDisplay)
                }
            }
            .addOnFailureListener {
                Log.d("MainActivityAvatar", "fail")

            }

        /*             BUTTONS           */

        // sign out button
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            auth.signOut()

            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }

        val text = findViewById<TextView>(R.id.text)
        text.setOnClickListener {
            startActivity(Intent(applicationContext, Chat::class.java))
        }

        val text1 = findViewById<TextView>(R.id.text1)
        text1.setOnClickListener {
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }


        //               TOOLBAR             \\
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    private fun checkIfUserIsConnected(){
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        // if the user is not connected, force login activity
        if (currentUser == null) {
            val intent = Intent(applicationContext, AccountMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

}