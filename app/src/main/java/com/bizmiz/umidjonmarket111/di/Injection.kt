package com.bizmiz.umidjonmarket111.di

import com.bizmiz.umidjonmarket111.helper.retrofit.ApiClient
import com.bizmiz.umidjonmarket111.bottom_nav.profile.edit_profile.GetDistrictViewModel
import com.bizmiz.umidjonmarket111.helper.retrofit.NetworkHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseStorage.getInstance().reference }
    single { ApiClient.getClient() }
    single { NetworkHelper(get()) }

}

val viewModelModule = module {
    viewModel { GetDistrictViewModel(get()) }
}