package com.example.mywether


import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val resultLive = MutableLiveData<WeatherAdapter>()
    val enabledPermissionLive = MutableLiveData<Boolean>()
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    init {
        getLastKnownLocation()
    }
    private fun getWeatherInfo(city:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currentWeatherResponse = RetrofitBuilder.weatherService.getWeather(
                    RetrofitBuilder.apiKey,
                    city,
                    "3",
                    "no",
                    "no",
                )
                if (currentWeatherResponse.isSuccessful) {
                    Log.e("MyLog", currentWeatherResponse.body()?.forecast?.forecastday.toString())
                    resultLive.postValue(WeatherAdapter(currentWeatherResponse.body()?.forecast?.forecastday!!))
                }
            } catch (e: Exception) {
                Log.e("MyLog", e.message.toString())
            }

        }
    }

    private fun getCityName(lat: Double, long: Double): String {
        var cityName: String?
        val geoCoder = Geocoder(getApplication(), Locale.ENGLISH)
        val address = geoCoder.getFromLocation(lat, long, 1)
        cityName = address?.get(0)?.adminArea
        if (cityName == null) {
            cityName = address?.get(0)?.locality
            if (cityName == null) {
                cityName = address?.get(0)?.subAdminArea
            }
        }
        return cityName!!
    }

    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            enabledPermissionLive.value = true
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    //cityLive.value = getCityName(it.latitude, it.longitude)
                    Log.e("location", "${it.latitude} ${it.longitude}")
                    getWeatherInfo(getCityName(it.latitude, it.longitude))
                }

            }
    }


}