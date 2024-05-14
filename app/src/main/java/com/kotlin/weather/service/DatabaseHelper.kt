package com.kotlin.weather.service

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kotlin.weather.model.Favorite

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "favorite_locations.db"
        private const val TABLE_NAME = "favorite_locations"
        private const val COLUMN_ID = "id"
        private const val COLUMN_LOCATION = "location"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_LOCATION TEXT UNIQUE)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertLocation(location: String, callback : (Boolean) -> Unit) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_LOCATION, location)

        try {
            db.insertOrThrow(TABLE_NAME, null, contentValues)
            callback(true)
        } catch (e: SQLiteConstraintException) {
            callback(false)
        }
    }


    fun getAllLocations(): ArrayList<Favorite> {
        val locationsList = ArrayList<Favorite>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor?.let {
            val columnIndex = it.getColumnIndex(COLUMN_LOCATION)
            if (columnIndex != -1) {
                if (it.moveToFirst()) {
                    val idColumnIndex = it.getColumnIndex(COLUMN_ID)
                    val locationColumnIndex = it.getColumnIndex(COLUMN_LOCATION)

                    if (idColumnIndex != -1 && locationColumnIndex != -1) {
                        do {
                            val id = it.getInt(idColumnIndex)
                            val location = it.getString(locationColumnIndex)
                            val favorite = Favorite(id,location)
                            locationsList.add(favorite)
                        } while (it.moveToNext())
                    }
                }
            }
            it.close()
        }
        return locationsList
    }


    fun deleteLocation(location: String, callback: (Boolean) -> Unit) {
        val db = this.writableDatabase

        try {
            db.delete(TABLE_NAME, "$COLUMN_LOCATION = ?", arrayOf(location))
            callback(true)
        } catch (e: SQLiteConstraintException) {
            callback(false)
        }
    }
}