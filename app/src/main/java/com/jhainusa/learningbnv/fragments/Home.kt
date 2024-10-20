package com.jhainusa.learningbnv.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jhainusa.learningbnv.R
import com.jhainusa.learningbnv.databinding.FragmentHomeBinding


class Home : Fragment() {
    lateinit var database: DatabaseReference
    lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: MyAdapter
    private lateinit var postList: MutableList<Post>
    lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)
         recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postList= mutableListOf()
        postAdapter=MyAdapter(postList)
        recyclerView.adapter=postAdapter
        database = FirebaseDatabase.getInstance().getReference("Posts")
        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for(i in snapshot.children){
                    val post=i.getValue(Post::class.java)
                    if(post!=null){
                        postList.add(post)
                    }
                }
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load posts", Toast.LENGTH_SHORT).show()
            }
        })


        return binding.root
    }

}