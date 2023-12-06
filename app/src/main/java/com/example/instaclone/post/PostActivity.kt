package com.example.instaclone.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instaclone.HomeActivity
import com.example.instaclone.R
import com.example.instaclone.databinding.ActivityPostBinding
import com.example.instaclone.models.Post
import com.example.instaclone.models.UserModel
import com.example.instaclone.utils.POST_FOLDER
import com.example.instaclone.utils.POST_NODE
import com.example.instaclone.utils.USER_NODE
import com.example.instaclone.utils.USER_PROFILE_FOLDER
import com.example.instaclone.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {

    private var imageUrl: String? = null
    private val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) { url ->
                if (url != null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.btnPost.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    var user = it.toObject<UserModel>()!!
                    val post: Post = Post(
                        postUrl = imageUrl!!,
                        caption = binding.captionText.editText?.text.toString(),
                        uid = Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString()
                    )
                    Firebase.firestore.collection(POST_NODE).document().set(post)
                        .addOnSuccessListener {
                            Firebase.firestore.collection(Firebase.auth.currentUser!!.uid)
                                .document().set(post)
                                .addOnSuccessListener {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }
                        }
                }
        }

        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}