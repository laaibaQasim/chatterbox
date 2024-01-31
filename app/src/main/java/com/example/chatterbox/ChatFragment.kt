package com.example.chatterbox

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.model.ChatroomModel
import com.example.chatterbox.util.FirebaseUtils
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.ObservableSnapshotArray
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.Query

class ChatFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecentChatRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = view.findViewById(R.id.recyler_view)
        setupRecyclerView()
        return view
    }

    @SuppressLint("RestrictedApi")
    private fun setupRecyclerView() {
        val query = FirebaseUtils.allUserCollectionReference2()?.orderByKey()

        val options = query?.let {
            FirebaseRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(it, ChatroomModel::class.java)
                .build()
        }

        adapter = options?.let { RecentChatRecyclerAdapter(it, requireContext()) }
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
        adapter?.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }
}


