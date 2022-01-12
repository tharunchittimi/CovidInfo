package com.example.covidinfo.models


import com.google.gson.annotations.SerializedName

class CountryCasesDetailsResponse : ArrayList<CountryCasesDetailsResponse.CovidDetailsResponseItem>(){
    data class CovidDetailsResponseItem(
        @SerializedName("code")
        val code: String?,
        @SerializedName("confirmed")
        val confirmed: Int?,
        @SerializedName("country")
        val country: String?,
        @SerializedName("critical")
        val critical: Int?,
        @SerializedName("deaths")
        val deaths: Int?,
        @SerializedName("lastChange")
        val lastChange: String?,
        @SerializedName("lastUpdate")
        val lastUpdate: String?,
        @SerializedName("latitude")
        val latitude: Double?,
        @SerializedName("longitude")
        val longitude: Double?,
        @SerializedName("recovered")
        val recovered: Int?
    )
}