package com.chatapp.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatapp.R
import com.chatapp.account.avatar.AvatarChoiceActivity
import com.chatapp.account.login.LoginEmailActivity
import com.chatapp.account.register.RegisterEmailActivity
import com.chatapp.conversation.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountMainActivity : AppCompatActivity() {

    companion object{
        var requestCode = "111"
    }

    private val auth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)


        /*          BUTTONS           */
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

    // open google sign in activity
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
                firebaseAuthWithGoogle(account.idToken!!)
                Toast.makeText(applicationContext, "Connexion réussie", Toast.LENGTH_SHORT).show()

            } catch (e: ApiException) {
                Toast.makeText(this, "Connexion échouée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // sign in with google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // sign in success
                    val intent = Intent(applicationContext, AvatarChoiceActivity::class.java)
                    intent.putExtra("requestCode", "111")
                    startActivity(intent)
                } else {
                    // if sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Connexion échouée", Toast.LENGTH_SHORT)
                        .show()

                }
            }
    }
}

