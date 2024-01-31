//package com.example.chatterbox.util
//
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.firestore.CollectionReference
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//
//import com.google.firebase.storage.StorageReference
//import java.text.SimpleDateFormat
//
//
//object FirebaseUtils {
//
//    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
//    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
//
//    fun currentUserId(): String? {
//        return FirebaseAuth.getInstance().uid
//    }
//
//    fun isLoggedIn(): Boolean {
//        return auth.currentUser != null
//    }
//
//    fun getCurrentUser(): FirebaseUser? {
//        return auth.currentUser
//    }
//    fun allUserCollectionReference(): CollectionReference? {
//        return FirebaseFirestore.getInstance().collection("users")
//    }
//    fun getOtherUserFromChatroom(userIds: List<String?>?): DocumentReference? {
//        return if (userIds?.get(0) == FirebaseUtils.currentUserId()) {
//            userIds?.get(1)?.let { allUserCollectionReference()!!.document(it) }
//        } else {
//            userIds?.get(0)?.let { allUserCollectionReference()!!.document(it) }
//        }
//    }
//    fun getOtherProfilePicStorageRef(otherUserId: String?): StorageReference? {
//        return FirebaseStorage.getInstance().reference.child("profile_pic")
//            .child(otherUserId!!)
//    }
//    fun timestampToString(timestamp: Long?): String? {
//        if (timestamp != null) {
//
//            var a=SimpleDateFormat("HH:MM").format(timestamp.toDate())
//        }
//    }
//
//    fun getChatroomReference(chatroomId: String): DatabaseReference {
//        return database.reference.child("chatrooms").child(chatroomId)
//    }
//    fun getChatroomReferenceRecycler(chatroomId: String): DatabaseReference {
//        return database.reference.child("chatrooms/chatrooms").child(chatroomId)
//    }
//
//    fun getChatroomId(userId1: String, userId2: String): String {
//        return if (userId1.hashCode() < userId2.hashCode()) {
//            "$userId1+$userId2"
//        } else {
//            "$userId2+$userId1"
//        }
//    }
//}
package com.example.chatterbox.util

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

object FirebaseUtils {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    fun getUserReference(userId: String): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("users").child(userId)
    }

    fun allUserCollectionReference(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection("users")
    }
    fun allUserCollectionReference2(): DatabaseReference? {
        return FirebaseDatabase.getInstance().reference.child("chatrooms")
    }


    fun getOtherUserFromChatroom(userIds: List<String?>?): DocumentReference? {
        return if (userIds?.get(0) == currentUserId()) {
            userIds?.get(1)?.let { allUserCollectionReference()?.document(it) }
        } else {
            userIds?.get(0)?.let { allUserCollectionReference()?.document(it) }
        }
    }

//    fun getOtherProfilePicStorageRef(otherUserId: String?): StorageReference? {
//        return FirebaseStorage.getInstance().reference.child("profile_pic")
//            .child(otherUserId!!)
//    }
fun getOtherProfilePicStorageRef(otherUserId: String?): StorageReference? {
    return otherUserId?.let {
        FirebaseStorage.getInstance().reference.child("users/$it/profileImageUrl")
    }
}
    fun getDownloadUrl(storageReference: StorageReference): Task<Uri> {
        // Use getDownloadUrl to fetch a new URL with an unexpired token
        return storageReference.downloadUrl
    }


    fun timestampToString(timestamp: Long?): String? {
        return timestamp?.let {
            val date = Date(it)
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        }
    }

    fun getChatroomReference(chatroomId: String): DatabaseReference {
        return database.reference.child("chatrooms").child(chatroomId)
    }

    fun getChatroomReferenceRecycler(chatroomId: String): DatabaseReference {
        return database.reference.child("chatrooms/chatrooms").child(chatroomId)
    }

    fun getChatroomId(userId1: String, userId2: String): String {
        return if (userId1.hashCode() < userId2.hashCode()) {
            "$userId1+$userId2"
        } else {
            "$userId2+$userId1"
        }
    }

}
