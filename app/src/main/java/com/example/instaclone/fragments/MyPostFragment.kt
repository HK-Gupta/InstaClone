package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instaclone.R
import com.example.instaclone.adapter.MyPostRvAdapter
import com.example.instaclone.databinding.ActivityPostBinding
import com.example.instaclone.databinding.FragmentMyPostBinding
import com.example.instaclone.models.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class MyPostFragment : Fragment() {

    private lateinit var binding: FragmentMyPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPostBinding.inflate(inflater, container, false)
        var postList =ArrayList<Post>()
        var adapter = MyPostRvAdapter(requireContext(), postList)
        binding.rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            var tempList = arrayListOf<Post>()
            for(i in it.documents) {
                var post:Post = i.toObject<Post>()!!
                tempList.add(post)
            }

            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}