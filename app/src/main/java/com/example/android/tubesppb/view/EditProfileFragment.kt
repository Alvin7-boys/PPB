package com.example.android.tubesppb.view

import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.tubesppb.databinding.FragmentEditProfileBinding
import com.example.android.tubesppb.room.user.User
import com.example.android.tubesppb.room.user.UserDB
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileFragment(private val user: User) : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var db: UserDB
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        db = UserDB(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()

        val nestedScrollView = binding.layoutEdit
        val display: Display = requireActivity().windowManager.defaultDisplay
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

        // mengambil data user berdasarkan UID dan menampilkan pada UI
        user.let {
            binding.textViewUsername.setText(user.userName)
            binding.textViewPhoneNumber.setText(user.phoneNumber)
            binding.textViewEmail.setText(user.email)
            binding.buttonSave.setOnClickListener {
                // mengupdate data user di database
                val username = binding.textViewUsername.text.toString().trim()
                val phoneNumber = binding.textViewPhoneNumber.text.toString().trim()
                val email = binding.textViewEmail.text.toString().trim()

                CoroutineScope(Dispatchers.IO).launch {
                    db.UserDao().updateUser(user.uid, username, phoneNumber, email)
                    firebaseAuth.currentUser?.updateEmail(email)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().finish()
                    }
                }
            }
        }
        // mengatur onClickListener untuk button Save

        binding.buttonCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }

}
