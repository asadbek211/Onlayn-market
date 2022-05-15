package com.bizmiz.umidjonmarket111.bottom_nav.home.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.bind(
            inflater.inflate(
                R.layout.fragment_notification,
                container,
                false
            )
        )
        return binding.root
    }
}