package com.example.chatterbox.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseUtils {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
