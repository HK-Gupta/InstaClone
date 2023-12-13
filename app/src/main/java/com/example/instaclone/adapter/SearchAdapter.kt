package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.databinding.SearchRvBinding
import com.example.instaclone.models.UserModel
import com.example.instaclone.utils.FOLLOW
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SearchAdapter(var context: Context, var userList: ArrayList<UserModel>): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isFollow = false
        Glide.with(context).load(userList[position].image).placeholder(R.drawable.person).into(holder.binding.profileImage)
        holder.binding.name.text = userList[position].name

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
            .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {
            if(it.documents.size == 0) {
                isFollow = false
            }
            else {
                isFollow = true
                holder.binding.follow.text = "Unfollow"
            }
        }
        holder.binding.follow.setOnClickListener {
            if(isFollow) {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                    .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                            .document(it.documents[0].id).delete()
                    }
                isFollow = false
                holder.binding.follow.text = "Follow"
            } else {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                    .document().set(userList[position])
                isFollow = true
                holder.binding.follow.text = "Un Follow"
            }

        }
    }
}