package com.bizmiz.umidjonmarket111.bottom_nav.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.umidjonmarket111.helper.retrofit.NetworkHelper
import com.bizmiz.umidjonmarket111.Resource
import com.bizmiz.umidjonmarket111.models.api.Result
import retrofit2.Response

class GetDistrictViewModel(private val networkHelper: NetworkHelper) : ViewModel() {
    private val setDistrict: MutableLiveData<Resource<Response<Result>?>> = MutableLiveData()
    val district: LiveData<Resource<Response<Result>?>>
        get() = setDistrict

    fun getDistrict(format: String, lat: String, lon: String) {
        networkHelper.getDistrict(format, lat, lon, {
            setDistrict.value = Resource.success(it)
        }, {
            setDistrict.value = Resource.error(it)
        })
    }
}