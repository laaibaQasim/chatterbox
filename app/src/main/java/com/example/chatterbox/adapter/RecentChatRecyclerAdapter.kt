package com.example.chatterbox

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.chatterbox.model.ChatroomModel
import com.example.chatterbox.model.User
import com.example.chatterbox.util.AndroidUtils
import com.example.chatterbox.util.FirebaseUtils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageException
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

//class RecentChatRecyclerAdapter(
//    options: FirebaseRecyclerOptions<ChatroomModel>,
//    private val context: Context
//) : FirebaseRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomModelViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false)
//        return ChatroomModelViewHolder(view)
//
//
//    }
//
//    override fun onBindViewHolder(
//        @NonNull holder: ChatroomModelViewHolder,
//        position: Int,
//        @NonNull model: ChatroomModel
//    ) {
//        FirebaseUtils.getOtherUserFromChatroom(model.userIds)
//            ?.get()?.addOnCompleteListener { task ->
//                if (task.isSuccessful()) {
//                    val lastMessageSentByMe =
//                        model.lastMessageSenderId == FirebaseUtils.currentUserId()
//                    val otherUserModel: User? = task.getResult().toObject(User::class.java)
//                    if (otherUserModel != null) {
//                        FirebaseUtils.getOtherProfilePicStorageRef(otherUserModel.getUserId())
//                            ?.getDownloadUrl()
//                            ?.addOnCompleteListener { t ->
//                                if (t.isSuccessful()) {
//                                    val uri: Uri = t.getResult()
//                                    AndroidUtils.setProfilePic(context, uri, holder.profilePic)
//                                }
//                            }
//                    }
//                    if (otherUserModel != null) {
//                        holder.usernameText.setText(otherUserModel.username)
//                    }
//                    if (lastMessageSentByMe) holder.lastMessageText.text =
//                        "You : " + model.lastMessage else holder.lastMessageText.text =
//                        model.lastMessage
//                    holder.lastMessageTime.setText(FirebaseUtils.timestampToString(model.lastMessageTimestamp))
//                    holder.itemView.setOnClickListener { v: View? ->
//                        //navigate to chat activity
//                        val intent = Intent(context, ChatActivity::class.java)
//                        AndroidUtils.passUserAsIntent(intent, otherUserModel)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        context.startActivity(intent)
//                    }
//                }
//                Log.d("Adapter", "onCreateViewHolder: $position")
//                Log.d("Adapter", "onBindViewHolder: $position - ${model.lastMessage}")
//
//            }
//    }
//    class ChatroomModelViewHolder(@NonNull itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        var usernameText: TextView
//        var lastMessageText: TextView
//        var lastMessageTime: TextView
//        var profilePic: ImageView
//
//        init {
//            usernameText = itemView.findViewById<TextView>(R.id.user_name_text)
//            lastMessageText = itemView.findViewById<TextView>(R.id.last_message_text)
//            lastMessageTime = itemView.findViewById<TextView>(R.id.last_message_time_text)
//            profilePic = itemView.findViewById<ImageView>(R.id.profile_pic_image_view)
//        }
//    }
//}


