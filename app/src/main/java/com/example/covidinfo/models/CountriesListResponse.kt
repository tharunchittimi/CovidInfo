package com.example.covidinfo.models


import com.google.gson.annotations.SerializedName

class CountriesListResponse : ArrayList<CountriesListResponse.CountriesListResponseItem>(){
    data class CountriesListResponseItem(
        @SerializedName("alpha2code")
        val alpha2code: String?,
        @SerializedName("alpha3code")
        val alpha3code: String?,
        @SerializedName("latitude")
        val latitude: Double?,
        @SerializedName("longitude")
        val longitude: Double?,
        @SerializedName("name")
        val name: String?
    ):BaseCountriesHeaderModel()
}