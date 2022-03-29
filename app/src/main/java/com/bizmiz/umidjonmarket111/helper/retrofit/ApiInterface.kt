package com.bizmiz.umidjonmarket111.helper.retrofit

import com.bizmiz.umidjonmarket111.models.Geo.PlacesNearbySearchResponse
import com.bizmiz.umidjonmarket111.models.api.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("reverse?")
    fun getDistrict(
        @Query("format") format: String,
        @Query("lat") lat: String, @Query("lon") lon: String
    ): Call<Result>
}