package com.bizmiz.umidjonmarket111.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val image_url: String = "",
    val categoryId: String = "",
    val type: String = "",
    val startPrice: String = "",
    val endPrice: String = "",
    val percent: String = "",
    val view: Int = 0,
    val create_data: Long = 0
):Parcelable
