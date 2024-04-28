package com.kotlin.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.weather.R
import com.kotlin.weather.databinding.RecyclerDayRowBinding
import com.kotlin.weather.model.Forecastday
import com.kotlin.weather.util.getWeatherIconResource
import com.kotlin.weather.util.isDayTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DayForecastAdapter(private val forecastDay : Forecastday) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    //ViewHolder sınıfı için RecyclerDayRowBinding tuttuğunu belirttik.
    class ViewHolder(val binding : RecyclerDayRowBinding) : RecyclerView.ViewHolder(binding.root)

    //RecyclerDayRowBinding artık bağlandı.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerDayRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    //Forecastday verisi kadar alan tutacağımızı belirttik
    override fun getItemCount(): Int {
        return forecastDay.hour.size
    }

    //RecyclerDayRowBinding için oluşturulan bileşenlere verilerini verdik ve sıraya göre kendisi otomatik olarak ekler
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.hourText.text = getHour(forecastDay.hour[position].time)
        holder.binding.temperature.text = String.format("%s°C", forecastDay.hour[position].temp_c.toString())
        if(isDayTime(forecastDay.hour[position].time)) {
            holder.binding.iconView.setImageResource(getWeatherIconResource(forecastDay.hour[position].condition.text))
        } else {
            holder.binding.iconView.setImageResource(R.drawable.night_icon)
        }
    }

    //Api'den gelen tarih-saat verisinden sadece saati aldık
    private fun getHour(timeString: String): String? {
        // Zaman dizesini ayrıştırma için SimpleDateFormat kullanılır
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date: Date? = dateFormat.parse(timeString) // Zaman dizesini tarihe dönüştürme

        // Saat kısmını almak için SimpleDateFormat'i yeniden kullanabiliriz
        val saatFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return date?.let { saatFormat.format(it) }
    }
}