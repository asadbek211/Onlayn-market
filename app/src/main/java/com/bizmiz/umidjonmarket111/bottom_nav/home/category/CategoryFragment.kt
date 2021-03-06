package com.bizmiz.umidjonmarket111.bottom_nav.home.category

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentCategoryBinding
import com.bizmiz.umidjonmarket111.utils.listCategoryImg
import com.bizmiz.umidjonmarket111.utils.listCategoryName
import com.bizmiz.umidjonmarket111.models.CategoryItem

class CategoryFragment : Fragment() {
    private lateinit var allCategoryAdapter: AllCategoryAdapter
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var list:List<CategoryItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        list = requireArguments().get("category") as List<CategoryItem>
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(),R.color.secondary_color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        binding = FragmentCategoryBinding.bind(inflater.inflate(R.layout.fragment_category, container, false))
        binding.ivBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.main_container)
            navController.popBackStack()
        }
        allCategoryAdapter = AllCategoryAdapter()
        allCategoryAdapter.onClickListener {categoryItem->
            val bundle = bundleOf(
                "categoryItem" to categoryItem
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.action_category_to_productsFragment, bundle)
        }
        binding.categoryRecyclerview.adapter = allCategoryAdapter
        setAllCategoryAdapter()
        return binding.root
    }
    private fun setAllCategoryAdapter(){
        allCategoryAdapter.allCategoryList = list
    }
}