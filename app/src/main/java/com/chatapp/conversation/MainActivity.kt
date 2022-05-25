package com.chatapp.conversation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.UserProfileActivity
import com.chatapp.account.AccountMainActivity
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
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
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
    val lastMessageList = TreeMap<String, ChatMessage>()



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

        // add conversation button
        findViewById<FloatingActionButton>(R.id.add_conversation_button)
            .setOnClickListener {
                startActivity(Intent(applicationContext, NewConversationActivity::class.java))
            }


        // recycler view settings
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view_main)
        recyclerview.adapter = adapter

        // open the conversation with user selected
        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(applicationContext, ChatActivity::class.java)
            val row = item as MainActivityAdapter
            intent.putExtra(USER_KEY, row.chatUser)
            startActivity(intent)
        }

        val userUid = auth.uid
        // long press on item
        adapter.setOnItemLongClickListener { item, view ->

            val message = item as MainActivityAdapter
            val ref = db.getReference("last-message/$userUid/${message.chatUser}")

            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.conversation_option, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    // TODO delete item + delete in firebase too
                    R.id.delete_conversation -> Log.d("taggbou", "$ref")

                }
                true
            }
            popupMenu.show()
            true
        }
    }




    private fun deleteItem(position: Int){

    }

    private fun refreshRecycler() {
        adapter.clear()

        // sort the list by time and add it to the recycler view
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

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
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
            R.id.delete_conversation ->
                startActivity(Intent(applicationContext, UserProfileActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}

/*    CLASS ADAPTER   */
class MainActivityAdapter(private val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {

    var chatUser: User? = null
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val auth = Firebase.auth
        // set last message of the user
        viewHolder.itemView.findViewById<TextView>(R.id.last_message).text = chatMessage.text

        // check which user sent the message
        val chatPartnerId: String
        if (chatMessage.toUid == auth.uid) {
            chatPartnerId = chatMessage.userUid
        } else {
            chatPartnerId = chatMessage.toUid
        }
        // set user data to the adapter ( username, avatar )
        val ref = MainActivity.db.getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatUser = snapshot.getValue<User>()
                // username
                viewHolder.itemView.findViewById<TextView>(R.id.username_user_adapter_main).text =
                    chatUser?.username
                //avatar
                val adapterAvatar =
                    viewHolder.itemView.findViewById<ImageView>(R.id.avatar_user_adapter_main)
                Glide.with(viewHolder.itemView).load(chatUser?.avatar).into(adapterAvatar)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    override fun getLayout(): Int {
        return R.layout.adapter_main_activity
    }


}



