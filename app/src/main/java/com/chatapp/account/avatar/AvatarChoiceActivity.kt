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
import com.chatapp.databinding.ActivityChoiseAvatarBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class AvatarChoiceActivity : AppCompatActivity() {

    private lateinit var root: ActivityChoiseAvatarBinding

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
            Log.d(TAG, "avatar was selected")

            // change the image with the selected one
            findViewById<CircleImageView>(R.id.avatar_avatar_final).setImageBitmap(bitmap)
            // set the button invisible to see circle avatar
            findViewById<Button>(R.id.avatar_circle).alpha = 0f
        }
    }


    private fun uploadInfoToFirebase() {

        if (selectedImage == null) return

        val filename = UUID.randomUUID().toString()
        val ref = Firebase.storage.getReference("/images/$filename")

        // put the selectedImage on the database
        ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                Log.d(TAG, "avatar on database: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener { url ->
                    Log.d(TAG, "uri path: ${url.toString()}")

                    saveUserInfo(url.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
                return@addOnFailureListener
            }
    }


    private fun saveUserInfo(profileImage: String) {

        val firebase = Firebase.auth.currentUser
        val uid = Firebase.auth.uid ?: ""
        Log.d(TAG, "uid: $uid")

        val ref = Firebase.firestore.collection("/users").document("/$uid")
        val user = User(firebase?.email, uid, profileImage)

        ref.set(user)
            .addOnSuccessListener {
                Log.d(TAG, "user infos added")
            }
            .addOnFailureListener {
                Log.d(TAG, "failed to add user infos")
            }
    }
}


class User(val email: String?, val uid: String, val profileImage: String)