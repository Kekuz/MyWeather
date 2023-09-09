package com.example.mywether

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val apiKey = "bdfd66d7ccc64e92881172652230509"
    private val weatherBaseUrl = "https://api.weatherapi.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getBtn = findViewById<Button>(R.id.get_btn)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(weatherBaseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherAPI::class.java)

        getBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val currentWeather = weatherService.getWeather(apiKey, "Rostov-on-Don", "no")
                Log.d("MyLog", currentWeather.toString())
                runOnUiThread {
                    //weatherTV.text = currentWeather.current.temp_c.toString()
                    recyclerView.adapter = WeatherAdapter(listOf(currentWeather))
                }
            }
        }

        //val newsAdapter = WeatherAdapter(weatherList)
        //recyclerView.adapter = newsAdapter


    }

}
