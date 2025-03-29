package com.mariammuhammad.climate.utiles

class ImageIcon {

    companion object {  //gives me the same effect of static
        private const val TEMP_ICON_BASE_RUL = "https://openweathermap.org/img/w/"
        private const val TEMP_ICON_EXTENSION = ".png"

        fun getWeatherImage(iconCode: String) =
            TEMP_ICON_BASE_RUL + iconCode + TEMP_ICON_EXTENSION
    }
}