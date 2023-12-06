package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.databinding.FollowRvBinding
import com.example.instaclone.models.UserModel

class FollowAdapter(var context: Context, var followList: ArrayList<UserModel>): RecyclerView.Adapter<FollowAdapter.VewHolder> () {

    inner class VewHolder(var binding: FollowRvBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VewHolder {
        var binding = FollowRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return VewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: VewHolder, position: Int) {
        Glide.with(context).load(followList[position].image)
            .placeholder(R.drawable.person).into(holder.binding.story)
        holder.binding.name.text = followList[position].name
    }
}