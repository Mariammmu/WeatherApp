package com.mariammuhammad.climate.utiles

import android.content.Context
import android.content.SharedPreferences

class MySharedPrefs {

    private var sharedPreferences: SharedPreferences? = null

    @Synchronized
    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        }
    }

    fun getInstance(): SharedPreferences? {
        return sharedPreferences
    }

}