package com.example.mywether

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.LocaleManagerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mywether.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        //binding.recyclerView.adapter = WeatherAdapter(listOf())

        binding.poweredByLinkTv.setOnClickListener{
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.powered_link))
                startActivity(this)
            }
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        viewModel.enabledPermissionLive.observe(this) {
            requestPermission()
        }

        viewModel.resultLive.observe(this) {
            with(binding) {
                //recyclerView.adapter = it

                if(it.current.is_day == 1){
                    weatherCl.setBackgroundColor(getColor(R.color.day))
                }else{
                    weatherCl.setBackgroundColor(getColor(R.color.night))
                }

                currentTemperature.text = "${it.current.temp_c.roundToInt()}°C"
                Glide.with(this@MainActivity)
                    .load("https:${it.current.condition.icon.replace("64", "128")}")
                    //.placeholder(R.drawable.big_trackplaceholder)
                    //.centerCrop()
                    .into(currentIcon)

                viewModel.cityLive.observe(this@MainActivity) { city ->
                    currentCity.text = city
                }

                /*tomorrowTv.text = getDayOfWeek(it.forecast.forecastday[1].date)
                Glide.with(this@MainActivity)
                    .load("https:${it.forecast.forecastday[1].day.condition.icon.replace("64", "128")}")
                    //.placeholder(R.drawable.big_trackplaceholder)
                    //.centerCrop()
                    .into(tomorrowCondIv)
                tomorrowTempTv.text = "${it.forecast.forecastday[1].day.mintemp_c.toInt()}°C / ${it.forecast.forecastday[1].day.maxtemp_c.toInt()}°C"

                afterTomorrowTv.text = getDayOfWeek(it.forecast.forecastday[2].date)
                Glide.with(this@MainActivity)
                    .load("https:${it.forecast.forecastday[2].day.condition.icon.replace("64", "128")}")
                    //.placeholder(R.drawable.big_trackplaceholder)
                    //.centerCrop()
                    .into(afterTomorrowCondIv)
                afterTomorrowTempTv.text = "${it.forecast.forecastday[2].day.mintemp_c.toInt()}°C / ${it.forecast.forecastday[2].day.maxtemp_c.toInt()}°C"*/

                windKphTv.text = "${(it.current.wind_kph / 3.6).roundToInt()} м/с"
                windArrowIv.animate().rotation(it.current.wind_degree.toFloat()) //тут анмация
                //windArrowIv.rotation = it.current.wind_degree.toFloat() //тут анимации нет

                precipMmTv.text = "${it.current.precip_mm.toInt()} мм"
                //Капельки не нарисовали, ставить нечего...

                humidityPercentTv.text = "${it.current.humidity}%"

                cloudPercentTv.text = "${it.current.cloud}%"

                loadPb.isVisible = false
            }

        }
        binding.currentFl.setOnClickListener {
            Toast.makeText(this,"Тык!", Toast.LENGTH_LONG).show()
        }


    }

    //этот ужасный ужас нужно отсюда убрать во viewmodel
    private fun getDayOfWeek(string: String): String =
        when (LocalDate.parse(string.take(10)).dayOfWeek.name) {
            "MONDAY" -> "Пн"
            "TUESDAY" -> "Вт"
            "WEDNESDAY" -> "Ср"
            "THURSDAY" -> "Чт"
            "FRIDAY" -> "Пт"
            "SATURDAY" -> "Сб"
            "SUNDAY" -> "Вс"
            else -> "-"
        }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getLastKnownLocation()
            } else {
                Toast.makeText(this, "Denied", Toast.LENGTH_LONG).show()
                binding.loadPb.isVisible = false
            }
        }
    }

}
