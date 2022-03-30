package com.bizmiz.umidjonmarket111.auth.get_started.update_account

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentUpdateAccountBinding
import com.bizmiz.umidjonmarket111.utils.PhoneNumberTextWatcher

class UpdateAccountFragment : Fragment() {
    private lateinit var binding: FragmentUpdateAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentUpdateAccountBinding.bind(inflater.inflate(R.layout.fragment_update_account, container, false))
        binding.txtPhoneNumber.addTextChangedListener(
            PhoneNumberTextWatcher(
                binding.txtPhoneNumber
            )
        )
        binding.btnCreate.setOnClickListener {
            if (networkCheck()){
                val bundle = bundleOf(
                    "phoneNumber" to "+998${binding.txtPhoneNumber.text?.toString()?.replace("-","")?.trim()}",
                 "number" to 1
                )
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.authContainer)
                navController.navigate(R.id.action_updateAccountFragment2_to_smsVerify,bundle)
            }
        }
        return binding.root
    }
    private fun networkCheck():Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }
}