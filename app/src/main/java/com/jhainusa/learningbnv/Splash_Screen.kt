package com.jhainusa.learningbnv

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Splash_Screen : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({

            val currentUser : FirebaseUser?=auth.currentUser
            if(currentUser !=null){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivity(Intent(this,NewJoinee_page::class.java))
            }
               finish()
                                                    },2000)

    }
     private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        auth=FirebaseAuth.getInstance()


    }
}