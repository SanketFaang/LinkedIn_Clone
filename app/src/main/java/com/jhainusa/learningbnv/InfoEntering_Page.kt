package com.jhainusa.learningbnv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.jhainusa.learningbnv.databinding.ActivityInfoEnteringPageBinding

class InfoEntering_Page : AppCompatActivity() {
    val binding:ActivityInfoEnteringPageBinding by lazy {
        ActivityInfoEnteringPageBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        binding.btnContinue.setOnClickListener {
            val fname = binding.firstNameInput.text.toString()
            val lname = binding.lastNameInput.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.pass.text.toString()
            if (fname.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this){
                        if(it.isSuccessful){
                            Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()
                            val editor = getSharedPreferences("USER_PROFILE", MODE_PRIVATE).edit()
                            editor.putString("fname",fname)
                            editor.putString("lname",lname)
                            editor.putString("email",email)
                            editor.putString("pass",pass)
                            editor.apply()
                            val intent=Intent(Intent(this,More_info::class.java))
                            intent.putExtra("fname",fname)
                            intent.putExtra("lname",lname)
                            intent.putExtra("email",email)
                            intent.putExtra("pass",pass)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,"Registration failed : ${it.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }
}