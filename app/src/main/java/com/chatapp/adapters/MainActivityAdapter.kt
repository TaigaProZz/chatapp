package com.chatapp.adapters

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chatapp.MainActivity
import com.chatapp.R
import com.chatapp.models.ChatMessage
import com.chatapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MainActivityAdapter(private val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {

    var chatUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val auth = Firebase.auth
        viewHolder.itemView.findViewById<TextView>(R.id.last_message).text = chatMessage.text


        val chatPartnerId: String
        if (chatMessage.toUid == auth.uid) {
            chatPartnerId = chatMessage.userUid
        } else {
            chatPartnerId = chatMessage.toUid
        }

        val ref = MainActivity.db.getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatUser = snapshot.getValue<User>()
                viewHolder.itemView.findViewById<TextView>(R.id.username_user_adapter_main).text =
                    chatUser?.username

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
