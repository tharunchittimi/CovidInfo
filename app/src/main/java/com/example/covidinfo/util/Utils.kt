package com.example.covidinfo.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.format.DateUtils
import android.widget.Toast
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

object Utils {
    fun isNetworkAvailable(context: Context?) =
        (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }

    fun getApiErrorMessage(error: Throwable): String {
        val noInternetDesc = "No Internet"
        return when (error) {
            is HttpException -> noInternetDesc
            is SocketTimeoutException -> noInternetDesc
            is IOException -> noInternetDesc
            else -> error.message ?: "Unknown Error"
        }
    }

    fun showToast(context:Context, message:String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun getCountryFlags(isoCode:String?) :String {
        return "https://flagcdn.com/h40/${isoCode?.lowercase()}.png"
    }

}