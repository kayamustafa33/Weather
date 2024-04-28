package com.kotlin.weather.viewModel

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.weather.R
import com.kotlin.weather.contracts.WeatherImplementor
import com.kotlin.weather.model.WeatherForecastResponse
import com.kotlin.weather.service.WeatherApiClient
import com.kotlin.weather.util.CustomProgressDialog
import com.kotlin.weather.util.getWeatherIconResource
import com.kotlin.weather.util.isDayTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeatherViewModel : ViewModel(), WeatherImplementor {

    //Live data oluşturduk.
    val weatherData = MutableLiveData<WeatherForecastResponse?>()

    override fun getWeatherDataFromViewModel(context: Context,location : String,weatherIcon : ImageView,layout: LinearLayout) {
        //Fonksiyon çağırılınca progress bar gösterir
        val customProgressDialog = CustomProgressDialog(context)
        customProgressDialog.show()
        //Coroutine ile api'den aldığımız verileri Main threadde çalıştırıyoruz
        CoroutineScope(Dispatchers.Main).launch {
            val weatherApiClient = WeatherApiClient()
            try {
                //Veriyi alıp response değişkenine atadık
                val response = withContext(Dispatchers.IO) {
                    weatherApiClient.getCurrentWeather(capitalizeFirstLetters(location))
                }
                //Live data'ya response verimizi atadık
                weatherData.value = response
                customProgressDialog.dismiss()

                //Eğer gece ise güneş gösterme ihtimalini önledik
                if(isDayTime(response.location.localtime).not()) {
                    weatherIcon.setImageResource(R.drawable.night_icon)
                } else {
                    weatherIcon.setImageResource(getWeatherIconResource(response.current.condition.text))
                }
                //Arka planı gündüz ve geceye göre farklı resimler ayarladık
                layout.setBackgroundResource(setBackgroundImage(response.location.localtime))
            } catch (e: Exception) {
                weatherData.value = null
                customProgressDialog.dismiss()
            }
        }
    }

    //Girilen şehir metninde ilk harfleri büyük hale getirir
    private fun capitalizeFirstLetters(input: String): String {
        return input.split(" ").joinToString(" ") { it.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(
                Locale.ROOT
            ) else char.toString()
        } }
    }

    //Arka plan resmini saate göre ayarlayan fonksiyon
    private fun setBackgroundImage(time : String) : Int {
        return if(isDayTime(time)) {
            R.drawable.morning_bg
        } else {
            R.drawable.night_bg
        }
    }
}