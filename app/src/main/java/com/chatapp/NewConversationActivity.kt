package com.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.chatapp.account.User
import com.chatapp.adapters.NewConversationActivityAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Value
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter

class NewConversationActivity : AppCompatActivity() {

    private val database = Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation)

        // recycler view settings
        // val adapterItem = NewConversationActivityAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_newconversation)
        val adapter = GroupieAdapter()

        recyclerView.adapter = adapter

        fetchUsers()

    }

    // TODO Add friends only
    // get users from database and add it on the recycler view
    private fun fetchUsers(){
        val ref = database.getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_newconversation)
                val adapter = GroupieAdapter()

                snapshot.children.forEach{
                    val user = it.getValue(User::class.java)
                    val adapterItem = NewConversationActivityAdapter(user!!)

                    Log.d("NewMessages", it.toString())
                    adapter.add(adapterItem)
                    recyclerView.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}

