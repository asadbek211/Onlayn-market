package com.bizmiz.umidjonmarket111.bottom_nav.favourite

import android.content.Context
import android.content.SharedPreferences
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
import com.bizmiz.umidjonmarket111.databinding.FragmentFavouriteBinding
import com.bizmiz.umidjonmarket111.utils.Constant
import com.bizmiz.umidjonmarket111.utils.ResourceState
import org.koin.android.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var exclusiveAdapter: ExclusiveAdapter
    private val productsViewModel: ProductsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = requireActivity().getSharedPreferences(
            Constant.PREFS,
            Context.MODE_PRIVATE
        )
        productsViewModel.getFavouriteData(prefs)
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
        binding = FragmentFavouriteBinding.bind(inflater.inflate(R.layout.fragment_favourite, container, false))
        binding.recViewFavourite.adapter = exclusiveAdapter
        exclusiveAdapter.onFavouriteListener {
            productsViewModel.getFavouriteData(prefs)
        }
        observe()
        exclusiveAdapter.onClickListener {
            val bundle = bundleOf(
                "products" to it
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.bottomNav_to_productView,bundle)
        }
        return binding.root
    }
    private fun observe() {
        productsViewModel.favourite.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    if (it.data!!.isEmpty()) {
                        binding.txtEmpty.visibility = View.VISIBLE
                        binding.recViewFavourite.visibility = View.GONE
                    }
                    exclusiveAdapter.exclusiveList = it.data
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}