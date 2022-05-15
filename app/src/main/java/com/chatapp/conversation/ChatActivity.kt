package com.chatapp.conversation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class ChatActivity : AppCompatActivity() {

    companion object {
        val TAG = "TagChatActivity"
    }

    private val db =
        Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
    val adapter = GroupieAdapter()
    var toUser: User? = null
    private val auth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        // init val
        toUser = intent.getParcelableExtra(NewConversationActivity.USER_KEY)


        // recycler view settings
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_chat)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(adapter.itemCount - 1)


        // call functions
        listenMessage()
        checkEditTextIsEmpty()


        // toolbar settings
        supportActionBar?.title = toUser?.username


        // send message button
        findViewById<Button>(R.id.sendMsgBtn).setOnClickListener {
            sendMessage()
            Log.d(TAG, "send mess btn ")
        }

    }


    private fun sendMessage() {
        val userUid = auth.uid ?: return
        val toUserUsername = toUser?.username
        val messageField = findViewById<EditText>(R.id.msg_box_edittext).text.toString()
        if (messageField.isEmpty()) {
            return
        } else {
            db.getReference("/users").child(userUid).child("/username")
                .get().addOnSuccessListener {
                    val userUsername = it.value.toString()
                    val ref = db.getReference("/messages/$userUsername/$toUserUsername").push()
                    val toRef = db.getReference("/messages/$toUserUsername/$userUsername").push()

                    val chatMessage = ChatMessage(
                        ref.key!!,
                        messageField,
                        userUid,
                        toUser!!.uid,
                        TimeModule_UptimeClockFactory.uptimeClock().time

                    )

                    ref.setValue(chatMessage).addOnSuccessListener {
                        findViewById<EditText>(R.id.msg_box_edittext).text.clear()

                    }
                    toRef.setValue(chatMessage)

                    val lastMessageRef =
                        db.getReference("/last-message/$toUserUsername/$userUsername")
                            .setValue(chatMessage)
                    val lastMessageToRef =
                        db.getReference("/last-message/$userUsername/$toUserUsername")
                            .setValue(chatMessage)


                }

        }
    }


    private fun listenMessage() {
        val userUid = auth.uid ?: return

        // get the username of the user logged
        db.getReference("/users").child(userUid).child("/username")
            .get().addOnSuccessListener {
                // collect message from database
                val userUsername = it.value.toString()
                val ref = db.getReference("/messages/${toUser?.username}/$userUsername")

                ref.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val message = snapshot.getValue<ChatMessage>()
                        if (message != null) {
                            if (message.userUid == userUid) {
                                val user = MainActivity.currentUser
                                Log.d("TagAccountMai", "${user?.uid}")
                                adapter.add(ChatUserAdapter(message.text, user!!))
                            } else {
                                adapter.add(ChatFriendAdapter(message.text, toUser!!))
                            }
                        }
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_chat)
                        recyclerView.scrollToPosition(adapter.itemCount - 1)

                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

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

    private fun checkEditTextIsEmpty() {
        val messageField = findViewById<EditText>(R.id.msg_box_edittext)
        val sendMsgBtn = findViewById<Button>(R.id.sendMsgBtn)
        sendMsgBtn.isVisible = false


        // check if message field is not empty, hide button if empty, or display if not empty
        messageField.addTextChangedListener {
            Log.d(TAG, it.toString())
            sendMsgBtn.isVisible = messageField.text.isNotEmpty()


        }
    }

}

class ChatFriendAdapter(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_body_friend).text = text
        // set friend avatar with extra intent
        val friendAvatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_friend_message)
        Glide.with(viewHolder.itemView).load(user.avatar).into(friendAvatar)

    }

    override fun getLayout(): Int {
        return R.layout.chat_friend_row
    }
}


class ChatUserAdapter(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_body_user).text = text

        // set user avatar
        val userAvatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_user_message)
        Glide.with(viewHolder.itemView).load(user.avatar).into(userAvatar)

    }

    override fun getLayout(): Int {
        return R.layout.chat_user_row
    }
}
