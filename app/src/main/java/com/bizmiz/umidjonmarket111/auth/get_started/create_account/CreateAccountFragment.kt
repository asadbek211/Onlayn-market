package com.bizmiz.umidjonmarket111.auth.get_started.create_account

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentCreateAccountBinding
import com.bizmiz.umidjonmarket111.utils.PhoneNumberTextWatcher
import com.google.android.material.tabs.TabLayout


class CreateAccountFragment : Fragment() {
    private lateinit var binding: FragmentCreateAccountBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        binding = FragmentCreateAccountBinding.bind(
            inflater.inflate(
                R.layout.fragment_create_account,
                container,
                false
            )
        )
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding.apply {
            btnCreate.setOnClickListener {
                if (networkCheck()){
                    val bundle = bundleOf(
                        "number" to "+998${binding.txtPhoneNumber.text?.trim()}"
                    )
                    val navController =
                        Navigation.findNavController(requireActivity(), R.id.authContainer)
                    navController.navigate(R.id.action_createAccount_to_smsVerify,bundle)
                }
            }
            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            loginName.alpha = 0f
                            loginName.animate().alpha(1f).duration = 500
                            loginSurname.alpha = 0f
                            loginSurname.animate().alpha(1f).setDuration(500).withEndAction {
                            }
                            signDescription.text = "Shaxsiy hisob yarating"
                            btnCreate.text = "Shaxsiy hisob yaratish"
                            loginName.visibility = View.VISIBLE
                            loginSurname.visibility = View.VISIBLE
                        }
                        1 -> {
                            loginName.alpha = 1f
                            loginSurname.animate().alpha(0f).duration = 500
                            loginSurname.alpha = 1f
                            loginName.animate().alpha(0f).setDuration(500).withEndAction {
                            }
                            signDescription.text = "Shaxsiy hisobingizga kiring"
                            btnCreate.text = "Shaxsiy hisobga kirish"
                            loginName.visibility = View.GONE
                            loginSurname.visibility = View.GONE
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

            txtPhoneNumber.addTextChangedListener(
                PhoneNumberTextWatcher(
                    txtPhoneNumber
                )
            )
        }

        setAnimation()
        return binding.root
    }

    private fun setAnimation() {

        binding.apply {
            tabLayout.alpha = 0f
            tabLayout.translationY = 50f
            tabLayout.animate().alpha(1f).translationYBy(-50f).duration = 1000
            signDescription.alpha = 0f
            signDescription.translationY = 50f
            signDescription.animate().alpha(1f).translationYBy(-50f).duration = 1000
            loginName.alpha = 0f
            loginName.translationY = 50f
            loginName.animate().alpha(1f).translationYBy(-50f).duration = 1000
            loginSurname.alpha = 0f
            loginSurname.translationY = 50f
            loginSurname.animate().alpha(1f).translationYBy(-50f).duration = 1000
            loginMainLayoutPhone.alpha = 0f
            loginMainLayoutPhone.translationY = 50f
            loginMainLayoutPhone.animate().alpha(1f).translationYBy(-50f).duration = 1000
            btnCreate.alpha = 0f
            btnCreate.translationY = 50f
            btnCreate.animate().alpha(1f).translationYBy(-50f).duration = 1000
            logo.alpha = 0f
            logo.animate().alpha(1f).duration = 1000
        }
    }
    private fun networkCheck():Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }
}