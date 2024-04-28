package com.kotlin.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.weather.R
import com.kotlin.weather.databinding.RecyclerWeeklyRowBinding
import com.kotlin.weather.model.Forecast
import com.kotlin.weather.util.getWeatherIconResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeeklyForecastAdapter(private val forecast: Forecast) : RecyclerView.Adapter<WeeklyForecastAdapter.ViewHolder>() {

    //ViewHolder sınıfı için RecyclerWeeklyRowBinding tuttuğunu belirttik.
    class ViewHolder(val binding : RecyclerWeeklyRowBinding) : RecyclerView.ViewHolder(binding.root)

    //RecyclerWeeklyRowBinding artık bağlandı.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerWeeklyRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    //Forecast verisi kadar alan tutacağımızı belirttik
    override fun getItemCount(): Int {
        return forecast.forecastday.size
    }

    //RecyclerWeeklyRowBinding için oluşturulan bileşenlere verilerini verdik ve sıraya göre kendisi otomatik olarak ekler
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dateText.text = forecast.forecastday[position].date
        holder.binding.conditionText.text = forecast.forecastday[position].day.condition.text
        holder.binding.temperatureText.text = String.format("%s°C", forecast.forecastday[position].day.avgtemp_c)
        holder.binding.iconView.setImageResource(getWeatherIconResource(forecast.forecastday[position].day.condition.text))
    }

}