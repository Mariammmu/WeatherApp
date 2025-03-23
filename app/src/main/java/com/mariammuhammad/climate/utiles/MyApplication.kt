package com.mariammuhammad.climate.utiles

import android.app.Application

class MyApplication :Application() {
        override fun onCreate() {
            super.onCreate()
            MySharedPrefs().init(this)
        }
    }
