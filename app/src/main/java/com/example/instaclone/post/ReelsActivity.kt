package com.example.instaclone.post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instaclone.HomeActivity
import com.example.instaclone.databinding.ActivityReelsBinding
import com.example.instaclone.models.Reels
import com.example.instaclone.models.UserModel
import com.example.instaclone.utils.POST_FOLDER
import com.example.instaclone.utils.REEL_NODE
import com.example.instaclone.utils.USER_NODE
import com.example.instaclone.utils.uploadImage
import com.example.instaclone.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ReelsActivity : AppCompatActivity() {

    private var videoUrl:String? = null
    lateinit var progressDialog: ProgressDialog
    val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri->
        uri?.let {
            uploadVideo(uri, REEL_NODE, progressDialog) { url->
                if(url!=null) {
                    videoUrl = url
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)

        binding.btnSelectReels.setOnClickListener {
            launcher.launch("video/*")
        }

        binding.btnPost.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    var user: UserModel = it.toObject<UserModel>()!!
                    val reels: Reels = Reels(videoUrl!!, binding.captionText.editText?.text.toString(), user.image!!)
                    Firebase.firestore.collection(REEL_NODE).document().set(reels).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL_NODE).document().set(reels)
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