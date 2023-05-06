package com.android.beyikyol2.component.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

data class WeatherCard(
    val time: String,
    val message: String,
    val icon: String,
)

@Composable
fun Assistant(
    card: WeatherCard,
    onOpen: () -> Unit
) {

//    val colorStops = listOf(
//        Color(0xFF1C1845),
//        Color(0xFF221F40),
//        Color(0xFF1D1A32)
//    )
//    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.android.beyikyol2.R.raw.ai))
//    val composition2 by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.android.beyikyol2.R.raw.assistant))
//    val composition3 by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.android.beyikyol2.R.raw.globe))

    LaunchedEffect(true) {
        delay(500L)
        onOpen()
    }
    WeatherUpdateCard(weatherCard = card)
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = MaterialTheme.colorScheme.surface),
//        color = MaterialTheme.colorScheme.surface
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Brush.verticalGradient(colorStops))
//        ) {
//            IconButton(
//                onClick = { onClose() }, modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(16.dp)
//            ) {
//                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
//            }
//            Column(
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//                Spacer(modifier = Modifier.height(22.dp))
//                LottieAnimation(
//                    composition = composition2,
//                    iterations = LottieConstants.IterateForever,
//                    modifier = Modifier.size(200.dp),
//                    contentScale = ContentScale.Inside
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Text(
//                    text = text,
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.White,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    LottieAnimation(
//                        composition = composition,
//                        iterations = LottieConstants.IterateForever,
//                        modifier = Modifier
//                            .size(150.dp)
//                            .align(Alignment.Center),
//                        contentScale = ContentScale.Inside
//                    )
//                }
//
//
//            }
//        }
//    }
}

@Composable
fun WeatherUpdateCard(weatherCard: WeatherCard) {
    Box {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = weatherCard.time, // "20 minutes ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weatherCard.message, // "If you don't want to get wet today, don't forget your umbrella.",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Image(
            painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${weatherCard.icon}@2x.png"),
            contentDescription = "weather overlap image",
            modifier = Modifier
                .size(100.dp)
                .align(alignment = Alignment.TopEnd)
                .offset(x = (-40).dp)
        )
    }
}
