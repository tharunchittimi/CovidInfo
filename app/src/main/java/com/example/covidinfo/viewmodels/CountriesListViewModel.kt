package com.example.covidinfo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidinfo.MyApplication
import com.example.covidinfo.models.CountriesListResponse
import com.example.covidinfo.network.ApiHelper
import com.example.covidinfo.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountriesListViewModel : ViewModel() {

    private var countriesList = MutableLiveData<CountriesListResponse>()
    private var loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getCountriesList(): LiveData<CountriesListResponse> {
        return countriesList
    }

    fun loadingObservable(): LiveData<Boolean> {
        return loadingLiveData
    }

    fun getCountriesListData() {
        loadingLiveData.value = true
        ApiHelper.getApi()
            .fetchCountriesData(
                "c76920adb1msh335f15c0d0f3ea0p13e2f4jsnce0810631275",
                "https://covid-19-data.p.rapidapi.com/help/countries"
            )
            .enqueue(object : Callback<CountriesListResponse> {
                override fun onResponse(
                    call: Call<CountriesListResponse>,
                    response: Response<CountriesListResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        countriesList.value = response.body()
                    } else {
                        Utils.showToast(MyApplication.getApplicationContext(), response.message())
                    }
                }

                override fun onFailure(call: Call<CountriesListResponse>, t: Throwable) {
                    loadingLiveData.value = false
                    t.message?.let { Utils.showToast(MyApplication.getApplicationContext(), it) }
                }

            })
    }
}