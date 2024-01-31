package com.example.chatterbox.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.R
import com.example.chatterbox.model.ChatMessageModel
import com.example.chatterbox.util.FirebaseUtils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ChatRecyclerAdapter(
    options: FirebaseRecyclerOptions<ChatMessageModel>,
    private val context: Context
) : FirebaseRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder>(options) {

    override fun onBindViewHolder(holder: ChatModelViewHolder, position: Int, model: ChatMessageModel) {
        Log.d("ChatAdapter", "onBindViewHolder - Position: " + position + ", Model: " + model.toString());

        if (model.senderId == FirebaseUtils.currentUserId()) {
            holder.leftChatLayout.visibility = View.GONE
            holder.rightChatLayout.visibility = View.VISIBLE
            holder.rightChatTextview.text = model.message
        } else {
            holder.rightChatLayout.visibility = View.GONE
            holder.leftChatLayout.visibility = View.VISIBLE
            holder.leftChatTextview.text = model.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatModelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false)
        return ChatModelViewHolder(view)
    }

    class ChatModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leftChatLayout: LinearLayout = itemView.findViewById(R.id.left_chat_layout)
        val rightChatLayout: LinearLayout = itemView.findViewById(R.id.right_chat_layout)
        val leftChatTextview: TextView = itemView.findViewById(R.id.left_chat_textview)
        val rightChatTextview: TextView = itemView.findViewById(R.id.right_chat_textview)
    }
}
