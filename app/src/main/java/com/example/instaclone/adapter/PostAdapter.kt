package com.example.instaclone.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.databinding.PostRvBinding
import com.example.instaclone.models.Post
import com.example.instaclone.models.UserModel
import com.example.instaclone.utils.USER_NODE
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PostAdapter(var context: Context, var postList: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.MyHolder>(){

    inner class MyHolder(var binding: PostRvBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        try{
            Firebase.firestore.collection(USER_NODE).document(postList[position].uid).get().addOnSuccessListener {
                var user = it.toObject<UserModel>()
                Glide.with(context).load(user!!.image).placeholder(R.drawable.person)
                holder.binding.nameUser.text = user.name
            }
        } catch (e: Exception) {}

        Glide.with(context).load(postList[position].postUrl)
             .placeholder(R.drawable.loading).into(holder.binding.postImage)

        try {
            val text = TimeAgo.using(postList[position].time.toLong())
            holder.binding.time.text = text
        } catch (e: Exception){
            holder.binding.time.text = ""
        }

        holder.binding.caption.text = postList[position].caption

        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.liked)
        }

        // Sharing the link of the image.
        holder.binding.send.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, postList[position].postUrl)
            context.startActivity(intent)
        }
    }
}