package com.bizmiz.umidjonmarket111.auth.container

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.ActivityContainerBinding

class ContainerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}