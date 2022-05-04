package com.chatapp.adapters

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.account.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NewConversationActivityAdapter(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.username_user_adapter).text = user.username

        val avatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_user_adapter)
        Glide.with(viewHolder.itemView).load(user.avatar).into(avatar)
    }

    override fun getLayout(): Int {
        return R.layout.users_new_conversation_adapter
    }

}