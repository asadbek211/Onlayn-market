package com.bizmiz.umidjonmarket111.bottom_nav.profile.edit_profile

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.ResourceState
import com.bizmiz.umidjonmarket111.databinding.FragmentEditProfileBinding
import com.google.android.gms.location.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class EditProfileFragment : Fragment() {
    private val district: GetDistrictViewModel by viewModel()
    private var maleChecked = false
    private var femaleChecked = false
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        requireActivity().window.statusBarColor =
//            ContextCompat.getColor(requireActivity(), R.color.loading_color)
        binding = FragmentEditProfileBinding.bind(
            inflater.inflate(
                R.layout.fragment_edit_profile,
                container,
                false
            )
        )
        binding.loadingAnim.playAnimation()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        binding.rdbFemale.setOnClickListener {
            if (femaleChecked) {
                binding.rdbFemale.isChecked = false
                femaleChecked = false
            } else {
                if (maleChecked) {
                    binding.rdbMale.isChecked = false
                    maleChecked = false
                }
                binding.rdbFemale.isChecked = true
                femaleChecked = true
            }
        }
        binding.rdbMale.setOnClickListener {
            if (maleChecked) {
                binding.rdbMale.isChecked = false
                maleChecked = false
            } else {
                if (femaleChecked) {
                    binding.rdbFemale.isChecked = false
                    femaleChecked = false
                }
                binding.rdbMale.isChecked = true
                maleChecked = true
            }
        }
        binding.loginEditBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val mYear: Int = calendar.get(Calendar.YEAR)
            val mMonth: Int = calendar.get(Calendar.MONTH)
            val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
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
            picImageIntent()
        }
        binding.loginEditLocation.setOnClickListener {
            if (networkCheck()){
                binding.loginEditLocation.setTextColor(resources.getColor(R.color.black))
                binding.loginEditLocation.text = "Joylashuv olinmoqda..."
                getLastLocation()
            }else{
                Toast.makeText(requireContext(), "Internetga ulanmagansiz", Toast.LENGTH_SHORT).show()
                binding.loginEditLocation.setTextColor(resources.getColor(android.R.color.holo_red_light))
                binding.loginEditLocation.text = "Joylashuv olinmadi!"
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
                        district.getDistrict(
                            "geocodejson",
                            "39.704229",
                            "64.700380"
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
                    binding.userImageEdit.setImageURI(data.data)
                }
            }
        }
    }
    private fun networkCheck():Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }
}