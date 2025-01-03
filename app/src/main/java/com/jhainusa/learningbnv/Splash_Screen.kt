package com.jhainusa.learningbnv

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Launch a coroutine to handle splash screen delay and authentication check
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) // Show splash screen for 2 seconds

            // Check if the user is signed in
            val currentUser: FirebaseUser? = withContext(Dispatchers.IO) { auth.currentUser }

            // Navigate to the appropriate activity based on authentication state
            if (currentUser != null) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreen, NewJoinee_page::class.java))
            }
            finish()
        }
    }
}
