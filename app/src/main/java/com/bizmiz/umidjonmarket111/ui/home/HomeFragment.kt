package com.bizmiz.umidjonmarket111.ui.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentHomeBinding
import com.bizmiz.umidjonmarket111.ui.model.CategoryItem
import com.bizmiz.umidjonmarket111.ui.model.ExclusiveItem
import com.bumptech.glide.Glide
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var exclusiveAdapter: ExclusiveAdapter
    private lateinit var listOffer:ArrayList<Int>
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        categoryAdapter = CategoryAdapter()
        exclusiveAdapter = ExclusiveAdapter()
        binding.homeCategoryRecyclerview.adapter = categoryAdapter
        binding.homeExclusiveRecyclerview.adapter = exclusiveAdapter
        setCategoryAdapter()
        setExclusiveAdapter()
        listOffer = arrayListOf(
            R.drawable.offer1,
            R.drawable.offer2
        )
        binding.carouselView.setImageListener(imageListener)
        binding.carouselView.pageCount = listOffer.size
        return binding.root
    }
    private fun setCategoryAdapter(){
        for (i in 0 until listCategoryImg.size){
            categoryAdapter.categoryList.add(CategoryItem(listCategoryImg[i], listCategoryName[i]))
        }
    }
    private fun setExclusiveAdapter(){
        for (i in 0 until listExclusiveImg.size){
            exclusiveAdapter.exclusiveList.add(ExclusiveItem(listExclusiveImg[i], listExclusiveName[i],"${i+1}0 000 so'm"))
        }
    }
    var imageListener =
        ImageListener { position, imageView ->
            Glide.with(imageView).load(listOffer[position]).into(imageView)
        }
}