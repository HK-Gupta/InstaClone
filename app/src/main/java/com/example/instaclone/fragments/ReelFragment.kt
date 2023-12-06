package com.example.instaclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instaclone.R
import com.example.instaclone.adapter.ReelAdapter
import com.example.instaclone.databinding.FragmentReelBinding
import com.example.instaclone.models.Reels
import com.example.instaclone.utils.REEL_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class ReelFragment : Fragment() {

    lateinit var binding: FragmentReelBinding
    lateinit var adapter: ReelAdapter
    var reelList = ArrayList<Reels>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentReelBinding.inflate(inflater, container, false)
        adapter = ReelAdapter(requireContext(), reelList)
        binding.viewPager.adapter = adapter
        Firebase.firestore.collection(REEL_NODE).get().addOnSuccessListener {
            var tempList = ArrayList<Reels>()
            reelList.clear()
            for(i in it.documents) {
                var reels = i.toObject<Reels>()!!
                tempList.add(reels)
            }
            reelList.addAll(tempList)
            reelList.reverse()      // To show teh recent reels
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {

    }
}