package com.bizmiz.umidjonmarket111.bottom_nav.home

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
import com.bizmiz.umidjonmarket111.ResourceState
import com.bizmiz.umidjonmarket111.auth.get_started.sms_verify.UserDataViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentHomeBinding
import com.bizmiz.umidjonmarket111.bottom_nav.home.category.CategoryAdapter
import com.bizmiz.umidjonmarket111.models.CategoryItem
import com.bizmiz.umidjonmarket111.models.ExclusiveItem
import com.bizmiz.umidjonmarket111.utils.listCategoryImg
import com.bizmiz.umidjonmarket111.utils.listCategoryName
import com.bizmiz.umidjonmarket111.utils.listExclusiveImg
import com.bizmiz.umidjonmarket111.utils.listExclusiveName
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.synnapps.carouselview.ImageListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var auth:FirebaseAuth
    private val userData: UserDataViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var exclusiveAdapter: ExclusiveAdapter
    private lateinit var listOffer: ArrayList<Int>

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        binding =
            FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        categoryAdapter = CategoryAdapter()
        exclusiveAdapter = ExclusiveAdapter()
        binding.homeCategoryRecyclerview.adapter = categoryAdapter
        binding.homeExclusiveRecyclerview.adapter = exclusiveAdapter
        setCategoryAdapter()
        setExclusiveAdapter()
        listOffer = arrayListOf(
            R.drawable.offer1,
            R.drawable.offer2,
            R.drawable.offer1,
            R.drawable.offer2
        )
        binding.carouselView.setImageListener(imageListener)
        binding.carouselView.pageCount = listOffer.size
        binding.tvAllCategory.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.bottomNav_to_category)
        }
        exclusiveAdapter.onClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.bottomNav_to_productView)
        }
        if (auth.currentUser != null) {
            userData.getUserData(auth.currentUser!!.uid)
        }
        getUserData()
        return binding.root
    }

    private fun setCategoryAdapter() {
        for (i in 0 until listCategoryImg.size) {
            categoryAdapter.categoryList.add(CategoryItem(listCategoryImg[i], listCategoryName[i]))
        }
    }

    private fun setExclusiveAdapter() {
        for (i in 0 until listExclusiveImg.size) {
            exclusiveAdapter.exclusiveList.add(
                ExclusiveItem(
                    listExclusiveImg[i],
                    listExclusiveName[i],
                    "${i + 1}0 000 UZS"
                )
            )
        }
    }

    var imageListener =
        ImageListener { position, imageView ->
            Glide.with(imageView).load(listOffer[position]).into(imageView)
        }
    private fun getUserData() {
        userData.user.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    binding.tvUseName.text = "Salom ${it.data?.name}"
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}