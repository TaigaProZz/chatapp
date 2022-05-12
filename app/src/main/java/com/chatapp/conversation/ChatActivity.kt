package com.chatapp.conversation

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.models.ChatMessage
import com.chatapp.models.User
import com.google.android.datatransport.runtime.time.TimeModule_UptimeClockFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatActivity : AppCompatActivity() {

    companion object {
        val TAG = "TagChatActivity"
    }

    private val db =
        Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
    private val auth = Firebase.auth

    val adapter = GroupieAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // init val
        val user = intent.getParcelableExtra<User>(NewConversationActivity.USER_KEY)

        // recycler view settings
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_chat)
        recyclerView.adapter = adapter


        // call functions
        listenMessage()


        // toolbar settings
        supportActionBar?.title = user?.username


        // send message button
        findViewById<ImageButton>(R.id.sendMsgBtn).setOnClickListener {
            sendMessage()
            Log.d(TAG, "send mess btn ")
        }

    }


    private fun sendMessage() {
        val userUid = Firebase.auth.uid
        val user = intent.getParcelableExtra<User>(NewConversationActivity.USER_KEY)
        val messageField = findViewById<EditText>(R.id.msg_box_edittext).text.toString()

        val ref = db.getReference("/messages").push() // /$userUid/${user!!.username}
        val chatMessage = ChatMessage(
            ref.key!!,
            messageField,
            userUid!!,
            user!!.uid,
            TimeModule_UptimeClockFactory.uptimeClock().time
        )

        ref.setValue(chatMessage).addOnSuccessListener {
            findViewById<EditText>(R.id.msg_box_edittext).text.clear()

        }

    }

    private fun listenMessage() {
        val userUid = Firebase.auth.uid
        val user = intent.getParcelableExtra<User>(NewConversationActivity.USER_KEY)
        val ref = db.getReference("/messages") // /$userUid/${user!!.username}
        Log.d(TAG, "ef ${ref.key}")


        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<ChatMessage>()

                if (message != null) {
                    Log.d(TAG, message.text)

                    if (message.userUid == userUid) {
                        adapter.add(ChatUserAdapter(message.text))

                    } else {
                        val toUser = intent.getParcelableExtra<User>(NewConversationActivity.USER_KEY)
                        Log.d(TAG, "sze ${toUser!!.avatar}")

                        adapter.add(ChatFriendAdapter(message.text, toUser))

                    }

                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, ref.get().toString())

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

}


class ChatFriendAdapter(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_body_friend).text = text
        val friendAvatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_friend_message)

        Log.d("TagChatActivity", user.avatar)
        if (user.avatar == "tdb"){
            Glide.with(viewHolder.itemView).load(R.drawable.ic_launcher_foreground).into(friendAvatar)
        } else  {
            Glide.with(viewHolder.itemView).load(user.avatar).into(friendAvatar)

        }


    }

    override fun getLayout(): Int {
        return R.layout.chat_friend_row
    }
}


class ChatUserAdapter(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_body_user).text = text

        val userAvatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_user_message)
        val uid = Firebase.auth.uid
        val db =
            Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
        db.getReference("users").child(uid!!).child("avatar")
            .get()
            .addOnSuccessListener {
                Log.d(ChatActivity.TAG, "le ${it.value}")
                val avatar = it.value
                Glide.with(viewHolder.itemView).load(avatar).into(userAvatar)

            }
    }

    override fun getLayout(): Int {
        return R.layout.chat_user_row
    }
}
