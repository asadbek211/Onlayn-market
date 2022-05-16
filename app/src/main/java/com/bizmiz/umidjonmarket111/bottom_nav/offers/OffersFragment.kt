package com.bizmiz.umidjonmarket111.bottom_nav.offers

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.bottom_nav.home.ExclusiveAdapter
import com.bizmiz.umidjonmarket111.bottom_nav.home.ProductsViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentOffersBinding
import com.bizmiz.umidjonmarket111.utils.ResourceState
import org.koin.android.viewmodel.ext.android.viewModel

class OffersFragment : Fragment() {
    private lateinit var exclusiveAdapter: ExclusiveAdapter
    private val productsViewModel: ProductsViewModel by viewModel()
    private lateinit var binding: FragmentOffersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productsViewModel.getQueryDataOffers("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding =
            FragmentOffersBinding.bind(inflater.inflate(R.layout.fragment_offers, container, false))
        binding.etOffers.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(v)
                    val query = binding.etOffers.text.toString()
                    productsViewModel.getQueryDataOffers(query)
                return@OnEditorActionListener true
            }
            false
        })
        binding.offersRecyclerview.adapter = exclusiveAdapter
        exclusiveAdapter.onClickListener {
            val bundle = bundleOf(
                "products" to it
            )
            val navController =
                Navigation.findNavController(requireActivity(), R.id.main_container)
            navController.navigate(R.id.bottomNav_to_productView,bundle)
        }
        observe()
        return binding.root
    }
    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun observe() {
        productsViewModel.offers.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    if (it.data.isNullOrEmpty()) {
                        binding.offersRecyclerview.visibility = View.GONE
                        binding.txtEslatma.visibility = View.VISIBLE
                        binding.txtEslatma.text = "Natija topilmadi"
                    } else {
                        binding.offersRecyclerview.visibility = View.VISIBLE
                        exclusiveAdapter.exclusiveList = it.data
                        binding.txtEslatma.visibility = View.GONE
                    }
                }
                ResourceState.ERROR -> {
                    binding.offersRecyclerview.visibility = View.GONE
                    binding.txtEslatma.visibility = View.VISIBLE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}