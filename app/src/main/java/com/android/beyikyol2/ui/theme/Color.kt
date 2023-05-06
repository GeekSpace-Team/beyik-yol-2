package com.android.beyikyol2.ui.theme

import androidx.annotation.ColorInt
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.math.absoluteValue

val Primary = Color(0xFF2F1FC8)
val BgDark = Color(0xFF161616)
val Bg = Color(0xFFF5F5F5)
val Clear = Color(0xFFFFFFFF)
val Grey = Color(0xFF979797)
val Tonal = Color(0xFF9088DC)
val Tonal10 = Color(0xFFEBEBF3)
val DDD = Color(0xFFDDDDDD)
val Warning = Color(0xFFC81F1F)
val AlertBg = Color(0xFFF4E7E6)

val GreyLight = Color(0xFFB5B6BA)
val GreyDark = Color(0xFF60626C)
val DADADA = Color(0xFFDADADA)

val Skeleton = Color(0xFFE3E3E3)


val Primary_DARK = Color(0xFF7668FB)
val BgDark_DARK = Color(0xFF161616)
val Bg_DARK = Color(0xFF1F1F1F)
val Clear_DARK = Color(0xFF303030)
val Grey_DARK = Color(0xFF989898)
val Tonal_DARK = Color(0xFFEFEEFB)
val Tonal10_DARK = Color(0xFF2E2E36)
val DDD_DARK = Color(0xFF616161)
val Warning_DARK = Color(0xFFF35959)
val AlertBg_DARK = Color(0xFFF4E7E6)



val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Pink = Color(0xFFE2437E)

val Green200 = Color(0xFFAEFF82)
val Green300 = Color(0xFFC9FCAD)
val Green500 = Color(0xFF07A312)

val DarkColor = Color(0xFF101522)
val DarkColor2 = Color(0xFF202532)
val LightColor = Color(0xFF414D66)
val LightColor2 = Color(0xFF626F88)

val GreenGradient = Brush.linearGradient(
    colors = listOf(Green300, Green200),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

val DarkGradient = Brush.verticalGradient(
    colors = listOf(DarkColor2, DarkColor)
)

@ColorInt
fun String.toHslColor(saturation: Float = 0.5f, lightness: Float = 0.4f): Int {
    val hue = fold(0) { acc, char -> char.code + acc * 37 } % 360
    return ColorUtils.HSLToColor(floatArrayOf(hue.absoluteValue.toFloat(), saturation, lightness))
}


