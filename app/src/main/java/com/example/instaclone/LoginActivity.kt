package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.instaclone.databinding.ActivityLoginBinding
import com.example.instaclone.models.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            if (binding.edtEmail.editText?.text.toString().equals("") or
                binding.edtPassword.editText?.text.toString().equals("")) {
                showToast("Please Fill all the Fields")
            } else {
                var user = UserModel(
                    binding.edtEmail.editText?.text.toString(),
                    binding.edtPassword.editText?.text.toString()
                )
                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener {task->
                        if(task.isSuccessful) {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            showToast(task.exception?.localizedMessage.toString())
                        }
                    }

            }
        }
        binding.gotoSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show();
    }
}