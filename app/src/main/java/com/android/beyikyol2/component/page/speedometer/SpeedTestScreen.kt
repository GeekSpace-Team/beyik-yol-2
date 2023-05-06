package com.android.beyikyol2.component.page.speedometer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.ui.theme.*
import kotlinx.coroutines.launch
import java.lang.Float.max
import kotlin.math.floor
import kotlin.math.roundToInt

suspend fun startAnimation(animation: Animatable<Float, AnimationVector1D>, targetValue: Float) {
    animation.animateTo(targetValue)
}

fun Animatable<Float, AnimationVector1D>.toUiState(maxSpeed: Float) = UiState(
    arcValue = value,
    speed = "%.1f".format(value * 100),
    ping = if (value > 0.2f) "${(value * 15).roundToInt()} ms" else "-",
    maxSpeed = if (maxSpeed > 0f) "%.1f mbps".format(maxSpeed) else "-",
    inProgress = isRunning
)



@Composable
fun SpeedTestScreen(
    startLocation: () -> Unit,
    stopLocation: () -> Unit,
    locationDetails: LocationDetails,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()

    val ctx = LocalContext.current

    fun getLocationManager(context: Context): LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun isLocEnable(context: Context): Boolean {
        val locationManager = getLocationManager(context)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    val animation = remember { Animatable(0f) }
    val maxSpeed = remember { mutableStateOf(200f) }
    maxSpeed.value = max(maxSpeed.value, animation.value * 200f)

    val isRunning = rememberSaveable {
        mutableStateOf(false)
    }

    fun checkGps(): Boolean{
        if(!isLocEnable(ctx)) {
            Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS,
                Uri.fromParts(
                    "package",
                    ctx.packageName,
                    null
                )
            ).also { intent ->
                try {
                    ctx.startActivity(
                        intent
                    )
                } catch (e: ActivityNotFoundException) {
                    Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    ).also { intentCatch ->
                        ctx.startActivity(
                            intentCatch
                        )
                    }
                }

            }
        }
        return isLocEnable(ctx)
    }

    LaunchedEffect(true){
        checkGps()
    }

    BackHandler {
        stopLocation()
        locationDetails.speed=0
        isRunning.value=false
        navController.popBackStack()
    }

    SpeedTestScreen(
        UiState(
            speed = locationDetails.speed.toString(), inProgress = isRunning.value,
            arcValue = locationDetails.speed.toFloat() / 400f
        )
    ) {
        if(checkGps()){
            if (isRunning.value) {
                locationDetails.speed=0
                stopLocation()
            } else {
                startLocation()
            }
            isRunning.value = !isRunning.value
        }

    }
}

@Composable
private fun SpeedTestScreen(state: UiState, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Header()
        SpeedIndicator(state = state, onClick = onClick)
        Box {}
    }
}

@Composable
fun Header() {
    Text(
        text = stringResource(id = R.string.speedometer),
        modifier = Modifier.padding(top = 52.dp, bottom = 16.dp),
        style = MaterialTheme.typography.h6,
        color = Color.White
    )
}

@Composable
fun SpeedIndicator(state: UiState, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        CircularSpeedIndicator(state.arcValue, 240f)
        StartButton(!state.inProgress, onClick)
        SpeedValue(state.speed)
    }
}

@Composable
fun SpeedValue(value: String) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 100.sp,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.speed_font)),
            fontWeight = FontWeight.Bold
        )
        Text(
            stringResource(id = R.string.km_h),
            style = MaterialTheme.typography.caption,
            color = Color.White
        )
    }
}

@Composable
fun StartButton(isEnabled: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 24.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.onSurface),

        ) {
        Text(
            text = if (isEnabled) stringResource(id = R.string.start) else stringResource(id = R.string.stop),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )
    }
}


@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF414D66))
            .width(1.dp)
    )
}


@Composable
fun CircularSpeedIndicator(value: Float, angle: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        drawLines(value, angle)
        drawArcs(value, angle)
    }
}

fun DrawScope.drawArcs(progress: Float, maxValue: Float) {
    val startAngle = 270 - maxValue / 2
    val sweepAngle = maxValue * progress

    val topLeft = Offset(50f, 50f)
    val size = Size(size.width - 100f, size.height - 100f)

    fun drawBlur() {
        for (i in 0..20) {
            drawArc(
                color = Green200.copy(alpha = i / 900f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)
            )
        }
    }

    fun drawStroke() {
        drawArc(
            color = Green500,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 86f, cap = StrokeCap.Round)
        )
    }

    fun drawGradient() {
        drawArc(
            brush = GreenGradient,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 80f, cap = StrokeCap.Round)
        )
    }

    drawBlur()
    drawStroke()
    drawGradient()
}

fun DrawScope.drawLines(progress: Float, maxValue: Float, numberOfLines: Int = 160) {
    val oneRotation = maxValue / numberOfLines
    val startValue = if (progress == 0f) 0 else floor(progress * numberOfLines).toInt() + 1

    for (i in startValue..numberOfLines) {
        rotate(i * oneRotation + (180 - maxValue) / 2) {
            drawLine(
                LightColor,
                Offset(if (i % 5 == 0) 80f else 30f, size.height / 2),
                Offset(0f, size.height / 2),
                8f,
                StrokeCap.Round
            )
        }
    }
}

