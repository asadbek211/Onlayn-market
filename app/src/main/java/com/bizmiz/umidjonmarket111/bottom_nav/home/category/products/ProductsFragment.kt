package com.bizmiz.umidjonmarket111.bottom_nav.home.category.products

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = requireArguments().get("categoryItem") as CategoryItem
        productsViewModel.getProductDataByCategoryId(category.id,"")
    }
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
        binding = FragmentProductsBinding.bind(inflater.inflate(R.layout.fragment_products, container, false))
        binding.etSearch.hint = category.name

        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(v)
                if (binding.etSearch.text.isNotEmpty()) {
                    val query = binding.etSearch.text.toString()
                    productsViewModel.getQueryData(query,category.id)
                } else {
                    binding.homeExclusiveRecyclerview.visibility = View.GONE
                    binding.txtEslatma.visibility = View.VISIBLE
                    binding.txtEslatma.text = "Mahsulot topilmadi"
                }
                return@OnEditorActionListener true
            }
            false
        })

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
        observe()
        return binding.root
    }
    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
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
    private fun observe() {
        productsViewModel.query.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    if (it.data.isNullOrEmpty()) {
                        binding.homeExclusiveRecyclerview.visibility = View.GONE
                        binding.txtEslatma.visibility = View.VISIBLE
                        binding.txtEslatma.text = "Natija topilmadi"
                    } else {
                        binding.homeExclusiveRecyclerview.visibility = View.VISIBLE
                        exclusiveAdapter.exclusiveList = it.data
                        binding.txtEslatma.visibility = View.GONE
                    }
                }
                ResourceState.ERROR -> {
                    binding.homeExclusiveRecyclerview.visibility = View.GONE
                    binding.txtEslatma.visibility = View.VISIBLE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}