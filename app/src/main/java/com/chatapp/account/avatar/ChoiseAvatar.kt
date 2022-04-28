package com.chatapp.account.avatar

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.chatapp.MainActivity
import com.chatapp.R
import com.chatapp.databinding.ActivityChoiseAvatarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class ChoiseAvatar : AppCompatActivity() {

    private lateinit var root: ActivityChoiseAvatarBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        const val TAG = "AvatarChoice"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = ActivityChoiseAvatarBinding.inflate(layoutInflater)
        val view = root.root
        setContentView(view)


        val auth = Firebase.auth

        // show the username of the user
        val getUsernameFromFirebase = auth.currentUser?.displayName
        root.username.text = getUsernameFromFirebase


        // open photo gallery from avatar circle button
        root.avatarCircle.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }


        /*          BUTTONS           */

        // skip the avatar choice btn
        root.skipAvatarChoiceBtn.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        // confirm button avatar, and upload the image on firebase database
        root.confirmAvatarButton.setOnClickListener {
            Log.d(TAG, "send image to firebase and go next activity")

            uploadAvatarToFirebase()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }

    var selectedImage: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedImage = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

            val bitmapDrawable = BitmapDrawable(bitmap)

            Log.d(TAG, "avatar was selected")

            findViewById<Button>(R.id.avatar_circle).setBackgroundDrawable(bitmapDrawable)

        }
    }


    private fun uploadAvatarToFirebase() {
        if (selectedImage == null) {
            Log.d(TAG, "image is null")
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = Firebase.storage.getReference("/images/$filename")
        ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                Log.d(TAG, "avatar on database: ${it.metadata?.path}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }


    }


}