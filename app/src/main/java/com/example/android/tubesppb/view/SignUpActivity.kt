package com.example.android.tubesppb.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.widget.Toast
import com.example.android.tubesppb.databinding.ActivitySignUpBinding
import com.example.android.tubesppb.room.user.User
import com.example.android.tubesppb.room.user.UserDB
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db by lazy { UserDB(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        setwidth()
        binding.btnSignUp.setOnClickListener {
            val username = binding.signupUsername.text.toString()
            val phoneNumber = binding.signupPhone.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && username.isNotEmpty() && phoneNumber.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val uid = it.result?.user?.uid.toString() // ambil UID dari Firebase Auth
                                CoroutineScope(Dispatchers.IO).launch {
                                    db.UserDao().addUser(User(0, username,phoneNumber,email,password, uid ?: ""))
                                }
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }
        binding.SignUpRedirectText.setOnClickListener{
            val loginIntent = Intent(this, SignInActivity::class.java)
            startActivity(loginIntent)

        }
    }
    private fun setwidth() {
        val layoutInti = binding.layoutInti
        val display: Display = windowManager.defaultDisplay
        val width = display.width

        if (width > 1000) {
            layoutInti.layoutParams.width = 1000

        } else {
            layoutInti.layoutParams.width = width
        }
    }
}