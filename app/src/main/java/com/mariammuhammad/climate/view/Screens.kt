package com.mariammuhammad.climate.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mariammuhammad.climate.R
import com.mariammuhammad.climate.model.pojo.HourlyModel
import com.mariammuhammad.climate.model.pojo.Weather

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
                .background(color = colorResource(R.color.background)),
//              .background(brush = Brush.linearGradient(
//                colors = listOf(colorResource(R.color.background),
//                    colorResource(R.color.violet))
//                                )),
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

    Box(
        modifier = Modifier.fillMaxSize()
//            .background(brush = Brush.linearGradient(
//                colors = listOf(colorResource(R.color.background),
//                    colorResource(R.color.violet)
    )

    {
        Image(
            painter = painterResource(id = R.drawable.weather), // Use your drawable resource
            contentDescription = null, // You can add description for accessibility if needed
            modifier = Modifier.fillMaxSize(), // Ensure the image fills the entire Box
            contentScale = ContentScale.FillBounds
        )

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Text(
                        text = "Mostly Cloudy",
                        fontSize = 24.sp,
                        color = colorResource(R.color.off_white),
                        fontFamily = FontFamily(Font(R.font.alfa_slab)),
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 48.dp),
                        textAlign = TextAlign.Center
                    )

                    Image(
                        painter = painterResource(id = R.drawable.cloudy_sunny),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp).padding(top = 8.dp)
                    )

                    Text(
                        text = "Sat April 5 | 11:00AM",
                        fontSize = 19.sp,
                        color = colorResource(R.color.off_white),
                        fontFamily = FontFamily(Font(R.font.alfa_slab)),
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "25º C",
                        fontSize = 60.sp, fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.off_white),
                        fontFamily = FontFamily(Font(R.font.alfa_slab)),
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "H:27 L:18",
                        fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.off_white),
                        fontFamily = FontFamily(Font(R.font.alfa_slab)),
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 14.dp)
                            .background(
                                color = colorResource(R.color.background),
                                shape = RoundedCornerShape(25.dp)
                            )
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .height(100.dp).padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        //verticalAlignment = Alignment.CenterVertically: Ensures that
                        // the child elements in the Row are centered vertically.

                        //horizontalArrangement = Arrangement.SpaceBetween: Ensures
                        // the child elements in the Row are spaced out evenly,
                        // with the first child on the left, the last on the right,
                        // and the rest evenly distributed between them.
                        /*
verticalAlignment = Alignment.CenterVertically:

This property controls how the child elements within the Row are vertically aligned.

Alignment.CenterVertically: Aligns the items in the Row vertically at the center of the Row's height. So, if you have multiple items inside the Row, they will all be vertically centered along the Row.

horizontalArrangement = Arrangement.SpaceBetween:

This property controls the horizontal arrangement of the items inside the Row.

Arrangement.SpaceBetween: Distributes the children elements such that:

The first item is placed at the start (left).

The last item is placed at the end (right).

The remaining items are evenly spaced out between the first and last items, ensuring that there’s equal space between all items in the Row.
 */

                        {

                            WeatherDetailItem(icon = R.drawable.rain2, value= "22%", label = "Rain")
                            WeatherDetailItem(icon = R.drawable.wind, value= "22%", label = "Wind Speed")
                            WeatherDetailItem(icon = R.drawable.humidity, value= "22%", label = "Humidity")
                            WeatherDetailItem(icon = R.drawable.pressure, value= "981 hpa", label = "Pressure")
                            WeatherDetailItem(icon = R.drawable.clouds, value= "100%", label = "Clouds")



                        }

                    }
                    Text(text = "Today",
                        fontSize = 20.sp, color = colorResource(R.color.off_white),
                        fontFamily = FontFamily(Font(R.font.alfa_slab)),
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal= 24.dp,vertical = 8.dp),
                    )
                }
                item {
                    LazyRow (modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//means that each item in the row will be spaced by 4 dp from the next one.
//So, in short, the items in the LazyRow will have a gap of 4 dp between them horizontally.
                    )
                    {

                        items (items){ item->
                            FutureModelViewHolder(item)
                        }
                    }
                }

                item {
                    Row (modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically){

                        Text(text="Future", fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.alfa_slab)),
                            color = colorResource(R.color.off_white),
                            modifier = Modifier.weight(1f))

                        Text(text="Next 5 days", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.alfa_slab)),
                            color = colorResource(R.color.off_white)
                        )
                    }
                }
            }
        }
}
}
    @Composable
    fun FutureModelViewHolder(model: HourlyModel){

        Column (modifier = Modifier.width(90.dp).wrapContentHeight()
            .padding(4.dp).background(
                color = colorResource(R.color.background),
                shape = RoundedCornerShape(8.dp)).padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){

            Text(text = model.hour, color = colorResource(R.color.off_white),
                fontSize = 16.sp, modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center, fontFamily = FontFamily(Font(R.font.alfa_slab))
            )

            Image(
                painter = painterResource(
                    id = when (model.picPath) {
                       "cloudy" -> R.drawable.clouds
                        "sunny" -> R.drawable.sunny
                        "windy" -> R.drawable.wind2
                        "rainy" -> R.drawable.rain
                        "stormy" -> R.drawable.storm
                        else -> R.drawable.clouds
                    }
                ), contentDescription = null,
                modifier = Modifier.size(45.dp).padding(8.dp),
                contentScale = ContentScale.Crop)

            Text(text = "${model.temp}", color = colorResource(R.color.off_white),
                fontSize = 16.sp, modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center, fontFamily = FontFamily(Font(R.font.alfa_slab))
            )

        }
    }
//check
val items = listOf(
    HourlyModel("9 am", 11, "cloudy"),  // R.drawable.cloudy_sunny
    HourlyModel("10 am", 12, "sunny"),  // R.drawable.happysun
    HourlyModel("11 am", 14, "windy"),  // R.drawable.wind
    HourlyModel("12 am", 15, "rainy"),  // R.drawable.rainy
    HourlyModel("1 am", 20, "stormy")   // R.drawable.storm
)
    
            @Composable
    fun WeatherDetailItem(icon: Int, value: String, label: String){

        Column (modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(painter = painterResource(id= icon), contentDescription = null,
                modifier = Modifier.size(34.dp)
            )

            Text(text= value, fontWeight = FontWeight.Bold,
                color = colorResource(id= R.color.off_white),
                textAlign = TextAlign.Center
            )

            Text(text= label, fontWeight = FontWeight.Bold,
                color = colorResource(id= R.color.off_white),
                textAlign = TextAlign.Center
            )
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