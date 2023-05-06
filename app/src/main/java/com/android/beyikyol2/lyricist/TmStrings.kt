package com.android.beyikyol2.lyricist

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.TM)
internal val TmStrings = Strings(
    simple = "Hello Compose tm!",

    annotated = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Red)) { append("Hello ") }
        withStyle(SpanStyle(fontWeight = FontWeight.Light)) { append("Compose!") }
    },

    parameter = { locale ->
        "Current locale: $locale"
    },

    plural = { count ->
        val value = when (count) {
            1, 2 -> "few"
            in 3..10 -> "bunch of"
            else -> "lot of"
        }
        "I have a $value apples"
    },

    list = listOf("Avocado", "Pineapple", "Plum")
)