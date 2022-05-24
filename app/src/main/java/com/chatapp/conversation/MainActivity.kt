package com.chatapp.conversation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chatapp.R
import com.chatapp.UserProfileActivity
import com.chatapp.account.AccountMainActivity
import com.chatapp.adapters.MainActivityAdapter
import com.chatapp.conversation.NewConversationActivity.Companion.USER_KEY
import com.chatapp.models.ChatMessage
import com.chatapp.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import java.util.*


class MainActivity : AppCompatActivity() {


    companion object {

        var currentUser: User? = null
        val db =
            Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
    }

    val tag = "TagMainActivity"
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

        // add conversation
        findViewById<FloatingActionButton>(R.id.add_conversation_button)
            .setOnClickListener {
                startActivity(Intent(applicationContext, NewConversationActivity::class.java))
            }


        // recycler view settings
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view_main)
        recyclerview.adapter = adapter
        adapter.clear()

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(applicationContext, ChatActivity::class.java)
            val row = item as MainActivityAdapter
            intent.putExtra(USER_KEY, row.chatUser)
            startActivity(intent)
        }

    }

    private val lastMessageList = TreeMap<String, ChatMessage>()

    private fun refreshRecycler() {
        adapter.clear()

        lastMessageList.values.sortedBy { message ->
            message.time
        }.forEach {
            adapter.add(0, MainActivityAdapter(it))

        }
    }

    private fun listenMessageFromDatabase() {

        // get the username of the user logged
        if (auth.uid != null) {
            // collect message from database
            val userUid = auth.uid

            val ref = db.getReference("last-message/$userUid")

            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<ChatMessage>() ?: return
                    lastMessageList[snapshot.key!!] = message
                    refreshRecycler()

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<ChatMessage>() ?: return
                    lastMessageList[snapshot.key!!] = message
                    refreshRecycler()

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
            R.id.user_profile_menu ->
                startActivity(Intent(applicationContext, UserProfileActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}

