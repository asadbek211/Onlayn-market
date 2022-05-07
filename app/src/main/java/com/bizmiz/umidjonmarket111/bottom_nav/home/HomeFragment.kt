package com.bizmiz.umidjonmarket111.bottom_nav.home

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.auth.get_started.sms_verify.UserDataViewModel
import com.bizmiz.umidjonmarket111.bottom_nav.home.category.CategoryAdapter
import com.bizmiz.umidjonmarket111.bottom_nav.home.category.CategoryViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentHomeBinding
import com.bizmiz.umidjonmarket111.models.CategoryItem
import com.bizmiz.umidjonmarket111.utils.ResourceState
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.synnapps.carouselview.ImageListener
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val categoryViewModel: CategoryViewModel by viewModel()
    private val productsViewModel:ProductsViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var exclusiveAdapter: ExclusiveAdapter
    private lateinit var listOffer: ArrayList<Int>
    private lateinit var categoryList: List<CategoryItem>
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryAdapter()
        exclusiveAdapter = ExclusiveAdapter()
        categoryViewModel.getCategoryData()
        productsViewModel.getProductData()
        listOffer = arrayListOf(
            R.drawable.offer1,
            R.drawable.offer2,
            R.drawable.offer1,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding =
                FragmentHomeBinding.bind(
                    inflater.inflate(
                        R.layout.fragment_home,
                        container,
                        false
                    )
                )
        }
        requireActivity().window.setFlags(
            0,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        categoryObserve()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        binding.homeCategoryRecyclerview.adapter = categoryAdapter
        binding.homeExclusiveRecyclerview.adapter = exclusiveAdapter
        binding.carouselView.setImageListener(imageListener)
        binding.carouselView.pageCount = listOffer.size
        binding.tvAllCategory.setOnClickListener {
            val bundle = bundleOf(
                "category" to categoryList
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.bottomNav_to_category, bundle)
        }
        categoryAdapter.onClickListener { categoryItem ->
            val bundle = bundleOf(
                "categoryItem" to categoryItem
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.action_bottomNav_to_productsFragment, bundle)
        }
        exclusiveAdapter.onClickListener {
            val bundle = bundleOf(
                "products" to it
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.bottomNav_to_productView,bundle)
        }
        productsObserve()
        return binding.root
    }

    private fun categoryObserve() {
        categoryViewModel.category.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    categoryList = it.data!!
                    categoryAdapter.categoryList = categoryList
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun productsObserve() {
        productsViewModel.products.observe(viewLifecycleOwner, Observer { it ->
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
    var imageListener =
        ImageListener { position, imageView ->
            Glide.with(imageView).load(listOffer[position]).into(imageView)
        }
}