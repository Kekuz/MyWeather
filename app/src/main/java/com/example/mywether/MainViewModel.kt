package com.example.mywether


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    val resultLive = MutableLiveData<WeatherAdapter>()

    fun getWeatherInfo(){
        CoroutineScope(Dispatchers.IO).launch {
            val currentWeather = RetrofitBuilder.weatherService.getWeather(
                RetrofitBuilder.apiKey,
                "Rostov-on-Don",
                "no"
            )
            Log.d("MyLog", currentWeather.toString())
            resultLive.postValue(WeatherAdapter(listOf(currentWeather)))
        }

    }


}