//class RecentChatRecyclerAdapter(
//    options: FirebaseRecyclerOptions<ChatroomModel>,
//    private val context: Context
//) : FirebaseRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomModelViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false)
//        return ChatroomModelViewHolder(view)
//    }
//
//
//
//override fun onBindViewHolder(
//    @NonNull holder: ChatroomModelViewHolder,
//    position: Int,
//    @NonNull model: ChatroomModel
//) {
//    val userIds = model.userIds
//    val otherUserId = userIds?.find { it != FirebaseUtils.currentUserId() }
//
//    if (otherUserId != null) {
//        FirebaseUtils.getUserReference(otherUserId)
//            ?.addListenerForSingleValueEvent(object : ValueEventListener {
//
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        val otherUserModel: User? = dataSnapshot.getValue(User::class.java)
//                        if (otherUserModel != null) {
//                            val storageRef = FirebaseUtils.getOtherProfilePicStorageRef(otherUserModel.getUserId())
//                            if (storageRef != null) {
//                                Log.d("Storage", "${storageRef.path}")
//                            }
//                            if (storageRef != null) {
//                                FirebaseUtils.getDownloadUrl(storageRef)
//                                    .addOnCompleteListener { task ->
//                                        if (task.isSuccessful) {
//                                            val uri: Uri = task.result!!
//                                            AndroidUtils.setProfilePic(context, uri, holder.profilePic)
//                                            Log.d("ProfilePic", "Profile picture set successfully")
//                                        } else {
//                                            val exception = task.exception
//                                            if (exception is StorageException && exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
//                                                // Handle 404 error (Object not found)
//                                                Log.e("ProfilePic", "Profile picture not found. Handle this case appropriately.")
//
//                                                // Set a default image or take appropriate action
//                                                holder.profilePic.setImageResource(R.drawable.person_icon)
//                                            } else {
//                                                // Handle other errors
//                                                Log.e("ProfilePic", "Failed to retrieve profile picture download URL", exception)
//                                            }
//                                        }
//                                    }
//
//                                holder.usernameText.text = otherUserModel.username
//                                holder.itemView.setOnClickListener { v: View? ->
//                                    // navigate to chat activity
//                                    val intent = Intent(context, ChatActivity::class.java)
//                                    AndroidUtils.passUserAsIntent(intent, otherUserModel)
//                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                                    context.startActivity(intent)
//                                }
//                            }
//                        }
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Handle error
//                }
//            })
//    }
//
//    val lastMessageSentByMe = model.lastMessageSenderId == FirebaseUtils.currentUserId()
//    if (lastMessageSentByMe) holder.lastMessageText.text = "You : " + model.lastMessage
//    else holder.lastMessageText.text = model.lastMessage
//
//    holder.lastMessageTime.text = FirebaseUtils.timestampToString(model.lastMessageTimestamp)
//
//    Log.d("Adapter", "onBindViewHolder: $position - ${model.lastMessage}")
//}
//
//
//    class ChatroomModelViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var usernameText: TextView = itemView.findViewById(R.id.user_name_text)
//        var lastMessageText: TextView = itemView.findViewById(R.id.last_message_text)
//        var lastMessageTime: TextView = itemView.findViewById(R.id.last_message_time_text)
//        var profilePic: ImageView = itemView.findViewById(R.id.profile_pic_image_view)
//        init {
//            // Add a placeholder image or handle the default image here if needed
//            profilePic.setImageResource(R.drawable.person_icon)
//        }
//    }
//}
class RecentChatRecyclerAdapter(
    options: FirebaseRecyclerOptions<ChatroomModel>,
    private val context: Context
) : FirebaseRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomModelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false)
        return ChatroomModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatroomModelViewHolder, position: Int, model: ChatroomModel) {
        val userIds = model.userIds
        val otherUserId = userIds?.find { it != FirebaseUtils.currentUserId() }

        if (otherUserId != null) {
            FirebaseUtils.getUserReference(otherUserId)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val otherUserModel: User? = dataSnapshot.getValue(User::class.java)
                            if (otherUserModel != null) {
                                val imageUrl = otherUserModel.profileImageUrl

                                // Use Picasso to load and display the image
                                Picasso.get().load(imageUrl).into(holder.profilePic)

                                holder.usernameText.text = otherUserModel.username
                                holder.itemView.setOnClickListener { v: View? ->
                                    // navigate to chat activity
                                    val intent = Intent(context, ChatActivity::class.java)
                                    AndroidUtils.passUserAsIntent(intent, otherUserModel)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
        }

        val lastMessageSentByMe = model.lastMessageSenderId == FirebaseUtils.currentUserId()
        if (lastMessageSentByMe) holder.lastMessageText.text = "You : " + model.lastMessage
        else holder.lastMessageText.text = model.lastMessage

        holder.lastMessageTime.text = FirebaseUtils.timestampToString(model.lastMessageTimestamp)

        Log.d("Adapter", "onBindViewHolder: $position - ${model.lastMessage}")
    }

    class ChatroomModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameText: TextView = itemView.findViewById(R.id.user_name_text)
        var lastMessageText: TextView = itemView.findViewById(R.id.last_message_text)
        var lastMessageTime: TextView = itemView.findViewById(R.id.last_message_time_text)
        var profilePic: ImageView = itemView.findViewById(R.id.profile_pic_image_view)

//        init {
//            // Add a placeholder image or handle the default image here if needed
//            profilePic.setImageResource(R.drawable.person_icon)
//
//            // Make the profile picture round
//            val radius =  profilePic.context.resources.getDimension(R.dimen.rounded_image_radius)
//            val shape = ShapeDrawable(OvalShape())
//            shape.paint.color = Color.TRANSPARENT
//            shape.paint.strokeWidth = 5f
//            shape.paint.style = Paint.Style.FILL
//            profilePic.background = shape
//            profilePic.clipToOutline = true
//        }
init {
    // Add a placeholder image or handle the default image here if needed
    profilePic.setImageResource(R.drawable.person_icon)

    // Make the profile picture a full circle
    profilePic.background = ContextCompat.getDrawable(profilePic.context, R.drawable.circle_background)
    profilePic.clipToOutline = true
}

    }
}
