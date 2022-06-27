package com.chatapp.mainActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chatapp.R
import com.chatapp.UserProfileActivity
import com.chatapp.account.AccountMainActivity
import com.chatapp.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val db =
            Firebase.database("https://chat-app-84489-default-rtdb.europe-west1.firebasedatabase.app")
    }

    val tag = "TagMainActivity"
    private val auth = Firebase.auth


    override fun onStart() {
        super.onStart()
        checkIfUserIsConnected()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call functions
        fetchUser()


        // NavBar Bottom Settings
        val mainFragment = MainFragment()
        val searchFriendFragment = FriendFragment()
        val settingsFragment = SettingsFragment()
        loadFragment(mainFragment)
        val navbarBottom = findViewById<BottomNavigationView>(R.id.bottom_nav_bar_main_activity)
        navbarBottom.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navbar_main_activity_chat_main -> loadFragment(mainFragment)
                R.id.navbar_main_activity_search_friend -> loadFragment(searchFriendFragment)
                R.id.navbar_main_activity_settings -> loadFragment(settingsFragment)
            }
            true
        }


        // Toolbar settings
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMain)
        toolbar.inflateMenu(R.menu.nav_menu)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.user_profile_menu ->
                    startActivity(Intent(applicationContext, UserProfileActivity::class.java))

            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun loadFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }




    // Fetch user information
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


    // If user is not connected with Firebase, force go to AccountMainActivity
    private fun checkIfUserIsConnected() {
        val currentUser = auth.currentUser
        // If the user is not connected, force login activity
        if (currentUser == null) {
            val intent =
                Intent(applicationContext, AccountMainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}




