package com.bizmiz.umidjonmarket111.models.Geo

data class PlacesNearbySearchResponse(
    val html_attributions:Array<String>,
    val results:Array<Place>,
    val status:String,
    val error_message:String,
    val info_messages:Array<String>,
    val next_page_token:String
)
