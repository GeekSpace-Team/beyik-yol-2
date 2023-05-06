package com.android.beyikyol2.feature_other.data.remote.dto.speech.response

data class SpeechResponse(
    val audioConfig: AudioConfig,
    val audioContent: String,
    val timepoints: List<Any>
)