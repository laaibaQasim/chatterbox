package com.example.chatterbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val initialFragment = ChatFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, initialFragment)
            .commit()

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation)
        val searchButton: ImageButton = findViewById(R.id.main_search_btn)

        searchButton.setOnClickListener{
            val intent = Intent(this, SearchUserActivity::class.java)
            startActivity(intent)
        }


        // Set the initial selection in the BottomNavigationView
        bottomNavigation.selectedItemId = R.id.menu_chat

        // Set a listener for the bottom navigation view
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            // Handle navigation item clicks here
            when (menuItem.itemId) {
                R.id.menu_chat -> {
                    replaceFragment(ChatFragment())
                    true
                }
                R.id.menu_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                // Add more cases for other menu items as needed
                else -> false
            }
        }
    }

    // Function to replace the current fragment in the FrameLayout
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, fragment)
            .commit()
    }
}