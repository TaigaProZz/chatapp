package com.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.chatapp.account.AccountMainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        supportActionBar?.title = "Profil"


        // show users' email / name
       val username = findViewById<TextView>(R.id.username_user_profile)
       val user = auth.currentUser?.email
       username.text = user

        // show user's avatar
        val avatarDisplay = findViewById<ImageView>(R.id.avatar_user_profile)
        db.collection("users")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val imageFromFirebase = document.data["profileImage"]
                    Glide.with(this).load(imageFromFirebase).into(avatarDisplay)
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "fail")

            }


        // sign out button
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            auth.signOut()

            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }


    }
}