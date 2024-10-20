package com.jhainusa.learningbnv.fragments

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jhainusa.learningbnv.R
import com.squareup.picasso.Picasso

class MyAdapter(private var postArray: List<Post>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(postArray[position])
    }

    override fun getItemCount(): Int = postArray.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val des: TextView = itemView.findViewById(R.id.scrollView2)
        private val postimg: ImageView = itemView.findViewById(R.id.postimg)
        private val skills: TextView = itemView.findViewById(R.id.skills)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val profimg:ImageView=itemView.findViewById(R.id.profileimg)
        fun bind(post: Post) {
            // Use safe calls with default values to prevent null issues
            name.text = post.name ?: "No Name"
            des.text = post.des ?: "No Description"
            skills.text = post.skills ?: "No Skills"
            time.text = post.time ?: "No Time"
            Picasso.get().load(post.profimg).resize(100,100).into(profimg)
            Picasso.get().load(post.postimg).into(postimg)

            // Manage expansion of the description TextView
            des.setOnClickListener {
                val isExpanded = des.maxLines == 3
                des.maxLines = if (isExpanded) Int.MAX_VALUE else 3
            }
        }
    }
}
