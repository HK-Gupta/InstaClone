package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instaclone.models.UserModel
import com.example.instaclone.databinding.ActivitySignUpBinding
import com.example.instaclone.utils.USER_NODE
import com.example.instaclone.utils.USER_PROFILE_FOLDER
import com.example.instaclone.utils.uploadImage
import com.google.api.Http
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class SignUp : AppCompatActivity() {

    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {uri->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) {
                if(it!=null) {
                    user.image = it
                    binding.profilePic.setImageURI(uri)
                }
            }
        }

    }

    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user = UserModel()

        if(intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
            binding.btnSignUp.text = "Update Profile"
            binding.gotoLogin.visibility = View.INVISIBLE
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                .get().addOnSuccessListener {
                    user = it.toObject<UserModel>()!!
                    if(!user.image.isNullOrEmpty()) {
                        Picasso.get().load(user.image).into(binding.profilePic)
                    }
                    binding.edtName.editText?.setText(user.name)
                    binding.edtEmail.editText?.setText(user.email)
                    binding.edtPassword.editText?.setText(user.password)
                }
        }
        // Adding the two color text in the Goto Login TextView
        val text = "<font color=#000000>Already have an account</font> <font color=#2080FF>Login?</font>"
        binding.gotoLogin.setText(Html.fromHtml(text))

        binding.btnSignUp.setOnClickListener {
            if(intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
                Firebase.firestore.collection(USER_NODE)
                    .document(Firebase.auth.currentUser!!.uid).set(user)
                    .addOnSuccessListener {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
            }
            else {
                if ((binding.edtName.editText?.text.toString() == "") ||
                    (binding.edtEmail.editText?.text.toString() == "") ||
                    (binding.edtPassword.editText?.text.toString() == "")
                ) {
                    showToast("Please Fill the required Fields")
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.edtEmail.editText?.text.toString(),
                        binding.edtPassword.editText?.text.toString()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.name = binding.edtName.editText?.text.toString()
                            user.email = binding.edtEmail.editText?.text.toString()
                            user.password = binding.edtPassword.editText?.text.toString()

                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                    showToast("Signed in Successfully")
                                }
                        } else {
                            showToast(task.exception?.localizedMessage.toString())
                        }
                    }
                }
            }
        }

        binding.addProfilePic.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.gotoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SignUp, message, Toast.LENGTH_SHORT).show();
    }
}