package com.example.chatterbox.model

class User(val uid: String, val username: String, val profileImageUrl: String) {
    constructor():this("","","")

    fun getUserId(): String {
        return uid
    }
}