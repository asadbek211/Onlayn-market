package com.bizmiz.umidjonmarket111.models

data class UserData(
    val userPhotoUrl: String? = "",
    val name: String = "",
    val surname: String = "",
    val dateBirthday: String? = "",
    val gender: String? = "",
    val location: String? = "",
    val phoneNumber: String = ""
)
