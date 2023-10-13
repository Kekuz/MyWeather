package com.example.mywether

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val sourceWeekDay: TextView = itemView.findViewById(R.id.sourceWeekDay)
    private val sourceIcon: ImageView = itemView.findViewById(R.id.sourceIcon)
    private val sourceTemperature: TextView = itemView.findViewById(R.id.sourceTemperature)
    private val sourceLayout: androidx.constraintlayout.widget.ConstraintLayout =
        itemView.findViewById(R.id.sourceLayout)

    fun bind(model:ForecastDay) {
        val date = LocalDate.parse(model.date.take(10))
        val dow = date.dayOfWeek.name
        sourceWeekDay.text = when(dow){
            "MONDAY" -> "Понедельник"
            "TUESDAY" -> "Вторник"
            "WEDNESDAY" -> "Среда"
            "THURSDAY" -> "Четверг"
            "FRIDAY" -> "Пятница"
            "SATURDAY"-> "Суббота"
            "SUNDAY" -> "Воскресенье"
            else -> "Error"
        }

        if (model.day.avgtemp_c > 0)
            sourceTemperature.text = "+${model.day.avgtemp_c} °C"
        else sourceTemperature.text = "${model.day.avgtemp_c} °C"

        sourceIcon.setBackgroundResource(
            when (model.day.condition.text) {
                "Sunny", "Clear" -> R.drawable.baseline_wb_sunny_48
                else -> R.drawable.baseline_wb_cloudy_48
            }
        )

        sourceLayout.setBackgroundColor(
            itemView.context.getColor(
                when (model.day.avgtemp_c) {
                    in -60.0..-40.0 -> R.color.blue
                    in -39.0..0.0 -> R.color.white_blue
                    in 1.0..20.0 -> R.color.white_orange
                    in 21.0..40.0 -> R.color.orange
                    else -> R.color.black
                }
            )
        )
    }


}