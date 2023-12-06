package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instaclone.R
import com.example.instaclone.adapter.MyPostRvAdapter
import com.example.instaclone.adapter.MyReelsRvAdapter
import com.example.instaclone.databinding.ActivityReelsBinding
import com.example.instaclone.databinding.FragmentMyPostBinding
import com.example.instaclone.databinding.FragmentMyReelsBinding
import com.example.instaclone.databinding.FragmentReelBinding
import com.example.instaclone.models.Post
import com.example.instaclone.models.Reels
import com.example.instaclone.utils.REEL_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class MyReelsFragment : Fragment() {

    private lateinit var binding: FragmentMyReelsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyReelsBinding.inflate(inflater, container, false)
        var reelsList =ArrayList<Reels>()
        var adapter = MyReelsRvAdapter(requireContext(), reelsList)
        binding.rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL_NODE).get().addOnSuccessListener {
            var tempList = arrayListOf<Reels>()
            for(i in it.documents) {
                var reels: Reels = i.toObject<Reels>()!!
                tempList.add(reels)
            }

            reelsList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}