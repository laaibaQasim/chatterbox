package com.example.chatterbox.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.R
import com.example.chatterbox.model.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class SearchUserRecyclerViewAdapter(private val context: Context, private var userList: List<User>) :
    RecyclerView.Adapter<SearchUserRecyclerViewAdapter.MyViewHolder>() {

    // Other adapter methods...

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.search_user_recycler_row,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("RecyclerView", "onBindView holder se ${holder.userName.text} bolrha hun")
        val user = userList[position]
        holder.userName.text = user.username
        Picasso.get().load(user.profileImageUrl).into(holder.userProfile)

        // Handle item click (if needed)
        holder.itemView.setOnClickListener {
            Log.d("RecyclerView", "${holder.userName.text}")
            // Implement your click handling logic here
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }

    fun filterByName(name: String) {
        val filteredList = userList.filter { it.username.contains(name, ignoreCase = true) }
        updateData(filteredList)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userProfile:CircleImageView = itemView.findViewById(R.id.user_dp)
    }

}