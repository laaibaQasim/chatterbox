package com.example.chatterbox.model

import android.util.Log
import com.google.firebase.database.DataSnapshot

//import com.google.firebase.Timestamp
//

//data class ChatMessageModel(
//    var message: String? = null,
//    var senderId: String? = null,
//    var timestamp: Timestamp? = null
//
//
//)



//package com.example.chatterbox.model

//import com.google.firebase.Timestamp
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.GenericTypeIndicator

//import com.google.firebase.Timestamp
//import com.google.firebase.database.DataSnapshot
//
//data class ChatMessageModel(
//    var message: String? = null,
//    var senderId: String? = null,
//    var timestamp: Timestamp? = null
//) {
//    companion object {
//        fun fromSnapshot(snapshot: DataSnapshot): ChatMessageModel {
//            val data = snapshot.value as? Map<*, *> ?: emptyMap<String, Any>()
//
//            val message = data["message"] as? String ?: ""
//            val senderId = data["senderId"] as? String ?: ""
//            val timestamp = data["timestamp"]
//
//            // Check if timestamp is a Map, as it might be a String
//            val timestampMap = timestamp as? Map<*, *>
//            val seconds = timestampMap?.get("seconds") as? Long ?: 0
//            val nanoseconds = timestampMap?.get("nanoseconds") as? Int ?: 0
//
//            return ChatMessageModel(message, senderId, Timestamp(seconds, nanoseconds))
//        }
//    }
//}

//data class ChatMessageModel(
//    val message: String = "",
//    val sender: String = "",
//    val timestamp: Timestamp = Timestamp.now()
//) {
//    companion object {
//        fun fromSnapshot(snapshot: DataSnapshot): ChatMessageModel {
//            val message = snapshot.child("message").getValue(String::class.java) ?: ""
//            val sender = snapshot.child("sender").getValue(String::class.java) ?: ""
//
//            val timestampString: String = // obtain the timestamp as a string from Firebase
//            val timestamp = Timestamp(timestampString.toLong()) // convert to Timestamp
//            val chatMessage = ChatMessageModel(sender, timestamp, message)
//
//            val timestamp = if (timestampString.isNotEmpty()) Timestamp(
//                java.util.Date(
//                    timestampString.toLong()
//                )
//            ) else Timestamp.now()
//
//            return ChatMessageModel(message, sender, timestamp)
//        }
//    }
//}
//
//data class ChatMessageModel(
//    val message: String = "",
//    val sender: String = "",
//    val timestamp: Timestamp = Timestamp.now()
//) {
//    companion object {
//        fun fromSnapshot(snapshot: DataSnapshot): ChatMessageModel {
//            val message = snapshot.child("message").getValue(String::class.java) ?: ""
//            val sender = snapshot.child("sender").getValue(String::class.java) ?: ""
//
//            val timestampString: String = snapshot.child("timestamp").getValue(String::class.java) ?: ""
//            val timestamp = if (timestampString.isNotEmpty()) {
//                Timestamp(java.util.Date(timestampString.toLong()))
//            } else {
//                Timestamp.now()
//            }
//
//            return ChatMessageModel(message, sender, timestamp)
//        }
//    }
//}

//data class ChatMessageModel(
//    val message: String = "",
//    val senderId: String = "",
//    val timestamp: Timestamp = Timestamp(0, 0)
//) {
//    companion object {
//        fun fromSnapshot(snapshot: DataSnapshot): ChatMessageModel {
//            val message = snapshot.child("message").getValue(String::class.java) ?: ""
//            val senderId = snapshot.child("senderId").getValue(String::class.java) ?: ""
//
//            val timestampSeconds = snapshot.child("timestamp").child("seconds").getValue(Long::class.java) ?: 0
//            val timestampNanoseconds = snapshot.child("timestamp").child("nanoseconds").getValue(Int::class.java) ?: 0
//
//            val timestamp = Timestamp(timestampSeconds, timestampNanoseconds)
//
//            return ChatMessageModel(message, senderId, timestamp)
//        }
//    }
//}

