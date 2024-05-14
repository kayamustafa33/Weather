package com.kotlin.weather.viewModel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.weather.model.Favorite
import com.kotlin.weather.service.DatabaseHelper

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    //Sqlite veritabanını başlattık
    private val dbHelper = DatabaseHelper(application.applicationContext)

    val favoritesData = MutableLiveData<ArrayList<Favorite>>()

    //Direkt olarak verileri live data'ya aktardık
    init {
        favoritesData.value = dbHelper.getAllLocations()
    }

    //Yeni veri ekleme işlemi
    fun insertLocation(location: String, callback : (Boolean) -> Unit) {
        dbHelper.insertLocation(location) {
            if(it) {
                Toast.makeText(getApplication(),"Kaydedildi!",Toast.LENGTH_SHORT).show()
                favoritesData.value = dbHelper.getAllLocations()
                callback(true)
            } else {
                Toast.makeText(getApplication(),"Aynı veri bulunmaktadır!",Toast.LENGTH_SHORT).show()
                callback(false)
            }
        }
    }

    //Veri silme işlemi
    fun deleteLocation(location: String, callback : (Boolean) -> Unit) {
        dbHelper.deleteLocation(location) {
            if(it) {
                Toast.makeText(getApplication(),"Silindi",Toast.LENGTH_SHORT).show()
                favoritesData.value = dbHelper.getAllLocations()
                callback(true)
            } else {
                Toast.makeText(getApplication(),"Başarısız!",Toast.LENGTH_SHORT).show()
                callback(false)
            }
        }
    }
}