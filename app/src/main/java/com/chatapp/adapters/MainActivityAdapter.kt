package com.chatapp.adapters

import com.chatapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class MainActivityAdapter(): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.main_activity_adapter
    }

}



