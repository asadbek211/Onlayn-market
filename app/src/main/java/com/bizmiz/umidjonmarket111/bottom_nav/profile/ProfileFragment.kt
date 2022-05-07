package com.bizmiz.umidjonmarket111.bottom_nav.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.utils.ResourceState
import com.bizmiz.umidjonmarket111.auth.container.ContainerActivity
import com.bizmiz.umidjonmarket111.auth.get_started.sms_verify.UserDataViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private val userData: UserDataViewModel by viewModel()
    private lateinit var prefs: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        auth = Firebase.auth
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        if (!::binding.isInitialized) {
            binding = FragmentProfileBinding.bind(
                inflater.inflate(
                    R.layout.fragment_profile,
                    container,
                    false
                )
            )
        }
        binding.txtPhoneNumber.text = auth.currentUser?.phoneNumber
        binding.imgLogout.setOnClickListener {
            setPrefs(0)
            startActivity(Intent(requireActivity(), ContainerActivity::class.java))
            requireActivity().finish()
            auth.signOut()
        }
        binding.editProfile.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.action_bottomNav_to_editProfile)
        }
        if (auth.currentUser != null) {
            userData.getUserData(auth.currentUser!!.uid)
        }
        getUserData()
        return binding.root
    }

    private fun setPrefs(number: Int) {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        prefs.edit().putInt("number", number).apply()
    }

    private fun getUserData() {
        userData.user.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    binding.txtNameSurname.text = "${it.data?.name} ${it.data?.surname}"
                    if (it.data?.userPhotoUrl != null) {
                        if (it.data.userPhotoUrl.isNotEmpty()) {
                            Glide.with(this).load(it.data.userPhotoUrl).into(binding.userImages)
                        }
                    }

                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}