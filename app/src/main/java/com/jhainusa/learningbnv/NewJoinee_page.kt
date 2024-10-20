package com.jhainusa.learningbnv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class NewJoinee_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_joinee_page)
        val btn=findViewById<Button>(R.id.btn_agree_join)
        val sign=findViewById<TextView>(R.id.sign_in_text)
        btn.setOnClickListener {
            startActivity(Intent(this,InfoEntering_Page::class.java))
        }
        sign.setOnClickListener {
            startActivity(Intent(this,SignIn::class.java))
        }
    }
}