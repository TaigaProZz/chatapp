package com.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chatapp.account.AccountMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        const val TAG = "MainActivityAvatar"
    }

    override fun onStart() {
        super.onStart()

        checkIfUserIsConnected()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view_main)
        recyclerview.adapter



    }

    private fun checkIfUserIsConnected(){
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        // if the user is not connected, force login activity
        if (currentUser == null) {
            val intent = Intent(applicationContext, AccountMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.new_conversation_menu ->
                startActivity(Intent(applicationContext, NewConversationActivity::class.java))

            R.id.user_profile_menu ->
                startActivity(Intent(applicationContext, UserProfileActivity::class.java))
        }
        return super.onOptionsItemSelected(item)

    }


}