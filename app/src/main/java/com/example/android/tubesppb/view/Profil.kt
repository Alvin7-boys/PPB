package com.example.android.tubesppb.view

import android.content.Intent
import android.os.Bundle
import android.view.Display
import androidx.appcompat.app.AppCompatActivity
import com.example.android.tubesppb.R
import com.example.android.tubesppb.databinding.ActivityProfilBinding
import com.example.android.tubesppb.room.user.User
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION", "NAME_SHADOWING")
class Profil : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        setmaxLayout()
        val user = intent.getParcelableExtra<User>("USER_DATA")
        user?.let { it ->
            binding.textViewUsername.text = it.userName
            binding.textViewPhoneNumber.text = it.phoneNumber
            binding.textViewEmail.text = it.email
            binding.buttonEditProfile.setOnClickListener {
                // Membuka Fragment Edit Profile
                user.let { it ->
                    val fragment = EditProfileFragment(it)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        // Mengatur onClickListener untuk button Logout
        binding.buttonLogOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun setmaxLayout() {
        val nestedScrollView = binding.layoutInti2
        val display: Display = windowManager.defaultDisplay
        val width = display.width

        if (width > 1000) {
            val layoutParams = nestedScrollView.layoutParams
            layoutParams.width = 1000
            nestedScrollView.layoutParams = layoutParams
        } else {
            val layoutParams = nestedScrollView.layoutParams
            layoutParams.width = width
            nestedScrollView.layoutParams = layoutParams
        }

    }
}
