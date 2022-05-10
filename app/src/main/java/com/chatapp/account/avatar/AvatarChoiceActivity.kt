package com.chatapp.account.avatar

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.MainActivity
import com.chatapp.R
import com.chatapp.databinding.ActivityChoiceAvatarBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class AvatarChoiceActivity : AppCompatActivity() {

    private lateinit var root: ActivityChoiceAvatarBinding
    private var db = Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")


    companion object {
        const val TAG = "AvatarChoice"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = ActivityChoiceAvatarBinding.inflate(layoutInflater)
        val view = root.root
        setContentView(view)


        // toolbar settings
        supportActionBar?.title = "Avatar"


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

            uploadInfoToFirebase()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    var selectedImage: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
            // change the image with the selected one
            findViewById<CircleImageView>(R.id.avatar_avatar_final).setImageBitmap(bitmap)
            // set the button invisible to see circle avatar
            findViewById<Button>(R.id.avatar_circle).alpha = 0f
        }
    }


    // upload the avatar to Firebase storage
    private fun uploadInfoToFirebase() {
        if (selectedImage == null) return
        // collect uid and image directory from Firebase Storage
        val uid = Firebase.auth.uid
        val ref = Firebase.storage.getReference("/images/$uid")

        // put the selectedImage on the database
        ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    // save avatar info to Firebase Database
                    saveAvatarInfo(url.toString())
                }
            }
            .addOnFailureListener {
                return@addOnFailureListener
            }
    }


    private fun saveAvatarInfo(avatar: String) {
        val uid = Firebase.auth.uid
        val ref = db.getReference("users/$uid")

        // put the link of the avatar from Firebase Storage to the user infos of Database
        ref.child("avatar").setValue(avatar)

    }
}


