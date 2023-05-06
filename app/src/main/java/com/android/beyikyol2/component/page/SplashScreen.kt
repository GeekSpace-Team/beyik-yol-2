package com.android.beyikyol2.component.page

import android.content.Context
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.BgDark
import com.android.beyikyol2.ui.theme.Grey
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = BgDark
        )
    }
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    val context: Context = LocalContext.current
    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(3000L)
        if(Utils.getSharedPreference(context,"isFirst").toString() == "0"){
            navController.navigate(Routes.MainScreen.route){
                popUpTo(Routes.Splash.route){
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Routes.OnBoarding.route){
                popUpTo(Routes.Splash.route){
                    inclusive = true
                }
            }
        }

    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BgDark)){
        Image(
            painter = painterResource(id = R.drawable.ic_white_logo),
            contentDescription = null,
            modifier = Modifier
                .scale(scale.value)
                .align(Alignment.Center)
                .size(200.dp,100.dp)
        )
        Text(
            text = "© Copyright new design. All Rights Reserved",
            color= Grey,
            fontSize=14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 24.dp)
        )
    }
}