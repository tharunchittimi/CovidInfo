package com.example.covidinfo.network

import com.example.covidinfo.models.CountriesListResponse
import com.example.covidinfo.models.CountryCasesDetailsResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url


interface API {
    @GET
    fun fetchCountriesData(
        @Header("x-rapidapi-key") rapidApi: String, @Url url: String
    ): Call<CountriesListResponse>

    @GET
    fun fetchCountryCasesDetails(
        @Header("x-rapidapi-key") rapidApi: String,
        @Header("x-rapidapi-host") rapidApiHost: String,
        @Url url: String
    ): Call<CountryCasesDetailsResponse>
}