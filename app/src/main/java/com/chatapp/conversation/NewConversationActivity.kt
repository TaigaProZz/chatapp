package com.chatapp.conversation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.mainActivity.MainActivity
import com.chatapp.models.User
import com.chatapp.models.UserFriend
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NewConversationActivity : AppCompatActivity() {

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    private val adapter = GroupieAdapter()


    private val db =
        Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation)


        // recycler view settings
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_NewConversation)
        recyclerView.adapter = adapter
        fetchUsers()


        /*          BUTTONS           */
        // Back Arrow
        findViewById<ImageView>(R.id.toolbar_arrow).setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }


    }

    // get users from database and add it on the recycler view
    private fun fetchUsers() {
        val auth = Firebase.auth
        val ref = db.getReference("/users/${auth.uid}/friends").orderByChild("username")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_NewConversation)
                val adapter = GroupieAdapter()

                snapshot.children.forEach {

                    val collectedUser = it.getValue<UserFriend>()

                    val collectedUid = collectedUser?.uid
                    db.getReference("users/$collectedUid/").get()
                        .addOnSuccessListener { it1 ->
                            val user = it1.getValue(User::class.java)
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
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}


class NewConversationActivityAdapter(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.findViewById<TextView>(R.id.username_user_adapter).text = user.username
        val avatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_user_adapter)
        Glide.with(viewHolder.itemView).load(user.avatar).into(avatar)

    }

    override fun getLayout(): Int {
        return R.layout.adapter_users_new_conversation
    }

}

