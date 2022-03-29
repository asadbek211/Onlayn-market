package com.bizmiz.umidjonmarket111.helper.retrofit

import com.bizmiz.umidjonmarket111.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var gson: Gson
        fun getClient(): Retrofit {
            if (!Companion::gson.isInitialized) {
              gson = GsonBuilder()
                  .setLenient()
                  .create()
            }
            if (!Companion::retrofit.isInitialized){
                retrofit =Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }
    }
}