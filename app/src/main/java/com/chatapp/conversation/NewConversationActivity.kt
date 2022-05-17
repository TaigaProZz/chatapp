package com.chatapp.conversation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chatapp.R
import com.chatapp.adapters.NewConversationActivityAdapter
import com.chatapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter

class NewConversationActivity : AppCompatActivity() {

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    private val db =
        Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation)

        // toolbar settings
        supportActionBar?.title = "Friends"


        // recycler view settings
        // val adapterItem = NewConversationActivityAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_NewConversation)
        val adapter = GroupieAdapter()
        recyclerView.adapter = adapter

        fetchUsers()

    }

    // TODO Add friends only
    // get users from database and add it on the recycler view
    private fun fetchUsers() {
        val ref = db.getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_NewConversation)
                val adapter = GroupieAdapter()

                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    val adapterItem = NewConversationActivityAdapter(user!!)
                    adapter.add(adapterItem)
                    recyclerView.adapter = adapter

                    adapter.setOnItemClickListener { item, view ->
                        val userItem = item as NewConversationActivityAdapter
                        val intent = Intent(applicationContext, ChatActivity::class.java)
                        intent.putExtra(USER_KEY, userItem.user)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}

