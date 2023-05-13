package com.example.android.tubesppb.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.widget.Toast
import com.example.android.tubesppb.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        setwidth()
        binding.btnSignIn.setOnClickListener{
            val email = binding.signInEmail.text.toString()
            val password = binding.signInPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("userEmail", email)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,
                            "Login Gagal",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Fields cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }
        binding.SignUpRedirectText.setOnClickListener {
            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
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