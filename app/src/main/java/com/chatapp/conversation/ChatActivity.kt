package com.chatapp.conversation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.mainActivity.MainActivity
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
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
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
    var valueListener: ValueEventListener? = null

    override fun onDestroy() {
        super.onDestroy()
        val uid = auth.uid
        val ref = db.getReference("/messages/$uid/${toUser?.uid}")
        ref.removeEventListener(valueListener!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // parse extra
        toUser = intent.getParcelableExtra(NewConversationActivity.USER_KEY)


        // recycler view settings
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_chat)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(adapter.itemCount - 1)


        // call functions
        listenMessage()
        checkEditTextIsEmpty()
        //checkIfMessageIsSeen(auth.uid!!)


        /// TOOLBAR SETTINGS ///

        // Username
        val getUsername = toUser?.username
        val getUsernameFromView = findViewById<TextView>(R.id.toolbar_username)
        getUsernameFromView.text = getUsername

        // Avatar
        val getUserAvatar = toUser?.avatar
        val getAvatarFromView = findViewById<CircleImageView>(R.id.toolbar_avatar)
        Glide.with(applicationContext).load(getUserAvatar).into(getAvatarFromView)

        // Back Arrow
        findViewById<ImageView>(R.id.toolbar_arrow).setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }


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

            val pattern = "dd.MM.yyyy HH:mm:ss z"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val date = simpleDateFormat.format(Date())

            val ref = db.getReference("/messages/$toUserUid/$userUid").push()
            val toRef = db.getReference("/messages/$userUid/$toUserUid").push()

            val chatMessage = ChatMessage(
                ref.key!!,
                messageField,
                userUid,
                toUserUid,
                date,
                false
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


    private fun checkEditTextIsEmpty() {
        val messageField = findViewById<EditText>(R.id.msg_box_edittext)

        // check if message field is not empty, hide button if empty, or display if not empty
        messageField.addTextChangedListener {
            Log.d(TAG, it.toString())
            if (messageField.text.isEmpty()) {
                return@addTextChangedListener
            }
        }
    }
    // TODO check message is seen

    // private fun checkIfMessageIsSeen(userId: String) {

    //     val uid = auth.uid
    //     val ref = db.getReference("/messages/$uid/${toUser?.uid}")
    //     val ref2 = db.getReference("/messages/${toUser?.uid}/$uid")

    //     valueListener = ref.addValueEventListener(object : ValueEventListener {
    //         override fun onDataChange(snapshot: DataSnapshot) {
    //             snapshot.children.forEach {
    //                 val message = snapshot.getValue<ChatMessage>()
    //                 if (message?.toUid.equals(toUser?.uid) && message?.userUid.equals(userId)) {
    //                     Log.d("tagrg", "true eeee")

    //                     val hashMap: HashMap<String, Any> = HashMap()
    //                     hashMap["seen"] = true
    //                     snapshot.ref.updateChildren(hashMap)
    //                     adapter.notifyDataSetChanged()

    //                 }
    //             }
    //         }

    //         override fun onCancelled(error: DatabaseError) {
    //         }
    //     })

    //     valueListener = ref2.addValueEventListener(object : ValueEventListener {
    //         override fun onDataChange(snapshot: DataSnapshot) {
    //             snapshot.children.forEach {
    //                 val message = it.getValue<ChatMessage>()
    //                 if (message?.toUid.equals(toUser?.uid) && message?.userUid.equals(userId)) {
    //                     Log.d("tagrg", "true eeee")

    //                    //val hashMap: HashMap<String, Any> = HashMap()
    //                    //hashMap["seen"] = true
    //                    //snapshot.ref.updateChildren(hashMap)
    //                     adapter.notifyDataSetChanged()

    //                 }

    //             }
    //         }

    //         override fun onCancelled(error: DatabaseError) {

    //         }
    //     })
    // }

}


class ChatFriendAdapter(
    val text: String, val user: User, private val chatMessage: ChatMessage
) :
    Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // set MESSAGE TEXT
        val textField = viewHolder.itemView.findViewById<TextView>(R.id.message_body_friend)
        textField.text = text

        // set FRIEND AVATAR with extra intent
        val friendAvatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_friend_message)
        Glide.with(viewHolder.itemView).load(user.avatar).into(friendAvatar)

        // set TIME of the message
        viewHolder.itemView.findViewById<TextView>(R.id.text_time_friend).text =
            chatMessage.time.subSequence(11, 16)

        // Show if message is seen or not
        val isSeen = viewHolder.itemView.findViewById<TextView>(R.id.isSeenFriend)


        if (chatMessage.seen) {
            isSeen.text = "Seen"
        }


        // on LONG click
        viewHolder.itemView.findViewById<TextView>(R.id.message_body_friend).setOnLongClickListener {
            popUp(viewHolder.itemView.findViewById(R.id.message_body_friend))
            isLongClickable
        }

    }

    override fun getLayout(): Int {
        return R.layout.chat_friend_row
    }
}

class ChatUserAdapter(val text: String, val user: User, private val chatMessage: ChatMessage) :
    Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // Set text of the message
        val textField = viewHolder.itemView.findViewById<TextView>(R.id.message_body_user)
        textField.text = text

        // Set time of the message
        viewHolder.itemView.findViewById<TextView>(R.id.text_time_user).text =
            chatMessage.time.subSequence(11, 16)


        // Show if message is seen or not
        val isSeen = viewHolder.itemView.findViewById<TextView>(R.id.isSeenUser)
        if (chatMessage.seen) {
            isSeen.text = "Seen"
        }

        // Pop up
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

