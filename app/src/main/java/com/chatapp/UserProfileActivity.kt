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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TagUserProfileActivity"
    }

    private val auth = Firebase.auth
    private var db =
        Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
    private val uid = auth.uid


    override fun onStart() {
        super.onStart()

        // call functions
        getUsernameFromFirebase()
        getAvatarFromFirebase()

        // show user email
        val userEmail = findViewById<TextView>(R.id.email_user_profile)
        val email = auth.currentUser?.email
        userEmail.text = email
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        // toolbar settings
        supportActionBar?.title = "Profil"


        // TODO
        //  Open a pop up with EDIT TEXT to change the username of the user
        // findViewById<ImageView>(R.id.save_username).setOnClickListener {
        //     userRef.update("username", usernameUser)
        // }


        /*          BUTTONS           */
        // sign out button
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            auth.signOut()
            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }
    }

    private fun getAvatarFromFirebase() {
        val avatarDisplay = findViewById<ImageView>(R.id.avatar_user_profile)

        db.getReference("users").child(uid!!).child("avatar")
            .get()
            .addOnSuccessListener {

                val imageFromFirebase = it.value
                Glide.with(applicationContext).load(imageFromFirebase).into(avatarDisplay)
                Log.d("TAG", "Image displayed on user profile: $imageFromFirebase")

            }
            .addOnFailureListener {
                Log.d("TAG", "Can't get image from firebase")
            }
    }

    private fun getUsernameFromFirebase() {
        db.getReference("users").child(uid!!).child("username")
            .get()
            .addOnSuccessListener {
                Log.d(TAG, "display ${it.value}")

                val usernameUser = findViewById<TextView>(R.id.username_user_profile)
                // collect username from Firebase and display it
                val usernameFromDatabase = it.value.toString()
                usernameUser.text = usernameFromDatabase

            }
            .addOnFailureListener {
                Log.d("TAG", "Can't get username from firebase")
            }
    }

}


