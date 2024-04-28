package com.kotlin.weather.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils {
    companion object {
        private const val PREFERENCES_NAME = "com.kotlin.weather"
        private var sharedPreferences: SharedPreferences ?= null

        //Shared preferences'i oluşturdum
        private fun getSharedPreferences(context: Context): SharedPreferences {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            }
            return sharedPreferences!!
        }

        // Veri ekleme
        fun putString(context: Context, key: String, value: String) {
            val sharedPreferencesEditor: SharedPreferences.Editor = getSharedPreferences(context).edit()
            sharedPreferencesEditor.putString(key,value).apply()
        }

        //Veri alma
        fun getString(context: Context, key: String): String {
            return getSharedPreferences(context).getString(key,"London").toString()
        }

        //Veri silme
        fun removeString(context: Context,key: String?){
            val sharedPreferencesEditor: SharedPreferences.Editor = getSharedPreferences(context).edit()
            sharedPreferencesEditor.remove(key).apply()
        }
    }

}