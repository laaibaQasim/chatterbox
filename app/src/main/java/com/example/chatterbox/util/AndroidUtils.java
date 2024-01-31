package com.example.chatterbox.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatterbox.R;
import com.example.chatterbox.model.User;

public class AndroidUtils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void passUserAsIntent(Intent intent, User user) {
        intent.putExtra("uid", user.getUid());
        intent.putExtra("username", user.getUsername());
        intent.putExtra("profileImageUrl", user.getProfileImageUrl());
    }
    public static User getUserFromIntent(Intent intent) {
        String uid = intent.getStringExtra("uid");
        String username = intent.getStringExtra("username");
        String profileImageUrl = intent.getStringExtra("profileImageUrl");

        return new User(uid, username, profileImageUrl);
    }

//    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView) {
//        Glide.with(context)
//                .load(imageUri)
//                .apply(RequestOptions.circleCropTransform())
//                .into(imageView);
//    }
public static void setProfilePic(Context context, Uri imageUri, ImageView imageView) {
    Glide.with(context)
            .load(imageUri)
            .apply(RequestOptions.circleCropTransform())
            .error(R.drawable.person_icon) // Provide a default image in case of error
            .into(imageView);
}


}
