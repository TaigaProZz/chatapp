package com.chatapp.account.avatar

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.mainActivity.MainActivity
import com.chatapp.R
import com.chatapp.databinding.ActivityChoiceAvatarBinding
import com.chatapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class AvatarChoiceActivity : AppCompatActivity() {

    private lateinit var root: ActivityChoiceAvatarBinding
    private var db = Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
    val auth = Firebase.auth
    var mGoogleCode: String =""


    companion object {
        const val TAG = "AvatarChoice"
    }

    override fun onStart() {
        super.onStart()

        // call functions
        saveGoogleProfileToFirebase()
        setUsername()
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
        // skip the avatar choice btn and set a default avatar
        root.skipAvatarChoiceBtn.setOnClickListener {
            val uid = auth.uid
            val ref = db.getReference("users/$uid")
            // set a default avatar
            // dont force default image if logged with google
            if (mGoogleCode != "111"){
                ref.child("avatar").setValue("https://firebasestorage.googleapis.com/v0/b/chat-app-84489.appspot.com/o/images%2FAndroid_robot.svg.png?alt=media&token=66c7f3bf-1608-451f-916e-01ce8264dd3d")
                startActivity(Intent(applicationContext, MainActivity::class.java))

            }else{
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }

        // confirm button avatar, and upload the image on firebase database
        root.confirmAvatarButton.setOnClickListener {
            Log.d(TAG, "send image to firebase and go next activity")

            uploadAvatarToFirestore()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    var selectedImage: Uri? = null


    private fun saveGoogleProfileToFirebase(){
        mGoogleCode = intent.getStringExtra("requestCode").toString()
        Log.d(TAG, mGoogleCode)
        if (mGoogleCode == "111"){
            val uid = auth.uid
            val ref = MainActivity.db.getReference("/users/$uid")
            Log.d("TagAccountMain", "${auth.currentUser?.displayName} " +
                    "${auth.currentUser?.email!!} $uid")
            val user = User(
                auth.currentUser?.displayName!!,
                auth.currentUser?.email!!,
                avatar = auth.currentUser?.photoUrl.toString(),
                uid = uid!!)
            ref.setValue(user)
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

    }

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
    private fun uploadAvatarToFirestore() {
        if (selectedImage == null) return
        // collect uid and image directory from Firebase Storage
        val uid = auth.uid
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


    private fun setUsername(){
        val uid = auth.uid
        val ref = db.getReference("/users/$uid/username").get()
        ref.addOnSuccessListener {
            val getString = findViewById<TextView>(R.id.avatar_username)
            getString.text = it.value.toString()
        }
    }


    private fun saveAvatarInfo(avatar: String) {
        val uid = auth.uid
        val ref = db.getReference("users/$uid")

        // put the link of the avatar from Firebase Storage to the user infos of Database
        ref.child("avatar").setValue(avatar)

    }
}


