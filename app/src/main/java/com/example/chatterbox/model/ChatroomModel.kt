package com.example.chatterbox.model

import android.util.Log

data class ChatroomModel(
    var chatroomId: String? = null,
    var userIds: List<String?>? = null,
    var lastMessageTimestamp: Long? = null,
    var lastMessageSenderId: String? = null,
    var lastMessage: String? = null
) {
    companion object {
        fun fromMap(map: Map<String, Any>?): ChatroomModel {
            val chatroomModel = ChatroomModel()

            if (map != null) {
                try {
                    chatroomModel.chatroomId = map.getOrDefault("chatroomId", "") as String
                    chatroomModel.userIds = (map.getOrDefault("userIds", emptyList<Any>()) as List<*>)
                        .filterIsInstance<String>()

                    // Handle lastMessageTimestamp conversion
                    chatroomModel.lastMessageTimestamp = map.getOrDefault("lastMessageTimestamp", 0L) as Long

                    chatroomModel.lastMessageSenderId = map.getOrDefault("lastMessageSenderId", "") as String
                    chatroomModel.lastMessage = map.getOrDefault("lastMessage", "") as String

                    // Log the converted model
                    Log.d("ChatroomModel", "Converted model: $chatroomModel")
                } catch (e: Exception) {
                    // Log the exception
                    Log.e("ChatroomModel", "Error converting data to ChatroomModel: $e")
                }
            } else {
                Log.e("ChatroomModel", "Error: Null data received.")
            }

            return chatroomModel
        }
    }
}

