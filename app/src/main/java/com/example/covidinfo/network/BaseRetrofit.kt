package com.example.covidinfo.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object BaseRetrofit : BaseOkHttp() {

    private var retrofit: Retrofit? = null
    private const val baseUrl = "https://covid-19-data.p.rapidapi.com"

    init {
        createRetrofit()
    }

    fun getMyRetrofit(): Retrofit {
        if (retrofit == null) {
            createRetrofit()
        }
        return retrofit!!
    }

    private fun createRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOKHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}