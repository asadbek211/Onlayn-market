package com.bizmiz.umidjonmarket111.auth.get_started.sms_verify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentSmsVerifyBinding
import com.bizmiz.umidjonmarket111.MainActivity
import java.text.SimpleDateFormat


class SmsVerifyFragment : Fragment() {
    private var a = false
    private var milliSecond = 0
    private lateinit var phoneNumber: String
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: FragmentSmsVerifyBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        phoneNumber = requireArguments().getString("number").toString()
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        binding = FragmentSmsVerifyBinding.bind(
            inflater.inflate(
                R.layout.fragment_sms_verify,
                container,
                false
            )
        )
        binding.txtPhoneNumber.text = phoneNumber.replace("-","")
        binding.watchAnim.playAnimation()
        binding.btnConfirm.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.loading_color)
            binding.loadingAnim.playAnimation()
            setPrefs(true)
            Handler().postDelayed({
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }, 5000)

        }
        binding.btnClose.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            val navController = Navigation.findNavController(requireActivity(), R.id.authContainer)
            navController.popBackStack()
        }
        timer()
        initTextWatcher(binding.loginMainCode1, binding.loginMainCode2, false)
        initTextWatcher(binding.loginMainCode2, binding.loginMainCode3, false)
        initTextWatcher(binding.loginMainCode3, binding.loginMainCode4, false)
        initTextWatcher(binding.loginMainCode4, binding.loginMainCode5, false)
        initTextWatcher(binding.loginMainCode5, binding.loginMainCode6, false)
        initTextWatcher(binding.loginMainCode6, binding.loginMainCode6, true)

        goToPreviousInput(binding.loginMainCode1, binding.loginMainCode1)
        goToPreviousInput(binding.loginMainCode2, binding.loginMainCode1)
        goToPreviousInput(binding.loginMainCode3, binding.loginMainCode2)
        goToPreviousInput(binding.loginMainCode4, binding.loginMainCode3)
        goToPreviousInput(binding.loginMainCode5, binding.loginMainCode4)
        goToPreviousInput(binding.loginMainCode6, binding.loginMainCode5)
        binding.txtResendCode.setOnClickListener {
            if (milliSecond>1000){
                val timer = object : CountDownTimer(200, 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.txtTimer.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    }
                    override fun onFinish() {
                        binding.txtTimer.setTextColor(resources.getColor(R.color.black))
                    }
                }
                timer.start()
            }else{
                timer()
                binding.watchAnim.playAnimation()
            }
        }
        return binding.root
    }

    private fun goToPreviousInput(editText: EditText, editText2: EditText) {
        editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (editText.text.toString().isEmpty()) {
                    editText2.requestFocus()
                }
            }
            false
        }
    }

    private fun initTextWatcher(editText: EditText, editText2: EditText, isLast: Boolean) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 1 && !isLast) {
                    editText2.requestFocus()
                } else if (s.length == 1 && isLast) {
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(editText.windowToken, 0)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
    private fun setPrefs(name: Boolean) {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("isAuth", name).apply()
    }
    fun getPrefs(): Boolean {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        return prefs.getBoolean("isAuth", false)
    }
    private fun timer() {
        val time = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                milliSecond = millisUntilFinished.toInt()
                if (a) {
                    cancel()
                }
                val simpleDateFormat = SimpleDateFormat("mm:ss")
                val dateString = simpleDateFormat.format(millisUntilFinished)
                binding.txtTimer.text = String.format("%s", dateString)
            }
            override fun onFinish() {
                binding.watchAnim.pauseAnimation()
            }
        }
        time.start()
    }
    override fun onDestroyView() {
        a = true
        super.onDestroyView()
    }
}