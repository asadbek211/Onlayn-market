package com.bizmiz.umidjonmarket111.bottom_nav.home.category.products

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.bottom_nav.home.ExclusiveAdapter
import com.bizmiz.umidjonmarket111.bottom_nav.home.ProductsViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentProductsBinding
import com.bizmiz.umidjonmarket111.models.CategoryItem
import com.bizmiz.umidjonmarket111.utils.ResourceState
import org.koin.android.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private lateinit var category:CategoryItem
    private lateinit var exclusiveAdapter:ExclusiveAdapter
    private val productsViewModel: ProductsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        requireActivity().window.setFlags(
            0,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        exclusiveAdapter = ExclusiveAdapter()
        category = requireArguments().get("categoryItem") as CategoryItem
        productsViewModel.getProductDataByCategoryId(category.id,"")
        binding = FragmentProductsBinding.bind(inflater.inflate(R.layout.fragment_products, container, false))
        binding.etSearch.hint = category.name
        binding.imgBack.setOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.popBackStack()
        }
        exclusiveAdapter.onClickListener {
            val bundle = bundleOf(
                "products" to it
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.action_productsFragment_to_productView,bundle)
        }
        binding.homeExclusiveRecyclerview.adapter = exclusiveAdapter
        productsObserve()
        return binding.root
    }
    private fun productsObserve() {
        productsViewModel.productsByCategory.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    exclusiveAdapter.exclusiveList = it.data!!
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}