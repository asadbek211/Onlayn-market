package com.bizmiz.umidjonmarket111.bottom_nav.product_view

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
import com.bizmiz.umidjonmarket111.databinding.FragmentProductViewBinding
import com.bizmiz.umidjonmarket111.bottom_nav.home.PopularProductAdapter
import com.bizmiz.umidjonmarket111.utils.listExclusiveImg
import com.bizmiz.umidjonmarket111.utils.listExclusiveName
import com.bizmiz.umidjonmarket111.models.ExclusiveItem

class ProductViewFragment : Fragment() {
    private var isPressed = true
    private lateinit var binding: FragmentProductViewBinding
    private lateinit var popularProductAdapter: PopularProductAdapter
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.first_color)
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
           0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
       binding = FragmentProductViewBinding.bind(inflater.inflate(R.layout.fragment_product_view, container, false))
       binding.ivFavourite.setOnClickListener {
           isPressed = if (isPressed){
               binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_on_white)
               false
           }else{
               binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
               true
           }
       }
        binding.ivBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.main_container)
            navController.popBackStack()
        }
        popularProductAdapter = PopularProductAdapter()
        binding.popularRecyclerview.adapter = popularProductAdapter
        setPopularProductAdapter()
        return binding.root
    }
    private fun setPopularProductAdapter() {
        for (i in 0 until listExclusiveImg.size) {
            popularProductAdapter.popularProductList.add(
                ExclusiveItem(
                    listExclusiveImg[i],
                    listExclusiveName[i],
                    "${i + 1}0 000 UZS"
                )
            )
        }
    }
}