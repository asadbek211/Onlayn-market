package com.bizmiz.umidjonmarket111.models.Geo

data class Place(
    val address_components:Array<AddressComponent>,
    val adr_address:String,
    val business_status:String,
    val formatted_address:String,
    val formatted_phone_number:String,
    val geometry:Geometry

)
