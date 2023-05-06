package com.android.beyikyol2.lyricist

import androidx.compose.ui.text.AnnotatedString

data class Strings(
    val simple: String,
    val annotated: AnnotatedString,
    val parameter: (locale: String) -> String,
    val plural: (count: Int) -> String,
    val list: List<String>
)

val strings = mapOf(
    Locales.EN to TmStrings,
    Locales.RU to RuStrings,
    Locales.TM to TmStrings
)

val DefaultLocale = RuStrings
