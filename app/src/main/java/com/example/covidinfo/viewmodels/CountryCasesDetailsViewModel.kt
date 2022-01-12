package com.example.covidinfo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidinfo.MyApplication
import com.example.covidinfo.models.CountriesListResponse
import com.example.covidinfo.models.CountryCasesDetailsResponse
import com.example.covidinfo.network.ApiHelper
import com.example.covidinfo.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryCasesDetailsViewModel : ViewModel() {

    private var countryCasesData = MutableLiveData<CountryCasesDetailsResponse>()
    private var loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getCountryCases(): LiveData<CountryCasesDetailsResponse> {
        return countryCasesData
    }

    fun loadingObservable(): LiveData<Boolean> {
        return loadingLiveData
    }

    fun getCountryCasesData(isoCode: String?) {
        loadingLiveData.value = true
        ApiHelper.getApi()
            .fetchCountryCasesDetails(
                rapidApi = "c76920adb1msh335f15c0d0f3ea0p13e2f4jsnce0810631275",
                rapidApiHost = "covid-19-data.p.rapidapi.com",
                url = "https://covid-19-data.p.rapidapi.com/country/code?code=${isoCode}"
            )
            .enqueue(object : Callback<CountryCasesDetailsResponse> {
                override fun onResponse(
                    call: Call<CountryCasesDetailsResponse>,
                    response: Response<CountryCasesDetailsResponse>
                ) {
                    loadingLiveData.value = false
                    if (response.isSuccessful) {
                        countryCasesData.value = response.body()
                    } else {
                        Utils.showToast(MyApplication.getApplicationContext(), response.message())
                    }
                }

                override fun onFailure(call: Call<CountryCasesDetailsResponse>, t: Throwable) {
                    loadingLiveData.value = false
                    t.message?.let { Utils.showToast(MyApplication.getApplicationContext(), it) }
                }

            })
    }
}