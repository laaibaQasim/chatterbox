package com.example.chatterbox

import android.app.Activity
import com.example.chatterbox.model.User
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import androidx.activity.result.contract.ActivityResultContracts
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class RegisterActivity : AppCompatActivity() {
    private var selectedPhotoUri: Uri? = null
    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            handleImageSelection(data)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val usernameEditText : EditText = findViewById(R.id.username)
        val goToSignInPage:TextView = findViewById(R.id.already_haa)
        val registerBtn : Button = findViewById(R.id.register_btn)
        val selectPhotoBtn : Button = findViewById(R.id.select_photo_btn)

        registerBtn.setOnClickListener {
            val emailEditText : EditText = findViewById(R.id.email)
            val passwordEditText : EditText = findViewById(R.id.password)
            performRegister(emailEditText,passwordEditText)
        }

        goToSignInPage.setOnClickListener{
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        selectPhotoBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            someActivityResultLauncher.launch(intent)
        }
    }
    private fun handleImageSelection(data: Intent?) {
        if (data != null) {
            Log.d(TAG, "Photo was selected")
            val uri = data.data
            selectedPhotoUri = uri
            Log.d("MainActivity","setting uri: $selectedPhotoUri")
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val selectPhotoBtn : Button = findViewById(R.id.select_photo_btn)
            val selectPhotoView: CircleImageView = findViewById(R.id.selectphoto_imageview_register)
            selectPhotoView.setImageBitmap(bitmap)
            selectPhotoBtn.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            selectPhotoBtn.background = bitmapDrawable
        }
    }
    private fun performRegister(emailEditText : EditText, passwordEditText: EditText ) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password: $password")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                uploadImageToFirebaseStorage()
                // else if successful
                Log.d("Main", "Successfully created user with uid: ${it.result.user?.uid}")
            }
            .addOnFailureListener{
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadImageToFirebaseStorage() {
        Log.d("MainActivity","selected photo uri: $selectedPhotoUri")
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        Log.d("MainActivity","File Name: $filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val usernameEditText:EditText = findViewById(R.id.username)
        val user = User(uid, usernameEditText.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }
}