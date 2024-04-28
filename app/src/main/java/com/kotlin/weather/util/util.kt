package com.kotlin.weather.util

import com.kotlin.weather.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//Apiden gelen resimler uygun olmadığı için kendi verilerimizi kendimiz yönetip ona göre resim ayarladık
fun getWeatherIconResource(weatherCondition: String): Int {
    return when {
        weatherCondition.contains("Cloudy", ignoreCase = true) -> R.drawable.overcast_icon
        weatherCondition.contains("Overcast", ignoreCase = true) -> R.drawable.overcast_icon
        weatherCondition.contains("Sunny", ignoreCase = true) || weatherCondition.contains("Clear", ignoreCase = true) -> R.drawable.sunny_icon
        weatherCondition.contains("rain", ignoreCase = true) || weatherCondition.contains("drizzle",ignoreCase = true)-> R.drawable.rainy_icon
        weatherCondition.contains("Snow", ignoreCase = true) -> R.drawable.snow
        weatherCondition.contains("Thundery outbreaks in nearby", ignoreCase = true) || weatherCondition.contains("Patchy light rain in area with thunder", ignoreCase = true) -> R.drawable.lightning
        else -> R.drawable.sunny_icon
    }
}

//Api'den gelen tarih-saat verisini gündüz veya gece olarak ayırır
fun isDayTime(timeString: String): Boolean {
    // Zaman dizesini ayrıştırma
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val time = dateFormat.parse(timeString)

    // Saat dilimini al
    val calendar = Calendar.getInstance()
    calendar.time = time!!

    // Saati kontrol et (örneğin, 06:00 ile 18:00 arası gündüz olarak kabul edilir)
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    return hourOfDay in 6..18
}
