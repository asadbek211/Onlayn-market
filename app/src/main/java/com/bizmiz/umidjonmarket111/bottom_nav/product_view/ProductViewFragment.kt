package com.bizmiz.umidjonmarket111.bottom_nav.product_view

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.bottom_nav.home.PopularProductAdapter
import com.bizmiz.umidjonmarket111.bottom_nav.home.ProductsViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentProductViewBinding
import com.bizmiz.umidjonmarket111.models.ProductsItem
import com.bizmiz.umidjonmarket111.utils.Constant
import com.bizmiz.umidjonmarket111.utils.ResourceState
import com.bumptech.glide.Glide
import org.koin.android.viewmodel.ext.android.viewModel

class ProductViewFragment : Fragment() {
    private val productsViewModel:ProductsViewModel by viewModel()
    private var isPressed = true
    private var categoryId = ""
    private var productId = ""
    private lateinit var products:ProductsItem
    private lateinit var binding: FragmentProductViewBinding
    private lateinit var popularProductAdapter: PopularProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        products = requireArguments().get("products") as ProductsItem
        categoryId = products.categoryId
        productId = products.id
        productsViewModel.getProductDataByCategoryId(categoryId,productId)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        binding = FragmentProductViewBinding.bind(
            inflater.inflate(
                R.layout.fragment_product_view,
                container,
                false
            )
        )
        Glide.with(this).load(products.image_url).into(binding.images)
        binding.tvProductName.text = products.name
        binding.description.text = products.description
        binding.tvProductCriterion.text = "1${products.type}"
        binding.tvProductPrice.text = "${products.startPrice} UZS"
        binding.tvNewProductPrice.text = "${products.endPrice} UZS"
        binding.tvOffer.text = products.percent
        binding.tvProductCount.text = "(${products.view} ko'rilgan)"
        if (products.endPrice!=""){
            binding.tvNewProductPrice.text = "${products.endPrice} UZS"
            binding.tvOffer.text = products.percent
            binding.tvNewProductPrice.visibility = View.VISIBLE
            binding.offerBackground.visibility = View.VISIBLE
            binding.tvOffer.visibility = View.VISIBLE
            binding.tvLine.visibility = View.VISIBLE

        }else{
            binding.tvNewProductPrice.visibility = View.GONE
            binding.offerBackground.visibility = View.GONE
            binding.tvOffer.visibility = View.GONE
            binding.tvLine.visibility = View.GONE
        }
        val prefs: SharedPreferences = binding.root.context.getSharedPreferences(
            Constant.PREFS,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor =prefs.edit()
        if (prefs.contains(productId)) {
            binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_on_white)
        } else {
            binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
        binding.ivFavourite.setOnClickListener {
            if (prefs.contains(productId)) {
                binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                editor.remove(productId).apply()
            } else {
                binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_on_white)
                editor.putBoolean(productId,true).apply()
            }
        }
        binding.ivBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.popBackStack()
        }
        popularProductAdapter = PopularProductAdapter()
        binding.popularRecyclerview.adapter = popularProductAdapter
        popularProductAdapter.onClickListener {
            val bundle = bundleOf(
                "products" to it
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.action_productView_self,bundle)
        }
        productsObserve()
        return binding.root
    }
    private fun productsObserve() {
        productsViewModel.productsByCategory.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    val list = it.data!!
                    popularProductAdapter.popularProductList = list.shuffled()
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}