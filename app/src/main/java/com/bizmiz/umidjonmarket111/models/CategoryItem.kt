package com.bizmiz.umidjonmarket111.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryItem(
    val id: String = "",
    val image_url: String = "",
    val name: String = "",
    val create_data: Long = 0
):Parcelable
