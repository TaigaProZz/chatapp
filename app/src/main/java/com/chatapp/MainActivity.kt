package com.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chatapp.account.AccountMainActivity
import com.chatapp.adapters.MainActivityAdapter
import com.chatapp.conversation.ChatActivity
import com.chatapp.conversation.NewConversationActivity
import com.chatapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter


class MainActivity : AppCompatActivity() {


    companion object {
        const val TAG = "TagMainActivity"
        val db =
            Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
        var currentUser: User? = null
        val auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()
        checkIfUserIsConnected()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchUser()


        // toolbar settings
        supportActionBar?.title = "ChatApp"

        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view_main)
        val groupieAdapter = GroupieAdapter()
        val adapterItem = MainActivityAdapter()
        recyclerview.adapter = groupieAdapter

        groupieAdapter.add(adapterItem)

    }

    private fun fetchUser(){
        val uid = auth.uid
        val ref = db.getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue<User>()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    // if user is not connected with Firebase, force go to AccountMainActivity
    private fun checkIfUserIsConnected() {

        val currentUser = auth.currentUser

        // if the user is not connected, force login activity
        if (currentUser == null) {
            val intent = Intent(applicationContext, AccountMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    // menu settings
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.new_conversation_menu ->
                startActivity(Intent(applicationContext, NewConversationActivity::class.java))

            R.id.user_profile_menu ->
                startActivity(Intent(applicationContext, UserProfileActivity::class.java))
        }
        return super.onOptionsItemSelected(item)

    }
}
