package com.mariammuhammad.climate.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.mariammuhammad.climate.R

class Screens {


   @Preview(showSystemUi = true)
    @Composable
    fun SplashScreen() {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_weather)
        )

        var isPlaying by remember { mutableStateOf(true) }

        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying
        )

        LaunchedEffect(key1 = progress) {
            if (progress == 0f) {
                isPlaying = true
            }
            if (progress == 1f) {
                isPlaying = false
            }

        }
        //background( Brush.verticalGradient(
        //            colors = listOf(Color.Cyan, Color.Magenta))),
//.background(Color.Transparent),
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(color = colorResource(R.color.background)),
                                .background(brush = Brush.linearGradient(
                colors = listOf(colorResource(R.color.background),
                    colorResource(R.color.violet))
                                )),
                    verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(400.dp),
                progress = progress
            )

            Text(
                stringResource(R.string.welcome_app),
                fontSize = 24.sp, fontWeight = FontWeight.Bold,
                color = colorResource(R.color.off_white),
                fontFamily = FontFamily(Font(R.font.pacifico))
            )
        }

    }

@Preview
    @Composable
    fun HomeScreen() {

        Box (modifier = Modifier.fillMaxSize()
//            .background(brush = Brush.linearGradient(
//                colors = listOf(colorResource(R.color.background),
//                    colorResource(R.color.violet)
                    )

        {
            Image(
                painter = painterResource(id = R.drawable.weather), // Use your drawable resource
                contentDescription = null, // You can add description for accessibility if needed
                modifier = Modifier.fillMaxSize() // Ensure the image fills the entire Box
            )

            Column (modifier = Modifier.fillMaxSize()){
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    item {
                        Text(text="Mostly Cloudy",
                            fontSize = 24.sp, fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.off_white),
                            fontFamily = FontFamily(Font(R.font.pacifico))
                        )
                    }
                }
            }

    }
}

    @Composable
    fun FavoriteScreen() {

    }

    @Composable
    fun SettingScreen() {

    }

    @Composable
    fun WeatherAlertScreen() {

    }

}