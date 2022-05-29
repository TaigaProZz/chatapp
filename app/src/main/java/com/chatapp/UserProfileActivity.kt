package com.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.chatapp.account.AccountMainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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
        refreshUsername()

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



        //  Open a pop up with EDIT TEXT to change the username of the user
        findViewById<ImageView>(R.id.change_username_btn).setOnClickListener {
            createAlertBoxChangeUsername()

        }

        /*          BUTTONS           */
        // sign out button
        findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            auth.signOut()
            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, AccountMainActivity::class.java))
        }

    }

    private fun createAlertBoxChangeUsername() {
        val alertDialog = AlertDialog.Builder(this)
        val userRef = db.getReference("/users/$uid/username")
        val dialogLayout = layoutInflater.inflate(R.layout.edit_text_alertbox, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.edit_text_change_username)

        with(alertDialog) {
            setTitle("Change Username")
            setPositiveButton("Confirm") { _, _ ->
                val username = editText.text.toString()
                userRef.setValue(username)
            }
            setNegativeButton("Cancel") { _, _ -> }
            setView(dialogLayout)
            show()
        }

    }

    private fun refreshUsername(){
        val userRef = db.getReference("/users/$uid/username")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usernameUser = findViewById<TextView>(R.id.username_user_profile)
                usernameUser.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    private fun getAvatarFromFirebase() {
        val avatarDisplay = findViewById<ImageView>(R.id.avatar_user_profile)

        db.getReference("users").child(uid!!).child("avatar")
            .get()
            .addOnSuccessListener {

                val imageFromFirebase = it.value
                Glide.with(applicationContext).load(imageFromFirebase).into(avatarDisplay)

            }
            .addOnFailureListener {
                Log.d("TAG", "Can't get image from firebase")
            }
    }

    private fun getUsernameFromFirebase() {
        db.getReference("users").child(uid!!).child("username")
            .get()
            .addOnSuccessListener {
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


