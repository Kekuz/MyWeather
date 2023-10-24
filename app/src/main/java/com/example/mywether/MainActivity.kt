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
import androidx.appcompat.app.AppCompatDelegate
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
                forecastRv.adapter = WeatherAdapter(it.forecast.forecastday)

                if(it.current.is_day == 1){
                    weatherCl.setBackgroundColor(getColor(R.color.day))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }else{
                    weatherCl.setBackgroundColor(getColor(R.color.night))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                currentTemperature.text = "${it.current.temp_c.roundToInt()}°C"
                Glide.with(this@MainActivity)
                    .load("https:${it.current.condition.icon.replace("64", "128")}")
                    .into(currentIcon)

                viewModel.cityLive.observe(this@MainActivity) { city ->
                    currentCity.text = city
                }

                windKphTv.text = "${(it.current.wind_kph / 3.6).roundToInt()} м/с"
                windArrowIv.animate().rotation(it.current.wind_degree.toFloat()) //тут анмация
                //windArrowIv.rotation = it.current.wind_degree.toFloat() //тут анимации нет

                precipMmTv.text = "${it.current.precip_mm.toInt()} мм"
                //Капельки не нарисовали, ставить нечего...

                humidityPercentTv.text = "${it.current.humidity}%"

                cloudPercentTv.text = "${it.current.cloud}%"

                loadPb.isVisible = false

                var code = viewModel.translate.map[it.current.condition.code]

                //Костыль для выбора ясно/солнечно
                if(it.current.condition.code == 1000){
                    code += it.current.is_day
                }

                currentFl.setOnClickListener {_->
                    Toast.makeText(this@MainActivity,"$code", Toast.LENGTH_SHORT).show()
                }
            }



        }


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
