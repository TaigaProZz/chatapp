package com.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.account.AccountMainActivity
import com.chatapp.adapters.MainActivityAdapter
import com.chatapp.conversation.ChatActivity
import com.chatapp.conversation.NewConversationActivity
import com.chatapp.conversation.NewConversationActivity.Companion.USER_KEY
import com.chatapp.models.ChatMessage
import com.chatapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class MainActivity : AppCompatActivity() {


    companion object {
        var currentUser: User? = null
        val db =
            Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
    }

    val TAG = "TagMainActivity"
    private val auth = Firebase.auth
    private val adapter = GroupieAdapter()


    override fun onStart() {
        super.onStart()
        checkIfUserIsConnected()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // call functions
        fetchUser()
        listenMessageFromDatabase()


        // toolbar settings
        supportActionBar?.title = "ChatApp"

        // recycler view settings
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view_main)
        recyclerview.adapter = adapter

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(applicationContext, ChatActivity::class.java)

            val row = item as MainActivityAdapter
            
            intent.putExtra(USER_KEY, row.chatUser)
            startActivity(intent)
        }

    }

    private val lastMessageList = HashMap<String, ChatMessage>()
    private fun refreshRecyclerView() {
        adapter.clear()
        lastMessageList.values.forEach {
            adapter.add(MainActivityAdapter(it))
        }
    }

    private fun listenMessageFromDatabase() {
        // get the username of the user logged
        if (auth.uid != null) {
            val refUsername = db.getReference("/users").child(auth.uid!!).child("/username")
            refUsername.get()
                .addOnSuccessListener {
                    // collect message from database
                    val userUsername = it.value.toString()
                    val ref = db.getReference("last-message/$userUsername")
                    ref.addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            val message = snapshot.getValue<ChatMessage>() ?: return
                            lastMessageList[snapshot.key!!] = message
                            refreshRecyclerView()

                        }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            val message = snapshot.getValue<ChatMessage>() ?: return
                            lastMessageList[snapshot.key!!] = message
                            refreshRecyclerView()
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                        }

                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
        }

    }

    // fetch user information
    private fun fetchUser() {
        val uid = auth.uid
        val ref = db.getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
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
            val intent =
                Intent(applicationContext, AccountMainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
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
                startActivity(
                    Intent(
                        applicationContext,
                        NewConversationActivity::class.java
                    )
                )

            R.id.user_profile_menu ->
                startActivity(
                    Intent(
                        applicationContext,
                        UserProfileActivity::class.java
                    )
                )
        }
        return super.onOptionsItemSelected(item)

    }
}
