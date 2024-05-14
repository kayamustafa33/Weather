package com.kotlin.weather.view.mainView

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.weather.R
import com.kotlin.weather.adapter.DayForecastAdapter
import com.kotlin.weather.adapter.WeeklyForecastAdapter
import com.kotlin.weather.databinding.FragmentWeatherBinding
import com.kotlin.weather.model.Favorite
import com.kotlin.weather.model.Forecast
import com.kotlin.weather.model.Forecastday
import com.kotlin.weather.util.SharedPreferencesUtils
import com.kotlin.weather.viewModel.FavoriteViewModel
import com.kotlin.weather.viewModel.WeatherViewModel

class WeatherFragment : Fragment() {

    private lateinit var dialog: Dialog
    private lateinit var binding : FragmentWeatherBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var dayForecastAdapter: DayForecastAdapter
    private lateinit var weatherForecastList: Forecastday
    private lateinit var weeklyForecastAdapter: WeeklyForecastAdapter
    private lateinit var forecastWeekly : Forecast
    private lateinit var location : String
    private lateinit var cityEditText : EditText
    private lateinit var setLocationButton : Button
    private lateinit var favoriteList : ArrayList<Favorite>

    private val favoriteViewModel: FavoriteViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Data binding ile layoutumuzu birleştirdik
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)

        favoriteList = ArrayList()

        //View modeli başlattık.
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        //Data binding'i view modelimiz ile birleştirdik
        binding.weatherViewModel = weatherViewModel
        binding.lifecycleOwner = this

        //Local verimiz eğer null ise default olarak London verdik
        location = SharedPreferencesUtils.getString(binding.root.context,"locationData")

        //viewModelden veri almak için fonksiyonumuzu çağırdık
        weatherViewModel.getWeatherDataFromViewModel(binding.root.context, location, binding.weatherIconImageView, binding.layout)

        //Fonksiyonları çağırdık
        fetchData()
        changeCityIconClicked()
        bottomSheetInit()
        updateWeather()
        observeFavoriteData()
        favoriteIconClicked()
        observeCurrentLocation()

        return binding.root
    }

    private fun favoriteIconClicked() {
        binding.favoriteSaveIcon.setOnClickListener {
            val isLocationFavorite = isLocationFavorite(location)
            if (isLocationFavorite) {
                // Konum favori olarak kaydedilmiş, silme işlemi yap
                favoriteViewModel.deleteLocation(location) { success ->
                    if (success) {
                        binding.favoriteSaveIcon.setImageResource(R.drawable.save)
                    }
                }
            } else {
                // Konum favori olarak kaydedilmemiş, ekleme işlemi yap
                favoriteViewModel.insertLocation(location) { success ->
                    if (success) {
                        binding.favoriteSaveIcon.setImageResource(R.drawable.saved)
                    }
                }
            }
        }
    }

    //favorilere eklenmiş mi kontrol ediyoruz
    private fun isLocationFavorite(location: String): Boolean {
        return favoriteViewModel.favoritesData.value?.any { it.location == location } ?: false
    }

    private fun observeFavoriteData() {
        favoriteViewModel.favoritesData.observe(viewLifecycleOwner) { favoriteList ->
            favoriteList?.let {
                // Eğer konum listede ekliyse resmi değiştirir
                if (isLocationFavorite(location)) {
                    binding.favoriteSaveIcon.setImageResource(R.drawable.saved)
                }
            }
        }
    }

    //Anlık olarak şehir değişince resmi de favorilerde olup olmadığını kontrol eder
    private fun observeCurrentLocation() {
        weatherViewModel.currentLocation.observe(viewLifecycleOwner) { currentLocation ->
            if(currentLocation != null) {
                if (isLocationFavorite(currentLocation)) {
                    binding.favoriteSaveIcon.setImageResource(R.drawable.saved)
                } else {
                    binding.favoriteSaveIcon.setImageResource(R.drawable.save)
                }
            }
        }
    }

    private fun fetchData() {
        weatherViewModel.weatherData.observe(viewLifecycleOwner) {
            it?.let { data ->
                //recycler view'a layout atadık.
                binding.daysForecastRV.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.HORIZONTAL, false)
                val forecastDay = Forecastday(data.forecast.forecastday[0].astro,data.forecast.forecastday[0].date,data.forecast.forecastday[0].date_epoch,data.forecast.forecastday[0].day,data.forecast.forecastday[0].hour)
                weatherForecastList = Forecastday(forecastDay.astro,forecastDay.date,forecastDay.date_epoch,forecastDay.day,forecastDay.hour)
                //Adapteri başlattık
                dayForecastAdapter = DayForecastAdapter(weatherForecastList)
                binding.daysForecastRV.adapter = dayForecastAdapter
                //Her veri eklendiğinde veya silindiğinde adapter sınıfına haber verdik.
                dayForecastAdapter.notifyDataSetChanged()

                //recycler view'a layout atadık.
                binding.weeklyForecastRV.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
                forecastWeekly = Forecast(data.forecast.forecastday)
                //Adapteri başlattık
                weeklyForecastAdapter = WeeklyForecastAdapter(forecastWeekly)
                binding.weeklyForecastRV.adapter = weeklyForecastAdapter
                //Her veri eklendiğinde veya silindiğinde adapter sınıfına haber verdik.
                weeklyForecastAdapter.notifyDataSetChanged()
            }
        }
    }

    //Icon'a tıklanınca bottom sheet dialogu gösterir.
    private fun changeCityIconClicked() {
        binding.changeCityIcon.setOnClickListener {
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.BottomSheetAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
    }

    //Şehir girildikten sonra yeni lokasyonu ayarlar ve shared dataya ekler.
    private fun updateWeather() {
        setLocationButton.setOnClickListener {
            if(TextUtils.isEmpty(cityEditText.text.toString())) {
                Toast.makeText(binding.root.context, getString(R.string.the_city_cannot_be_left_empty),Toast.LENGTH_SHORT).show()
            } else {
                val newLocation = cityEditText.text.toString()
                dialog.cancel()
                SharedPreferencesUtils.putString(binding.root.context,"locationData",cityEditText.text.toString())
                weatherViewModel.getWeatherDataFromViewModel(binding.root.context,newLocation,binding.weatherIconImageView,binding.layout)
                cityEditText.text.clear()
            }
        }
    }

    //Dialogu oluşturduk ve layoutumuz ile bağladık.
    private fun bottomSheetInit() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.city_bottom_sheet)

        //Dialog içerisindeki bileşenleri tanımladık
        cityEditText = dialog.findViewById(R.id.cityEditText)
        setLocationButton = dialog.findViewById(R.id.setCityTextBtn)

    }

}