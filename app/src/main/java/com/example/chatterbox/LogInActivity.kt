package com.example.chatterbox

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val backToSignUp: TextView = findViewById(R.id.btr)
        val loginBtn: Button = findViewById(R.id.login_btn)
        val emailEditText: EditText = findViewById(R.id.login_email)
        val passwordEditText: EditText = findViewById(R.id.login_password)

        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            Toast.makeText(
                                this,
                                "Authentication successful. Don't try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(
                                this,
                                "Authentication failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this,
                            "Authentication failed. Please check your credentials.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        backToSignUp.setOnClickListener {
            finish()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
