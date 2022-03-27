package com.bizmiz.umidjonmarket111.helper.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import com.bizmiz.umidjonmarket111.models.api.Result

class NetworkHelper(private val apiClient: Retrofit) {
    fun getDistrict(
        format: String, lat: String, lon: String, onSuccess: (district: Response<Result>?) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val call = apiClient.create(ApiInterface::class.java).getDistrict(format, lat, lon)
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>?, response: Response<Result>?) {
                onSuccess.invoke(response)
            }

            override fun onFailure(call: Call<Result>?, t: Throwable?) {
                onFailure.invoke(t?.localizedMessage)
            }

        })
    }
}