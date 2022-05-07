package com.bizmiz.umidjonmarket111.di

import android.content.Context
import com.bizmiz.umidjonmarket111.auth.get_started.sms_verify.UserDataHelper
import com.bizmiz.umidjonmarket111.auth.get_started.sms_verify.UserDataViewModel
import com.bizmiz.umidjonmarket111.bottom_nav.home.ExclusiveAdapter
import com.bizmiz.umidjonmarket111.bottom_nav.home.PopularProductAdapter
import com.bizmiz.umidjonmarket111.bottom_nav.home.ProductDataHelper
import com.bizmiz.umidjonmarket111.bottom_nav.home.ProductsViewModel
import com.bizmiz.umidjonmarket111.bottom_nav.home.category.CategoryDataHelper
import com.bizmiz.umidjonmarket111.bottom_nav.home.category.CategoryViewModel
import com.bizmiz.umidjonmarket111.bottom_nav.profile.edit_profile.GetDistrictViewModel
import com.bizmiz.umidjonmarket111.helper.retrofit.ApiClient
import com.bizmiz.umidjonmarket111.helper.retrofit.NetworkHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedPreferencesModule = module {
    single {
        androidApplication().applicationContext.getSharedPreferences(
            "com.bizmiz.umidjon-market.preferences",
            Context.MODE_PRIVATE
        )
    }
}
val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseStorage.getInstance().reference }
    single { ApiClient.getClient() }
    single { NetworkHelper(get()) }
    single { UserDataHelper(get()) }
    single { CategoryDataHelper(get()) }
    single { ProductDataHelper(get()) }

}
val viewModelModule = module {
    viewModel { GetDistrictViewModel(get()) }
    viewModel { UserDataViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductsViewModel(get()) }
}