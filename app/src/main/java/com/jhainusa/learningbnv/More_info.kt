package com.jhainusa.learningbnv

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Gallery
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.jhainusa.learningbnv.databinding.ActivityMoreInfoBinding
import java.io.File
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class More_info : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance() // Firestore instance
    val storageRef = FirebaseStorage.getInstance().reference // Storage reference
    val binding: ActivityMoreInfoBinding by lazy {
        ActivityMoreInfoBinding.inflate(layoutInflater)
    }
    lateinit var profimg:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.addAPhoto.setOnClickListener {
           pickImageFromGallery()
        }
        binding.btnSubmit.setOnClickListener {
            val prof=binding.proffesion.text.toString()
            val location=binding.locationInput.text.toString()
            val skills=binding.universityInput.text.toString()
            if(prof.isNotEmpty() && location.isNotEmpty() && skills.isNotEmpty() && profimg!=null){
                uploadImageToFirebaseStorage(profimg)
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this,"Please Fill all the details",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun pickImageFromGallery(){
        val intent=Intent()
        intent.action=Intent.ACTION_PICK
        intent.type="image/*"
        startActivityForResult(intent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1000 && resultCode == RESULT_OK && data!=null){
            val imageUri=data.data
            profimg= imageUri!!
            binding.userphoto.setImageURI(profimg)
        }
    }
    interface ImageUploadCallback {
        fun onImageUploaded(imageUrl: String)
        fun onUploadFailed(errorMessage: String)
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri?){
        if(imageUri!=null){
            val profileImage = storageRef.child("profileImages/${FirebaseAuth.getInstance().currentUser?.uid}.jpg")
            profileImage.putFile(imageUri)
                .addOnSuccessListener {
                    profileImage.downloadUrl.addOnSuccessListener {it->
                        val imageUrl=it.toString()
                        val prof=binding.proffesion.text.toString()
                        val location=binding.locationInput.text.toString()
                        val skills=binding.universityInput.text.toString()
                        CoroutineScope(Dispatchers.IO).launch {
                        saveProfileToFirestore(imageUrl,prof,location,skills)
                        }
                    }
                        .addOnFailureListener {
                            Toast.makeText(this,"Failed to upload image : ${it.message}",Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }
    private suspend fun saveProfileToFirestore(imageUrl: String,prof:String,location:String,skills:String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val intent=intent
        val fname=intent.getStringExtra("fname")
        val lname=intent.getStringExtra("lname")
        val email=intent.getStringExtra("email")
        val pass=intent.getStringExtra("pass")

        val userProfile= hashMapOf(
            "profileImageUrl" to imageUrl,
            "firstname" to fname,
            "lastname" to lname,
            "email" to email,
            "pass" to pass,
            "prof" to prof,
            "location" to location,
            "skills" to skills
        )
        db.collection("Users").document(userId!!)
            .set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

}