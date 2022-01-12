package com.example.covidinfo.network


object ApiHelper {
    fun getApi(): API {
        return BaseRetrofit.getMyRetrofit().create(API::class.java)
    }
}