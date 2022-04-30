package com.chatapp.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.MainActivity
import com.chatapp.R
import com.chatapp.account.avatar.AvatarChoiceActivity
import com.chatapp.account.avatar.User
import com.chatapp.account.login.LoginEmailActivity
import com.chatapp.account.register.RegisterEmailActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountMainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)

        auth = Firebase.auth

        /*          BUTTONS           */

        findViewById<Button>(R.id.avatar_btn).setOnClickListener {
            startActivity(Intent(applicationContext, AvatarChoiceActivity::class.java))
        }

        // google button sign in
        findViewById<SignInButton>(R.id.google_button).setOnClickListener {
            createRequest()
        }


        // goto login with email button
        findViewById<Button>(R.id.login_email_button).setOnClickListener {
            startActivity(Intent(applicationContext, LoginEmailActivity::class.java))
        }

        // goto register with email button
        findViewById<Button>(R.id.register_email_button).setOnClickListener {
            startActivity(Intent(applicationContext, RegisterEmailActivity::class.java))
        }

        // back arrow
        findViewById<ImageView>(R.id.backArrow).setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }


    /*                  FUNCTIONS to login with GOOGLE                */

    private fun createRequest() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, 123)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("ResultGoogleId", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()

            } catch (e: ApiException) {
                Toast.makeText(this, "Connexion échouée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //saveUserInfo()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Connexion échouée", Toast.LENGTH_SHORT)
                        .show()

                }
            }
    }

    private fun saveUserInfo(profileImage: String) {

        val firebase = Firebase.auth.currentUser
        val uid = Firebase.auth.uid ?: ""
        Log.d(AvatarChoiceActivity.TAG, "uid: $uid")

        val ref = Firebase.firestore.collection("/users").document("/$uid")
        val user = User(firebase?.email, uid, profileImage)

        ref.set(user)
            .addOnSuccessListener {
                Log.d(AvatarChoiceActivity.TAG, "user infos added")
            }
            .addOnFailureListener {
                Log.d(AvatarChoiceActivity.TAG, "failed to add user infos")
            }
    }

}

class User(val email: String?, val uid: String, val profileImage: String)