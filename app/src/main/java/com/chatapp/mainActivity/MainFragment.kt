package com.chatapp.mainActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.conversation.ChatActivity
import com.chatapp.conversation.NewConversationActivity
import com.chatapp.mainActivity.MainActivity.Companion.currentUser
import com.chatapp.mainActivity.MainActivity.Companion.db
import com.chatapp.models.ChatMessage
import com.chatapp.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.util.*

class MainFragment : Fragment() {
    companion object{
        private val adapter = GroupieAdapter()
        val lastMessageList = TreeMap<String, ChatMessage>()
    }
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenMessageFromDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // add conversation button
        view.findViewById<FloatingActionButton>(R.id.add_conversation_button)
            .setOnClickListener {
                startActivity(Intent(view.context, NewConversationActivity::class.java))
            }


        // Recycler view settings
        val recyclerview = view.findViewById<RecyclerView>(R.id.recycler_view_main)
        recyclerview.adapter = adapter



        // Open the conversation with user selected
        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(view.context, ChatActivity::class.java)
            val row = item as MainActivityAdapter
            intent.putExtra(NewConversationActivity.USER_KEY, row.chatUser)
            startActivity(intent)
        }


        // Inflate the layout for this fragment
        return view
    }


    private fun refreshRecycler() {
        adapter.clear()

        // Sort the list by time and add it to the recycler view
        lastMessageList.values.sortedBy { message ->
            message.time
        }.forEach {
            adapter.add(0, MainActivityAdapter(it))
        }
    }

    private fun listenMessageFromDatabase() {
        // Get the username of the user logged
        if (auth.uid != null) {
            // Collect message from database
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
                    val message = snapshot.getValue<ChatMessage>() ?: return
                    lastMessageList[snapshot.key!!] = message
                    refreshRecycler()

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<ChatMessage>() ?: return
                    lastMessageList[snapshot.key!!] = message
                    refreshRecycler()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    /*    CLASS ADAPTER   */
    class MainActivityAdapter(private val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {

        var chatUser: User? = null

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            val auth = Firebase.auth
            // Set LAST MESSAGE of the user
            viewHolder.itemView.findViewById<TextView>(R.id.last_message).text = chatMessage.text

            // Check WHICH USER sent the message
            val chatPartnerId: String = if (chatMessage.toUid == auth.uid) {
                chatMessage.userUid
            } else {
                chatMessage.toUid
            }

            // Set USER DATA to the adapter (username, avatar)
            val ref = db.getReference("/users/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatUser = snapshot.getValue<User>()
                    // Username
                    viewHolder.itemView.findViewById<TextView>(R.id.username_user_adapter_main).text =
                        chatUser?.username
                    // Avatar
                    val adapterAvatar =
                        viewHolder.itemView.findViewById<ImageView>(R.id.avatar_user_adapter_main)
                    Glide.with(viewHolder.itemView).load(chatUser?.avatar).into(adapterAvatar)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            // Set message DATE
            // TODO afficher la date par rapport à l'anciennetée du message : jour ou heures
            viewHolder.itemView.findViewById<TextView>(R.id.time_main_activity).text =
                chatMessage.time.subSequence(11, 16)


            // Long click PopUp
            popUp(position)

        }

        override fun getLayout(): Int {
            return R.layout.adapter_main_activity
        }

        private fun popUp(position: Int) {
            // Long press on item

            adapter.setOnItemLongClickListener { item, view ->
                val message = item as MainActivityAdapter
                val auth = Firebase.auth
                val userUid = auth.uid
                val uid = message.chatUser?.uid
                val refLastMessage = db.getReference("last-message/$userUid/$uid")
                // Pop up
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.menuInflater.inflate(R.menu.message_option, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        // Delete message button
                        R.id.delete_conversation -> {
                            // TODO refresh the recyclerview
                            refLastMessage.removeValue().addOnSuccessListener {
                                adapter.notifyItemRangeRemoved(
                                    position,
                                    lastMessageList.size
                                )
                            }
                        }
                    }
                    true
                }
                popupMenu.show()
                true
            }
        }
    }
}
