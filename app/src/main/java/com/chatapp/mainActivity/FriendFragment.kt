package com.chatapp.mainActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class FriendFragment : Fragment() {


    private val friendAdapter = GroupieAdapter()


    private val db = MainActivity.db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)


        // recycler view params
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_friend_fragment)
        recyclerView.adapter = friendAdapter


        // val userUid = currentUser?.uid
        // val friendRef = db.getReference("$userUid/friends")


        val editTextSearchFriend = view?.findViewById<EditText>(R.id.search_friend_bar)


        view.findViewById<ImageView>(R.id.add_friend_ico).setOnClickListener {

            val text = editTextSearchFriend?.text.toString()
            val searchText = text.lowercase()
            userSearch(searchText)
            Log.d("fzfzfz", searchText)

        }


        // Inflate the layout for this fragment
        return view

    }

    private fun userSearch(text: String) {

        db.getReference("/users").orderByChild("username")
            .equalTo(text)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val getAllUser = it.getValue<User>()

                        friendAdapter.add(FriendFragmentAdapter(getAllUser!!))
                        Log.d("fzffzz", getAllUser.username)

                    }
                }


                override fun onCancelled(error: DatabaseError) {

                }
            })

    }
}

class FriendFragmentAdapter(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val avatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_friend_adapter)
        Glide.with(viewHolder.itemView).load(user.avatar).into(avatar)
        viewHolder.itemView.findViewById<TextView>(R.id.friend_adapter_username).text =
            user.username


    }

    override fun getLayout(): Int {
        return R.layout.adapter_friend_fragment
    }
}