package com.chatapp.conversation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.models.ChatMessage
import com.chatapp.models.User
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
import java.util.*

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
        findViewById<ImageView>(R.id.sendMsgBtn).setOnClickListener {
            sendMessage()
        }

    }


    private fun sendMessage() {
        val messageField = findViewById<EditText>(R.id.msg_box_edittext).text.toString()
        if (messageField.isEmpty()) {
            return
        } else {

            val userUid = auth.uid ?: return
            val toUserUid = toUser?.uid ?: return
            val calendar = Calendar.getInstance().time.time

            val ref = db.getReference("/messages/$toUserUid/$userUid").push()
            val toRef = db.getReference("/messages/$userUid/$toUserUid").push()

            val chatMessage = ChatMessage(
                ref.key!!,
                messageField,
                userUid,
                toUserUid,
                calendar.toString()
            )

            ref.setValue(chatMessage).addOnSuccessListener {
                findViewById<EditText>(R.id.msg_box_edittext).text.clear()

            }
            toRef.setValue(chatMessage)

            val toUid = toUser?.uid
            val lastMessageRef = db.getReference("/last-message/$toUid/$userUid")
            lastMessageRef.setValue(chatMessage)


            val lastMessageToRef = db.getReference("/last-message/$userUid/$toUid")
            lastMessageToRef.setValue(chatMessage)


        }

    }


    private fun listenMessage() {
        val userUid = auth.uid ?: return

        // get the username of the user logged
        val refUsername = db.getReference("/users").child(userUid).child("/username").get()

        refUsername.addOnSuccessListener {
            // collect message from database

            val toUserUid = toUser?.uid
            val ref = db.getReference("/messages/$userUid/$toUserUid")

            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<ChatMessage>()
                    if (message != null) {
                        if (message.userUid == userUid) {
                            val user = MainActivity.currentUser
                            adapter.add(ChatUserAdapter(message.text, user!!, message))

                        } else {
                            adapter.add(ChatFriendAdapter(message.text, toUser!!, message))
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
        val sendMsgBtn = findViewById<ImageView>(R.id.sendMsgBtn)


        // check if message field is not empty, hide button if empty, or display if not empty
        messageField.addTextChangedListener {
            Log.d(TAG, it.toString())
            if(messageField.text.isEmpty()){
                return@addTextChangedListener
            }


        }
    }
}


class ChatFriendAdapter(val text: String, val user: User, val chatMessage: ChatMessage) :
    Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // set text of the message
        val textField = viewHolder.itemView.findViewById<TextView>(R.id.message_body_friend)
        textField.text = text
        // set friend avatar with extra intent
        val friendAvatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_friend_message)
        Glide.with(viewHolder.itemView).load(user.avatar).into(friendAvatar)

        // set time of the message
        viewHolder.itemView.findViewById<TextView>(R.id.text_time_friend).text = chatMessage.time.toString()

        viewHolder.itemView.findViewById<TextView>(R.id.message_body_friend).setOnLongClickListener {
            popUp(viewHolder.itemView.findViewById(R.id.message_body_friend))
            isLongClickable
        }

    }

    override fun getLayout(): Int {
        return R.layout.chat_friend_row
    }
}

class ChatUserAdapter(val text: String, val user: User, val chatMessage: ChatMessage) :
    Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // set text of the message
        val textField = viewHolder.itemView.findViewById<TextView>(R.id.message_body_user)
        textField.text = text
        // set user avatar
        val userAvatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_user_message)
        Glide.with(viewHolder.itemView).load(user.avatar).into(userAvatar)
        // set time of the message
        viewHolder.itemView.findViewById<TextView>(R.id.text_time_user).text = chatMessage.time.toString()

        viewHolder.itemView.findViewById<TextView>(R.id.message_body_user).setOnLongClickListener {
            popUp(viewHolder.itemView.findViewById(R.id.message_body_user))
            isLongClickable
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_user_row
    }
}

private fun popUp(view: View) {
    val popupMenu = PopupMenu(view.context, view)
    popupMenu.menuInflater.inflate(R.menu.message_option, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener {
        true
    }
    popupMenu.show()
}