package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.R
import com.example.instaclone.databinding.ReelDesignBinding
import com.example.instaclone.models.Reels
import com.squareup.picasso.Picasso

class ReelAdapter(var context: Context, var reelList:ArrayList<Reels>) : RecyclerView.Adapter<ReelAdapter.ViewHolder>(){

    inner class ViewHolder(var binding:ReelDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ReelDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(reelList[position].profile_link).placeholder(R.drawable.person).into(holder.binding.userImage)
        holder.binding.caption.text = reelList[position].caption
        holder.binding.videoView.setVideoPath(reelList[position].reelUrl)
        holder.binding.videoView.setOnPreparedListener {
            holder.binding.progressBar.visibility = View.GONE
            holder.binding.videoView.start()
        }
    }
}