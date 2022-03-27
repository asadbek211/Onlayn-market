package com.bizmiz.umidjonmarket111.bottom_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentBottomNavBinding
import com.bizmiz.umidjonmarket111.MainActivity

class BottomNavFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentBottomNavBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      binding = FragmentBottomNavBinding.bind(inflater.inflate(R.layout.fragment_bottom_nav, container, false))
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment)
        (activity as MainActivity).setSupportActionBar(binding.toolBar)
        (activity as MainActivity).setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()
    }
    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(requireContext(), null)
        popupMenu.inflate(R.menu.nav_menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }
}