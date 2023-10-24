package com.example.mywether

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mywether.models.ForecastDay
import java.time.LocalDate


class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val weekdayTv: TextView = itemView.findViewById(R.id.weekday_tv)
    private val forecastConditionIv: ImageView = itemView.findViewById(R.id.forecast_condition_iv)
    private val forecastTemperatureTv: TextView =
        itemView.findViewById(R.id.forecast_temperature_tv)

    fun bind(model: ForecastDay) {
        val date = LocalDate.parse(model.date.take(10))
        val dow = date.dayOfWeek.name


        if (model.date == LocalDate.now().toString()) {
            weekdayTv.text = itemView.context.getString(R.string.today)
        } else {
            weekdayTv.text = when (dow) {
                "MONDAY" -> "Пн"
                "TUESDAY" -> "Вт"
                "WEDNESDAY" -> "Ср"
                "THURSDAY" -> "Чт"
                "FRIDAY" -> "Пт"
                "SATURDAY" -> "Сб"
                "SUNDAY" -> "Вс"
                else -> "-"
            }
        }


        forecastTemperatureTv.text =
            "${model.day.mintemp_c.toInt()}°C / ${model.day.maxtemp_c.toInt()}°C"


        Glide.with(itemView)
            .load("https:${model.day.condition.icon.replace("64", "128")}")
            .into(forecastConditionIv)

    }


}