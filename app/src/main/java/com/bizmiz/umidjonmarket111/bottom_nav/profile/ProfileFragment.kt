package com.bizmiz.umidjonmarket111.bottom_nav.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentProfileBinding
import com.bizmiz.umidjonmarket111.auth.container.ContainerActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
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
       binding = FragmentProfileBinding.bind(inflater.inflate(R.layout.fragment_profile, container, false))
        binding.imgLogout.setOnClickListener {
            startActivity(Intent(requireActivity(), ContainerActivity::class.java))
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.action_bottomNav_to_editProfile)
        }
        return binding.root
    }
}