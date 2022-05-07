package com.bizmiz.umidjonmarket111.auth.get_started.sms_verify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.MainActivity
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.utils.ResourceState
import com.bizmiz.umidjonmarket111.databinding.FragmentSmsVerifyBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class SmsVerifyFragment : Fragment() {
    private val userData: UserDataViewModel by viewModel()
    private var a = false
    private var milliSecond = 0
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var auth: FirebaseAuth
    private var name: String? = null
    private var surname: String? = null
    private lateinit var phoneNumber: String
    private var number by Delegates.notNull<Int>()
    private lateinit var binding: FragmentSmsVerifyBinding
    private lateinit var prefs: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        auth = Firebase.auth
        auth.setLanguageCode("uz")
        name = requireArguments().getString("name")
        surname = requireArguments().getString("surname")
        phoneNumber = requireArguments().getString("phoneNumber").toString()
        number = requireArguments().getInt("number", 0)
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
        binding.loading.setOnClickListener {}
        binding.txtPhoneNumber.text = phoneNumber
        binding.btnClose.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.authContainer)
            navController.popBackStack()
        }
        initTextWatcher(binding.code1, binding.code2, false)
        initTextWatcher(binding.code2, binding.code3, false)
        initTextWatcher(binding.code3, binding.code4, false)
        initTextWatcher(binding.code4, binding.code5, false)
        initTextWatcher(binding.code5, binding.code6, false)
        initTextWatcher(binding.code6, binding.code6, true)
        goToPreviousInput(binding.code1, binding.code1)
        goToPreviousInput(binding.code2, binding.code1)
        goToPreviousInput(binding.code3, binding.code2)
        goToPreviousInput(binding.code4, binding.code3)
        goToPreviousInput(binding.code5, binding.code4)
        goToPreviousInput(binding.code6, binding.code5)
        binding.txtResendCode.setOnClickListener {
            if (milliSecond > 1000) {
                val timer = object : CountDownTimer(200, 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.txtTimer.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    }

                    override fun onFinish() {
                        binding.txtTimer.setTextColor(resources.getColor(R.color.black))
                    }
                }
                timer.start()
            } else if (::resendToken.isInitialized) {
                loading(true)
                resendVerificationCode(phoneNumber, resendToken)
            }
        }
        binding.watchAnim.pauseAnimation()
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                cursorVisible(true)
                loading(false)
                binding.watchAnim.playAnimation()
                timer()
                storedVerificationId = verificationId
                resendToken = token
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
            override fun onVerificationFailed(e: FirebaseException) {
                loading(false)
                Toast.makeText(requireActivity(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        startPhoneNumberVerification(phoneNumber)
        binding.apply {
            btnConfirm.setOnClickListener {
                if (code1.text.isNotEmpty() &&code2.text.isNotEmpty() &&code3.text.isNotEmpty() &&
                    code4.text.isNotEmpty() &&code5.text.isNotEmpty() &&code6.text.isNotEmpty()){
                    if (milliSecond > 1000) {
                        loading(true)
                        val code =
                            code1.text.toString() + code2.text.toString() + code3.text.toString() + code4.text.toString() + code5.text.toString() + code6.text.toString()
                        verifyPhoneNumberWithCode(storedVerificationId, code)
                    }else{
                        Toast.makeText(requireActivity(), "SMS Kod yaroqsiz", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireActivity(), "Qatorlarni to'ldiring", Toast.LENGTH_SHORT).show()
                }
            }
        }
        loading(true)
        userDataObserve()
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

    private fun timer() {
        val time = object : CountDownTimer(60000, 1000) {
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

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        binding.apply {
            code1.setText("")
            code2.setText("")
            code3.setText("")
            code4.setText("")
            code5.setText("")
            code6.setText("")
        }
        cursorVisible(false)
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
        if (token != null) {
            optionsBuilder.setForceResendingToken(token)
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        if (number == 1) {
            updatePhoneNumber(credential)
        } else {
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser != null) {
                        if (name != null && surname != null) {
                            userData.setUserData(
                                auth.currentUser!!.uid,
                                "",
                                name!!,
                                surname!!,
                                "",
                                "",
                                "",
                                phoneNumber
                            )
                        } else {
                            loading(false)
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Nomalum xatolik yuz berdi qayta urinib ko'ring",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    loading(false)
                    Toast.makeText(
                        requireActivity(),
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updatePhoneNumber(credential: PhoneAuthCredential) {
        auth.currentUser?.updatePhoneNumber(credential)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (auth.currentUser != null) {
                    userData.setPhoneNumber(auth.currentUser!!.uid, phoneNumber)
                }
            } else {
                loading(false)
                Toast.makeText(
                    requireActivity(),
                    task.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun userDataObserve() {
        userData.userData.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    setPrefs(name, surname, phoneNumber.replace("+998", "").trim())
                    loading(false)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
                ResourceState.ERROR -> {
                    loading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        userData.phoneNumber.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    setPrefs(null, null, phoneNumber.replace("+998", "").trim())
                    loading(false)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
                ResourceState.ERROR -> {
                    loading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
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

    private fun setPrefs(name: String?, surname: String?, phoneNumber: String) {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        if (name != null && surname != null) {
            prefs.edit().putString("name", name).apply()
            prefs.edit().putString("surname", surname).apply()
        }
        prefs.edit().putString("phoneNumber", phoneNumber).apply()
    }
    private fun cursorVisible(isVisible: Boolean) {
        binding.apply {
            code1.isCursorVisible = isVisible
            code2.isCursorVisible = isVisible
            code3.isCursorVisible = isVisible
            code4.isCursorVisible = isVisible
            code5.isCursorVisible = isVisible
            code6.isCursorVisible = isVisible
        }
    }
}