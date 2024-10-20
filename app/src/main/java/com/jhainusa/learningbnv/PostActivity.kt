package com.jhainusa.learningbnv

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.jhainusa.learningbnv.databinding.ActivityPostBinding
import com.jhainusa.learningbnv.fragments.Post
import com.squareup.picasso.Picasso
import java.time.LocalDate

class PostActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPostBinding
    lateinit var imguri:Uri
    val userId= FirebaseAuth.getInstance().currentUser?.uid
    var db=FirebaseFirestore.getInstance()
    lateinit var database:DatabaseReference
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db.collection("Users").document(userId!!).get().addOnSuccessListener {
            Picasso.get().load(it.getString("profileImageUrl")).resize(100,100).into(binding.circleImageView2)
        }
        binding.imagefromgal.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent,1000)
        }
        binding.cancelbutton.setOnClickListener{
            finish()
        }
        binding.postingbutton.setOnClickListener {
            db.collection("Users").document(userId!!)
                .get()
                .addOnSuccessListener {
                    val name=it.getString("firstname")+""+it.getString("lastname")
                    val des=binding.shareyourT.text.toString()
                    val skills = it.getString("skills")
                    val time=LocalDate.now().toString()
                    val profimage=it.getString("profileImageUrl")
                    val ref = FirebaseStorage.getInstance().reference.child(
                        "photo/" + System.currentTimeMillis() + "." + gettype(imguri)
                    )
                    val uploadTask = ref.putFile(imguri)
                    uploadTask.addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            val user=Post(profimage,name,des,uri.toString(),skills,time)
                            database=FirebaseDatabase.getInstance().getReference("Posts")
                            database.child("${System.currentTimeMillis()}"+name).setValue(user).addOnSuccessListener {
                                Toast.makeText(this,"uploaded",Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to get download URL: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to upload file: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }




        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1000 && resultCode== RESULT_OK && data!=null){
            val imageUri:Uri?=data.data
            imguri= imageUri!!
            Toast.makeText(this,imageUri.toString(),Toast.LENGTH_SHORT).show()
            val imageView:ImageView=findViewById(R.id.imageView2)
            imageView.setImageURI(imageUri)
        }
    }

    private fun gettype(data: Uri): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val extension = MimeTypeMap.getFileExtensionFromUrl(data.toString())
        return mimeTypeMap.getMimeTypeFromExtension(extension)
    }
}
