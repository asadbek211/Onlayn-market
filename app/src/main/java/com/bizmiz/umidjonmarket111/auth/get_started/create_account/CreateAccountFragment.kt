package com.bizmiz.umidjonmarket111.auth.get_started.create_account

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.utils.ResourceState
import com.bizmiz.umidjonmarket111.auth.get_started.sms_verify.UserDataViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentCreateAccountBinding
import com.bizmiz.umidjonmarket111.utils.PhoneNumberTextWatcher
import com.bizmiz.umidjonmarket111.utils.showSoftKeyboard
import com.google.android.material.tabs.TabLayout
import org.koin.android.viewmodel.ext.android.viewModel


class CreateAccountFragment : Fragment() {
    private lateinit var binding: FragmentCreateAccountBinding
    private val userData: UserDataViewModel by viewModel()
    private lateinit var prefs: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        if (!::binding.isInitialized){
            binding = FragmentCreateAccountBinding.bind(
                inflater.inflate(
                    R.layout.fragment_create_account,
                    container,
                    false
                )
            )
        }
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding.loading.setOnClickListener { }
        binding.apply {
            btnCreate.setOnClickListener {
                if (networkCheck()) {
                    if (binding.btnCreate.text == "Shaxsiy hisob yaratish") {
                        if (loginName.text != null && loginSurname.text != null) {
                            if (loginName.text!!.trim().isNotEmpty()) {
                                if (loginSurname.text!!.trim().isNotEmpty()) {
                                    if (txtPhoneNumber.text!!.trim().isNotEmpty()) {
                                        loading(true)
                                        cursorVisible(false)
                                        userData.checkUserRegistered(
                                            "+998${
                                                binding.txtPhoneNumber.text?.toString()
                                                    ?.replace("-", "")?.trim()
                                            }"
                                        )
                                    } else {
                                        txtPhoneNumber.error = "Raqam kiritilishi majburiy"
                                        txtPhoneNumber.showSoftKeyboard()
                                    }
                                } else {
                                    loginSurname.error = "Familiya kiritish majburiy"
                                    loginSurname.showSoftKeyboard()
                                }
                            } else {
                                loginName.error = "Ism kiritish majburiy"
                                loginName.showSoftKeyboard()
                            }
                        }
                    } else if (binding.btnCreate.text == "Shaxsiy hisobga kirish") {
                        if (txtPhoneNumber.text!!.trim().isNotEmpty()) {
                            loading(true)
                            cursorVisible(false)
                            userData.checkUserRegistered(
                                "+998${
                                    binding.txtPhoneNumber.text?.toString()?.replace("-", "")
                                        ?.trim()
                                }"
                            )
                        } else {
                            txtPhoneNumber.error = "Raqam kiritilishi majburiy"
                            txtPhoneNumber.showSoftKeyboard()
                        }
                    }
                } else {
                    Toast.makeText(requireActivity(), "Internet aloqasi yo'q", Toast.LENGTH_SHORT)
                        .show()
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
        checkUserObserve()
        getPrefs()
        return binding.root
    }

    private fun cursorVisible(isVisible: Boolean) {
        binding.loginName.isCursorVisible = isVisible
        binding.loginSurname.isCursorVisible = isVisible
        binding.txtPhoneNumber.isCursorVisible = isVisible
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

    private fun checkUserObserve() {
        userData.checkUser.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    if (it.data == "registered") {
                        loading(false)
                        cursorVisible(true)
                        if (binding.btnCreate.text == "Shaxsiy hisob yaratish") {
                            showDialog(
                                0,
                                "Hurmatli foydalanuvchi telefon raqamingiz orqali shaxsiy hisob yaratilgan.\n\nSignIn orqali shaxsiy hisobingizga\nkiring!"
                            )
                        } else if (binding.btnCreate.text == "Shaxsiy hisobga kirish") {
                            val bundle = bundleOf(
                                "phoneNumber" to "+998${
                                    binding.txtPhoneNumber.text?.toString()?.replace("-", "")
                                        ?.trim()
                                }",
                                "number" to 0
                            )
                            val navController =
                                Navigation.findNavController(requireActivity(), R.id.authContainer)
                            navController.navigate(R.id.action_createAccount_to_smsVerify, bundle)
                        }

                    } else if (it.data == "unregistered") {
                        loading(false)
                        cursorVisible(true)
                        if (binding.btnCreate.text == "Shaxsiy hisob yaratish") {
                            val bundle = bundleOf(
                                "name" to binding.loginName.text.toString(),
                                "surname" to binding.loginSurname.text.toString(),
                                "phoneNumber" to "+998${
                                    binding.txtPhoneNumber.text?.toString()?.replace("-", "")
                                        ?.trim()
                                }",
                                "number" to 0
                            )
                            val navController =
                                Navigation.findNavController(requireActivity(), R.id.authContainer)
                            navController.navigate(R.id.action_createAccount_to_smsVerify, bundle)
                        } else if (binding.btnCreate.text == "Shaxsiy hisobga kirish") {
                            showDialog(
                                1,
                                "Hurmatli foydalanuvchi telefon raqamingiz orqali shaxsiy hisob yaratilmagan.\n\nSignUp orqali shaxsiy hisobingizni\nyarating!"
                            )
                        }

                    }
                }
                ResourceState.ERROR -> {
                    loading(false)
                    cursorVisible(true)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun networkCheck(): Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }

    private fun loading(loading: Boolean) {
        if (loading) {
            binding.loading.visibility = View.VISIBLE
            binding.loadingAnim.playAnimation()
        } else {
            binding.loading.visibility = View.GONE
            binding.loadingAnim.pauseAnimation()
        }

    }

    private fun showDialog(tabId: Int, dialogText: String) {
        val message = AlertDialog.Builder(requireActivity())
        message.setTitle("Umidjon Market 111")
        message.setMessage(dialogText)
            .setCancelable(false)
            .setPositiveButton("Tushunarli") { message, _ ->
                message.dismiss()
                if (tabId == 1) {
                    binding.tabLayout.getTabAt(0)?.select()
                } else {
                    binding.tabLayout.getTabAt(1)?.select()
                }

            }
            .create().show()
    }

    private fun getPrefs() {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        val name = prefs.getString("name", null)
        val surname = prefs.getString("surname", null)
        val phoneNumber = prefs.getString("phoneNumber", null)
        if (name!=null && surname!=null && phoneNumber!=null){
            binding.loginName.setText(name)
            binding.loginSurname.setText(surname)
            binding.txtPhoneNumber.setText(phoneNumber)
        }else if (phoneNumber!=null){
            binding.txtPhoneNumber.setText(phoneNumber)
        }
    }
}