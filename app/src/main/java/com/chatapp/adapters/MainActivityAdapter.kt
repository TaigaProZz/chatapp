package com.chatapp.adapters

import com.chatapp.R
import com.chatapp.account.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class MainActivityAdapter(user: User): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.main_activity_adapter
    }


}



