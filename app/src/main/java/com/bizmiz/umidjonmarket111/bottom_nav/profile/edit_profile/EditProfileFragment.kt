package com.bizmiz.umidjonmarket111.bottom_nav.profile.edit_profile

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.ResourceState
import com.bizmiz.umidjonmarket111.auth.container.ContainerActivity
import com.bizmiz.umidjonmarket111.auth.get_started.sms_verify.UserDataViewModel
import com.bizmiz.umidjonmarket111.databinding.FragmentEditProfileBinding
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class EditProfileFragment : Fragment() {
    private var isAnim = true
    private lateinit var prefs: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private val district: GetDistrictViewModel by viewModel()
    private val userData: UserDataViewModel by viewModel()
    private var maleChecked = false
    private var femaleChecked = false
    private var gender = ""
    private var imageUrl: Uri? = null
    private var storageUrl:String? = null
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        binding = FragmentEditProfileBinding.bind(
            inflater.inflate(
                R.layout.fragment_edit_profile,
                container,
                false
            )
        )
        binding.loading.setOnClickListener {

        }
        if (auth.currentUser != null) {
            userData.getUserData(auth.currentUser!!.uid)
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        binding.rdbFemale.setOnClickListener {
            if (femaleChecked) {
                gender = ""
                binding.rdbFemale.isChecked = false
                femaleChecked = false
            } else {
                if (maleChecked) {
                    gender = ""
                    binding.rdbMale.isChecked = false
                    maleChecked = false
                }
                binding.rdbFemale.isChecked = true
                gender = binding.rdbFemale.text.toString()
                femaleChecked = true
            }
        }
        binding.rdbMale.setOnClickListener {
            if (maleChecked) {
                gender = ""
                binding.rdbMale.isChecked = false
                maleChecked = false
            } else {
                if (femaleChecked) {
                    gender = ""
                    binding.rdbFemale.isChecked = false
                    femaleChecked = false
                }
                binding.rdbMale.isChecked = true
                gender = binding.rdbMale.text.toString()
                maleChecked = true
            }
        }
        binding.loginEditBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val mYear: Int = calendar.get(Calendar.YEAR)
            val mMonth: Int = calendar.get(Calendar.MONTH)
            val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _, year, monthOfYear, dayOfMonth ->
                    if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                        binding.loginEditBirthday.text = "0$dayOfMonth.0${monthOfYear + 1}.$year"
                    } else if (monthOfYear + 1 < 10) {
                        binding.loginEditBirthday.text = "$dayOfMonth.0${monthOfYear + 1}.$year"
                    } else if (dayOfMonth < 10) {
                        binding.loginEditBirthday.text = "0$dayOfMonth.${monthOfYear + 1}.$year"
                    } else {
                        binding.loginEditBirthday.text = "$dayOfMonth.${monthOfYear + 1}.$year"
                    }
                }, mYear, mMonth, mDay
            )
            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePickerDialog.setCancelable(false)
            datePickerDialog.setOnDismissListener {
                it.dismiss()
            }
            datePickerDialog.show()
        }
        binding.btnBack.setOnClickListener {
            val message = AlertDialog.Builder(requireActivity())
            message.setMessage("O'zgarishlarni saqlaysizmi?")
                .setCancelable(false)
                .setPositiveButton("Ha") { message, _ ->
                    message.dismiss()
                }
                .setNegativeButton("Yo'q") { message, _ ->
                    message.dismiss()
                    val navController =
                        Navigation.findNavController(requireActivity(), R.id.main_container)
                    navController.popBackStack()
                }
                .create().show()
        }
        binding.editImage.setOnClickListener {
            imageAnimation()
//            picImageIntent()
        }
        binding.loginEditLocation.setOnClickListener {
            if (networkCheck()) {
                getLastLocation()
            } else {
                Toast.makeText(requireContext(), "Internetga ulanmagansiz", Toast.LENGTH_SHORT)
                    .show()
                binding.loginEditLocation.setTextColor(resources.getColor(android.R.color.holo_red_light))
                binding.loginEditLocation.text = "Joylashuv olinmadi!"
            }
        }
        binding.btnPhoneNumberEdit.setOnClickListener {
            val message = AlertDialog.Builder(requireActivity())
            message.setMessage("O'zgarishlarni saqlaysizmi?")
                .setCancelable(false)
                .setPositiveButton("Ha") { message, _ ->
                    message.dismiss()
                    setPrefs(1)
                    startActivity(Intent(requireActivity(), ContainerActivity::class.java))
                }
                .setNegativeButton("Yo'q") { message, _ ->
                    message.dismiss()
                    startActivity(Intent(requireActivity(), ContainerActivity::class.java))
                    setPrefs(1)
                }
                .create().show()

        }
        binding.loginEditPhoneNumber.text = auth.currentUser?.phoneNumber
        getUserData()
        binding.txtSave.setOnClickListener {
            setUserImage()
        }
        getUserDataObserve()
        binding.addImage.setOnClickListener {
            picImageIntent()
            imageAnimation()
        }
        binding.removeImage.setOnClickListener {
            if (storageUrl!=null && storageUrl!!.isNotEmpty()){
                if(imageUrl!=null){
                    imageAnimation()
                    imageUrl = null
                    Glide.with(this).load(storageUrl).into(binding.userImageEdit)
                }else{
                    binding.loading.visibility = View.VISIBLE
                    binding.loadingAnim.playAnimation()
                    imageAnimation()
                    removeImages()
                }

            }else{
                imageAnimation()
                imageUrl = null
                binding.userImageEdit.setImageResource(R.drawable.profile_img)
            }
        }
        return binding.root

    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isGPSEnable()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        newLocationData()
                    } else {
                        binding.loginEditLocation.setTextColor(resources.getColor(R.color.black))
                        binding.loginEditLocation.text = "Joylashuv olinmoqda..."
                        district.getDistrict(
                            "geocodejson",
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                        getDistrict()
                    }
                }
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermission()
        }
    }

    private fun newLocationData() {
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
            }
        }
        if (checkPermission()) fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun Fragment.isGPSEnable(): Boolean =
        requireContext().getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)

    private fun Context.getLocationManager() =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 1010
        )
    }

    private fun getDistrict() {
        var location = ""
        district.district.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    it.data?.body()?.features?.forEach { feature ->
                        when {
                            feature.properties.geocoding.state != "Respublikası" -> {
                                location = feature.properties.geocoding.state
                            }
                            feature.properties.geocoding.county != null -> {
                                location = feature.properties.geocoding.district
                            }
                            feature.properties.geocoding.city != null -> {
                                location = feature.properties.geocoding.city
                            }
                        }
                        binding.loginEditLocation.setTextColor(resources.getColor(R.color.black))
                        binding.loginEditLocation.text = "${
                            feature.properties.geocoding.state.replace(
                                "Respublikası",
                                ""
                            )
                        },${feature.properties.geocoding.county}"
                    }
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.loginEditLocation.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    binding.loginEditLocation.text = "Joylashuv olinmadi!"
                }
            }
        })
    }

    private fun getUserData() {
        userData.user.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    binding.loginEditName.setText(it.data?.name)
                    binding.loginEditSurname.setText(it.data?.surname)
                    binding.loginEditBirthday.text = it.data?.dateBirthday
                    binding.loginEditLocation.text = it.data?.location
                    if (it.data?.gender == "Erkak") {
                        binding.rdbMale.isChecked = true
                        maleChecked = true
                        femaleChecked = false
                        gender = binding.rdbMale.text.toString()
                    } else if (it.data?.gender == "Ayol") {
                        binding.rdbFemale.isChecked = true
                        femaleChecked = true
                        maleChecked = false
                        gender = binding.rdbFemale.text.toString()
                    }
                    storageUrl = it.data?.userPhotoUrl
                    if (storageUrl != null) {
                        if (storageUrl!!.isNotEmpty()) {
                            Glide.with(this).load(storageUrl).into(binding.userImageEdit)
                        }
                    }
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun picImageIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.ACTION_PICK, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image(s)"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    imageUrl = data.data
                    binding.userImageEdit.setImageURI(data.data)
                }
            }
        }
    }

    private fun networkCheck(): Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }

    private fun setPrefs(number: Int) {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        prefs.edit().putInt("number", number).apply()
    }

    private fun setUserImage() {
        if (imageUrl!=null){
            binding.loading.visibility = View.VISIBLE
            binding.loadingAnim.playAnimation()
            val storeRef =
                FirebaseStorage.getInstance().reference.child("${auth.currentUser?.uid}/userImage")
            storeRef.putFile(imageUrl!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                            userData.updateUserData(
                                auth.currentUser!!.uid,
                                uri.toString(),
                                binding.loginEditName.text.toString(),
                                binding.loginEditSurname.text.toString(),
                                binding.loginEditBirthday.text.toString(),
                                gender,
                                binding.loginEditLocation.text.toString(),
                                binding.loginEditPhoneNumber.text.toString()
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    binding.loading.visibility = View.GONE
                    binding.loadingAnim.pauseAnimation()
                    Toast.makeText(requireActivity(), it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
        }else if (storageUrl!=null && storageUrl!!.isEmpty()){
            binding.loading.visibility = View.VISIBLE
            binding.loadingAnim.playAnimation()
            userData.updateUserData(
                auth.currentUser!!.uid,
               "",
                binding.loginEditName.text.toString(),
                binding.loginEditSurname.text.toString(),
                binding.loginEditBirthday.text.toString(),
                gender,
                binding.loginEditLocation.text.toString(),
                binding.loginEditPhoneNumber.text.toString()
            )
        }else{
            userData.updateUserData(
                auth.currentUser!!.uid,
                "noUpdate",
                binding.loginEditName.text.toString(),
                binding.loginEditSurname.text.toString(),
                binding.loginEditBirthday.text.toString(),
                gender,
                binding.loginEditLocation.text.toString(),
                binding.loginEditPhoneNumber.text.toString()
            )
        }


    }
    private fun removeImages(){
        if (storageUrl!=null){
            val storeRef =
                FirebaseStorage.getInstance().getReferenceFromUrl(storageUrl!!)
            storeRef.delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        userData.updateUserData(auth.currentUser!!.uid,
                            "","","","","","","")

                    }
                }
                .addOnFailureListener {
                    binding.loading.visibility = View.GONE
                    binding.loadingAnim.pauseAnimation()
                    Toast.makeText(requireActivity(), it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
    private fun getUserDataObserve() {
        userData.updateUser.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceState.SUCCESS -> {
                    binding.loading.visibility = View.GONE
                    binding.loadingAnim.pauseAnimation()
                    binding.userImageEdit.setImageResource(R.drawable.profile_img)
                    binding.loading.visibility = View.GONE
                    binding.loadingAnim.pauseAnimation()
                    val navController =
                        Navigation.findNavController(requireActivity(), R.id.main_container)
                    navController.popBackStack()
                }
                ResourceState.ERROR -> {
                    binding.loading.visibility = View.GONE
                    binding.loadingAnim.pauseAnimation()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun imageAnimation(){
        if (isAnim){
            binding.editImage.setImageResource(R.drawable.ic_close_img)
            isAnim = false
            binding.addImage.animate().translationX(140f).duration = 500
            if (storageUrl!= null && storageUrl!!.isNotEmpty() || imageUrl!=null){
                binding.removeImage.animate().translationY(-140f).duration = 500
            }

        }else{
            binding.editImage.setImageResource(R.drawable.ic_edit_img)
            isAnim = true
            binding.addImage.animate().translationX(0f).duration = 500
            if (storageUrl!=null && storageUrl!!.isNotEmpty()|| imageUrl!=null){
                binding.removeImage.animate().translationY(0f).duration = 500
            }
        }

    }
}