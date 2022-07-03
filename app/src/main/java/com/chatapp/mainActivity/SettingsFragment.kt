package com.chatapp.mainActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.UserProfileActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private val db = MainActivity.db
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val uid = auth.uid

        getAvatar(view, uid)
        refreshUsername(view, uid)
        getUsername(view, uid)


        // goto profile edit
        view.findViewById<TextView>(R.id.settings_fragment_edit_profile).setOnClickListener {
            startActivity(Intent(view.context, UserProfileActivity::class.java))
        }


        // Inflate the layout for this fragment
        return view

    }


    // get username from firebase and set it
    private fun getUsername(view: View, uid: String?) {
        val usernameId = view.findViewById<TextView>(R.id.settings_fragment_username)
        db.getReference("users").child(uid!!).child("username")
            .get()
            .addOnSuccessListener {

                // collect username from Firebase and display it
                val usernameFromDatabase = it.value.toString()
                usernameId.text = usernameFromDatabase

            }
            .addOnFailureListener {
                Log.d("TAG", "Can't get username from firebase")
            }

    }


    // get avatar from firebase and display it
    private fun getAvatar(view: View, uid: String?){
        val avatar = view.findViewById<ImageView>(R.id.settings_fragment_avatar)
        db.getReference("users/$uid/avatar").get()
            .addOnSuccessListener {
                val imageFromFirebase = it.value
                Glide.with(view.context).load(imageFromFirebase).into(avatar!!)
            }
            .addOnFailureListener {
                Log.d("TAG", "Can't get image from firebase")
            }
    }



    // refresh username when username data change
    private fun refreshUsername(view: View, uid: String?){
        val usernameId = view.findViewById<TextView>(R.id.settings_fragment_username)
        val userRef = db.getReference("/users/$uid/username")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usernameId.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }



}