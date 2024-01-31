package com.example.chatterbox

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.adapter.ChatRecyclerAdapter
import com.example.chatterbox.model.ChatMessageModel
import com.example.chatterbox.model.ChatroomModel

import com.example.chatterbox.model.User
import com.example.chatterbox.util.AndroidUtils
import com.example.chatterbox.util.FirebaseUtils
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.sql.Timestamp


class ChatActivity : AppCompatActivity() {
    var otherUser: User? = null
    var messageInput: EditText? = null
    var sendMessageBtn: ImageButton? = null
    var backBtn: ImageButton? = null
    var otherUsername: TextView? = null
    var recyclerView: RecyclerView? = null
    var imageView: ImageView? = null
    var chatroomId: String? = null
    var chatroomModel: ChatroomModel? = null

    var adapter: ChatRecyclerAdapter? = null
    fun DataSnapshot.toChatroomModel(): ChatroomModel? {
        val map = value as? Map<String, Any> ?: return null
        return ChatroomModel.fromMap(map)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()

        otherUser = AndroidUtils.getUserFromIntent(intent)
        messageInput = findViewById(R.id.chat_message_input)
        sendMessageBtn = findViewById(R.id.message_send_btn)
        backBtn = findViewById(R.id.back_btn)
        otherUsername = findViewById(R.id.other_username)
        recyclerView = findViewById(R.id.chat_recycler_view)
        imageView = findViewById(R.id.profile_pic_image_view)

        chatroomId =
            FirebaseUtils.currentUserId()?.let { FirebaseUtils.getChatroomId(it, otherUser!!.getUserId()) }

        backBtn!!.setOnClickListener(View.OnClickListener { v: View? -> onBackPressed() })
        otherUsername!!.text = otherUser!!.username

        getOrCreateChatroomModel()
        setupChatRecyclerView()
        sendMessageBtn!!.setOnClickListener { v ->
            val message = messageInput!!.text.toString().trim()
            if (message.isEmpty()) return@setOnClickListener
            sendMessageToUser(message)
        }
    }

private fun sendMessageToUser(message: String) {
    chatroomModel?.apply {
        // Set the current user as the lastMessageSenderId
        lastMessageSenderId = FirebaseUtils.currentUserId().toString()

        // Create a new message ID
        val messageId = FirebaseUtils.getChatroomReference(chatroomId ?: "").child("chats").push().key

        if (messageId != null) {
            // Build the path for the new message
            val messagePath = "chatrooms/$chatroomId/chats/$messageId"
            val messageRef = FirebaseUtils.getChatroomReference(messagePath)

            // Set the current message as the lastMessage (as a string)
            lastMessage = message

            // Add timestamp and senderId to the message
            val timestamp = System.currentTimeMillis()
            val senderId = FirebaseUtils.currentUserId() ?: ""

            // Update the chatroom model in the database
            FirebaseUtils.getChatroomReference(chatroomId ?: "").setValue(this)

            // Set the new message in the "chats" node under the chatroom
            messageRef.setValue(
                mapOf(
                    "message" to message,
                    "senderId" to senderId,
                    "timestamp" to timestamp
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Clear the message input field after sending
                    messageInput?.setText("")
                } else {
                    Log.e("ChatActivity", "Error sending message: ${task.exception}")
                }
            }
        }
    }
}
    @SuppressLint("RestrictedApi")
    private fun setupChatRecyclerView() {
        val chatroomReference = chatroomId?.let {
            FirebaseUtils.getChatroomReferenceRecycler(it)
                .child("chats")
        } ?: return

        val query = chatroomReference.orderByChild("timestamp")
        val options = FirebaseRecyclerOptions.Builder<ChatMessageModel>()
            .setQuery(query, ChatMessageModel::class.java)
            .build()

        adapter = ChatRecyclerAdapter(options, this@ChatActivity)

        val manager = LinearLayoutManager(this@ChatActivity)
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = adapter

        // Add a ChildEventListener to capture real-time updates
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle new chat messages
                val chatMessage = snapshot.getValue(ChatMessageModel::class.java)
                Log.d("ChatActivity", "New chat message: $chatMessage")

                // Log additional details
                val recordCount = snapshot.childrenCount
                Log.d("ChatActivity", "Number of records in the snapshot: $recordCount")
                Log.d("ChatActivity", "Timestamp of the added message: ${chatMessage?.timestamp}")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            // ... (other ChildEventListener methods)

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("ChatActivity", "Error querying database: ${error.message}")
            }
        })

        // Reverse the order of the displayed items
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val reversedList = dataSnapshot.children.reversed()
                // Now, you can use reversedList to manually display the messages in descending order
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e("ChatActivity", "Error querying database: ${databaseError.message}")
            }
        })
    }

    private fun getOrCreateChatroomModel() {
    chatroomId?.let { roomId ->
        val chatroomRef = FirebaseUtils.getChatroomReference(roomId)

        chatroomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    Log.d("ChatActivity", "DataSnapshot key: ${dataSnapshot.key}")
                    Log.d("ChatActivity", "DataSnapshot value: ${dataSnapshot.value}")

                    // Add more detailed logs about the dataSnapshot children
                    for (childSnapshot in dataSnapshot.children) {
                        Log.d("ChatActivity", "Child key: ${childSnapshot.key}")
                        Log.d("ChatActivity", "Child value: ${childSnapshot.value}")
                    }

                    // Check if chatroomModel is null
                    if (dataSnapshot.exists()) {
                        // If dataSnapshot exists, convert it to a ChatroomModel
                        val chatroomMap = dataSnapshot.value as Map<String, Any>
                        chatroomModel = ChatroomModel.fromMap(chatroomMap)
                        Log.d("Masla", "${chatroomModel}")
                    } else {
                        Log.d("ChatActivity", "Creating new ChatroomModel")

                        // Create a new ChatroomModel if it doesn't exist
                        chatroomModel = ChatroomModel(
                            roomId,
                            listOf(FirebaseUtils.currentUserId(), otherUser!!.getUserId()),
                            System.currentTimeMillis(),
                            ""
                        )

                        // Set the new ChatroomModel in the database
                        chatroomRef.setValue(chatroomModel)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("ChatActivity", "ChatroomModel creation successful")
                                } else {
                                    Log.e("ChatActivity", "Error creating ChatroomModel: ${task.exception}")
                                }
                            }
                    }

                    // Log the final chatroomModel
                    Log.d("ChatActivity", "Final chatroomModel: $chatroomModel")
                } catch (e: Exception) {
                    Log.e("ChatActivity", "Error in onDataChange: $e")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e("ChatActivity", "DatabaseError: $databaseError")
            }
        })
    }
}

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

}
