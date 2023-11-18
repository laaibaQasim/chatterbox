package com.example.chatterbox

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.adapter.SearchUserRecyclerViewAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatterbox.model.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchUserActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchUserRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        val searchButton: ImageButton = findViewById(R.id.search_user_btn)
        val searchInput: EditText = findViewById(R.id.seach_username_input)
        recyclerView = findViewById(R.id.search_user_recycler_view)
        adapter = SearchUserRecyclerViewAdapter(this, mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchUsers()

        searchButton.setOnClickListener {
            searchInput.requestFocus()
            var searchName:String = searchInput.text.toString()
            if (searchName.isEmpty())
            {
                searchInput.error = "Invalid Username"
            }
            else{
                Log.d("SearchUserActivity", "Search button clicked. Searching for: $searchName")
                adapter.filterByName(searchName)
            }
        }
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()

                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    user?.let { userList.add(it) }
                }

                // Update the adapter's data
                adapter.updateData(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("YourActivity", "Failed to read value.", error.toException())
            }
        })
    }
}