//data class ChatMessageModel(
//    val message: String = "",
//    val senderId: String = "",
//    val timestamp: Timestamp = Timestamp(0, 0)
//) {
//    companion object {
//        fun fromSnapshot(snapshot: DataSnapshot): ChatMessageModel {
//            val message = snapshot.child("message").getValue(String::class.java) ?: ""
//            val senderId = snapshot.child("senderId").getValue(String::class.java) ?: ""
//
//            val timestampSeconds = snapshot.child("timestamp").child("seconds").getValue(Long::class.java) ?: 0
//            val timestampNanoseconds = snapshot.child("timestamp").child("nanoseconds").getValue(Int::class.java) ?: 0
//
//            val timestamp = Timestamp(timestampSeconds, timestampNanoseconds)
//
//            return ChatMessageModel(message, senderId, timestamp)
//        }
//    }
//}
//
//


//data class ChatMessageModel(
//    val message: String = "",
//    val senderId: String = "",
//    val timestamp: Timestamp = Timestamp(0, 0)
//) {
//    constructor() : this("", "", Timestamp.now())
//    companion object {
//        fun fromSnapshot(snapshot: DataSnapshot): ChatMessageModel {
//            val message = snapshot.child("message").getValue(String::class.java) ?: ""
//            val senderId = snapshot.child("senderId").getValue(String::class.java) ?: ""
//
//            val timestampSeconds = snapshot.child("timestamp").child("seconds").getValue(Long::class.java) ?: 0
//            val timestampNanoseconds = snapshot.child("timestamp").child("nanoseconds").getValue(Int::class.java) ?: 0
//
//            val timestamp = Timestamp(timestampSeconds, timestampNanoseconds)
//
//            return ChatMessageModel(message, senderId, timestamp)
//        }
//    }
//}


import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatMessageModel(
    var message: String? = null,
    var senderId: String? = null,
    var timestamp: Long? = null
) {
    fun fromSnapshot(snapshot: DataSnapshot): ChatMessageModel {
        val message = snapshot.child("message").getValue(String::class.java) ?: ""
        val senderId = snapshot.child("senderId").getValue(String::class.java) ?: ""
        val timestamp = snapshot.child("timestamp").getValue(Long::class.java) ?: 0

        return ChatMessageModel(message, senderId, timestamp)
    }

    companion object {
        fun fromMap(map: Map<String, Any>?): ChatMessageModel? {
            if (map == null) {
                Log.e("ChatMessageModel", "Error: Null data received.")
                return null
            }

            try {
                val messageModel = ChatMessageModel()

                for ((key, value) in map) {
                    when (key) {
                        "message" -> messageModel.message = value as? String
                        "senderId" -> messageModel.senderId = value as? String
                        "timestamp" -> {
                            messageModel.timestamp = value as? Long
                        }
                    }
                }

                // Log the received data and the model
                Log.d("ChatMessageModel", "Received data: $map")
                Log.d("messageee", "Received data: ${messageModel.message}")
                Log.d("ChatMessageModel", "Converted model: $messageModel")

                if (messageModel.message == null || messageModel.senderId == null || messageModel.timestamp == null) {
                    Log.e("ChatMessageModel", "Error: Incomplete data. Message model: $messageModel")
                    return null
                }

                return messageModel
            } catch (e: Exception) {
                // Log the exception
                Log.e("ChatMessageModel", "Error converting data to ChatMessageModel: $e")
                return null
            }
        }
    }

    // Getter methods for Firebase compatibility
    @JvmName("getSenderIdNullable")
    fun getSenderId(): String? {
        return senderId
    }

    @JvmName("getMessageNullable")
    fun getMessage(): String? {
        return message
    }

    @JvmName("getTimestampNullable")
    fun getTimestamp(): Long? {
        return timestamp
    }
}


