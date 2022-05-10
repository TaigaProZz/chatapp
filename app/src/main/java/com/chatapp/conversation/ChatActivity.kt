package com.chatapp.conversation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chatapp.R
import com.chatapp.account.User
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val user = intent.getParcelableExtra<User>(NewConversationActivity.USER_KEY)

        // toolbar settings
        supportActionBar?.title = user?.username

        //val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_chat)

        dummyStuff()

    }

    private fun dummyStuff() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_chat)

        val adapter = GroupieAdapter()
        val adapterFriendItem = ChatFriendAdapter("ff")
        val adapterUserItem = ChatUserAdapter("ffe")
        recyclerView.adapter = adapter

        adapter.add(adapterFriendItem)
        adapter.add(adapterUserItem)
        adapter.add(adapterFriendItem)
        adapter.add(adapterUserItem)
        adapter.add(adapterFriendItem)
        adapter.add(adapterUserItem)
        adapter.add(adapterFriendItem)
        adapter.add(adapterUserItem)

    }

}

class ChatFriendAdapter(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_body_friend).text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_friend_row
    }
}


class ChatUserAdapter(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.message_body_user).text = text

    }

    override fun getLayout(): Int {
        return R.layout.chat_user_row
    }
}
