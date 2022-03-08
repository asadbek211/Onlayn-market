package com.bizmiz.umidjonmarket111.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentHomeBinding
import com.bizmiz.umidjonmarket111.ui.home.adapter.CategoryAdapter
import com.bizmiz.umidjonmarket111.ui.home.model.CategoryItem
import com.bumptech.glide.Glide
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var listOffer:ArrayList<Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        categoryAdapter = CategoryAdapter()
        binding.homeCategoryRecyclerview.adapter = categoryAdapter
        setAdapter()
        listOffer = arrayListOf(
            R.drawable.offer1,
            R.drawable.offer2
        )
        binding.carouselView.setImageListener(imageListener)
        binding.carouselView.pageCount = listOffer.size
        return binding.root
    }
    private fun setAdapter(){
        for (i in 0 until listImg.size){
            categoryAdapter.categoryList.add(CategoryItem(listImg[i], listName[i]))
        }
    }
    var imageListener =
        ImageListener { position, imageView ->
            Glide.with(imageView).load(listOffer[position]).into(imageView)
        }
}