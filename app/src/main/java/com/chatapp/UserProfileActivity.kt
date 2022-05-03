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

    companion object {
        const val TAG = "TagUserProfileActivity"
    }

    private val auth = Firebase.auth
    private var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        // toolbar settings
        supportActionBar?.title = "Profil"

        // show user email
        val userEmail = findViewById<TextView>(R.id.email_user_profile)
        val email = auth.currentUser?.email
        userEmail.text = email

        // show user username
        db.collection("users")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val usernameUser = findViewById<TextView>(R.id.username_user_profile)
                    // collect username from Firebase and display it
                    val usernameFromDatabase = document.data["username"].toString()
                    usernameUser.text = usernameFromDatabase
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Can't get username from firebase")
            }

        // TODO change the username of the user
        // findViewById<ImageView>(R.id.save_username).setOnClickListener {
        //     userRef.update("username", usernameUser)
        // }


        // show user's avatar
        val avatarDisplay = findViewById<ImageView>(R.id.avatar_user_profile)
        db.collection("users")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val imageFromFirebase = document.data["avatar"]
                    Glide.with(this).load(imageFromFirebase).into(avatarDisplay)
                    Log.d("TAG", "Image displayed on user profile: $imageFromFirebase")

                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Can't get image from firebase")
            }


        /*          BUTTONS           */
        // sign out button
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            auth.signOut()
            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }
    }
}


