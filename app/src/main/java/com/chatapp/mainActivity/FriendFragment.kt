package com.chatapp.mainActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatapp.R
import com.chatapp.conversation.ChatActivity
import com.chatapp.conversation.NewConversationActivity
import com.chatapp.mainActivity.MainActivity.Companion.currentUser
import com.chatapp.mainActivity.MainActivity.Companion.db
import com.chatapp.models.User
import com.chatapp.models.UserFriend
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class FriendFragment : Fragment() {

    private val friendAdapter = GroupieAdapter()
    private val db = MainActivity.db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchUsers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)


        // recycler view params
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_friend_fragment)
        recyclerView.adapter = friendAdapter


        // search bar user
        val editTextSearchFriend = view?.findViewById<EditText>(R.id.search_friend_bar)
        editTextSearchFriend?.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    friendAdapter.clear()
                    fetchUsers()
                } else {
                    friendAdapter.clear()
                    val text = it.toString()
                    val searchText = text.lowercase()
                    userSearch(searchText)

                }
            }
        }

        // Inflate the layout for this fragment
        return view

    }

    private fun userSearch(text: String) {

        db.getReference("/users").orderByChild("username").equalTo(text)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val getAllUser = it.getValue<User>()
                        friendAdapter.clear()
                        friendAdapter.add(FriendFragmentAdapter(getAllUser!!))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }


    // get friends from database and add it on the recycler view
    private fun fetchUsers() {
        val auth = Firebase.auth
        val ref = db.getReference("/users/${auth.uid}/friends").orderByChild("username")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val collectedUser = it.getValue<UserFriend>()
                    val collectedUid = collectedUser?.uid

                    db.getReference("users/$collectedUid/").get()
                        .addOnSuccessListener { it1 ->
                            val user = it1.getValue(User::class.java)
                            friendAdapter.add(FriendFragmentAdapter(user!!))
                            friendAdapter.setOnItemClickListener { item, view ->
                                val intent = Intent(view.context, ChatActivity::class.java)
                                val row = item as FriendFragmentAdapter
                                intent.putExtra(NewConversationActivity.USER_KEY, row.collectedUser)
                                startActivity(intent)
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}

class FriendFragmentAdapter(val collectedUser: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val view = viewHolder.itemView

        val avatar = view.findViewById<ImageView>(R.id.avatar_friend_adapter)
        Glide.with(view).load(collectedUser.avatar).into(avatar)
        view.findViewById<TextView>(R.id.friend_adapter_username).text = collectedUser.username

        // start conversation btn
        val startConvBtn = view.findViewById<ImageView>(R.id.start_conversation_btn_friend_adapter)
        startConvBtn.setOnClickListener {
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(NewConversationActivity.USER_KEY, collectedUser)
            view.context.startActivity(intent)
        }

        // add btn
        val addBtn = view.findViewById<ImageView>(R.id.add_btn_friend_adapter)
            addBtn.setOnClickListener {
            if (collectedUser.uid != currentUser?.uid) {
                addFriend(collectedUser.uid, collectedUser.username)
            } else {
                Toast.makeText(
                    view.context, "Tu ne peux pas t'ajouter toi même ><",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        db.getReference("users").child("${currentUser?.uid}")
            .child("friends").child(collectedUser.uid).get().addOnSuccessListener {
            if (it.exists()){
                addBtn.visibility = View.INVISIBLE
            } else {
                addBtn.visibility = View.VISIBLE
            }

        }

    }

    override fun getLayout(): Int {
        return R.layout.adapter_friend_fragment
    }


    // add friend fun
    private fun addFriend(userUid: String, userUsername: String) {
        val currentUser = currentUser
        val friendRef = db.getReference("/users/${currentUser?.uid}/friends/${userUid}")
        val user = UserFriend(userUsername, userUid)
        friendRef.push()
        friendRef.setValue(user)

    }

}

class SearchFriendFragmentAdapter(private val collectedUser: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val avatar = viewHolder.itemView.findViewById<ImageView>(R.id.avatar_friend_adapter)
        Glide.with(viewHolder.itemView).load(collectedUser.avatar).into(avatar)
        viewHolder.itemView.findViewById<TextView>(R.id.friend_adapter_username).text = collectedUser.username

    }

    override fun getLayout(): Int {
        return R.layout.adapter_search_friend_fragment
    }
}

