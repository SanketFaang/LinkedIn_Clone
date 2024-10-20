package com.jhainusa.learningbnv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jhainusa.learningbnv.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadUserprofile()
        loadUserprofile()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.imageButton.setOnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
        }
        var nav = findNavController(R.id.fragmentContainerView3)
        var btm = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        btm.setupWithNavController(nav)
    }

    private fun loadUserprofile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("MESSAGEs",FirebaseAuth.getInstance().currentUser?.uid.toString())
        db.collection("Users").document(userId!!)
            .get()
            .addOnSuccessListener {
                val imgUrl = it.getString("profileImageUrl")
                if (!imgUrl.isNullOrEmpty()) {
                    Picasso.get().load(imgUrl).resize(100,100).into(binding.circleImageView)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }
}