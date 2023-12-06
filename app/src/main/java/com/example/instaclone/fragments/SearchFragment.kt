package com.example.instaclone.fragments

import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaclone.R
import com.example.instaclone.adapter.SearchAdapter
import com.example.instaclone.databinding.FragmentSearchBinding
import com.example.instaclone.models.UserModel
import com.example.instaclone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.rv.adapter = adapter
        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
           addToUserList(it)
        }

        binding.searchButton.setOnClickListener{
            var text = binding.searchView.text.toString()

            Firebase.firestore.collection(USER_NODE).whereEqualTo("name", text).get().addOnSuccessListener {
                if(!it.isEmpty) {
                    addToUserList(it)
                }
            }
        }

        return  binding.root
    }

    private fun addToUserList(it: QuerySnapshot?) {
        var tempList = ArrayList<UserModel>()
        userList.clear()
        for(i in it!!.documents) {
            if(i.id.toString() == Firebase.auth.currentUser!!.uid) { }
            else {
                var user: UserModel = i.toObject<UserModel>()!!
                tempList.add(user)
            }
        }
        userList.addAll(tempList)
        adapter.notifyDataSetChanged()
    }


    companion object {

    }
}