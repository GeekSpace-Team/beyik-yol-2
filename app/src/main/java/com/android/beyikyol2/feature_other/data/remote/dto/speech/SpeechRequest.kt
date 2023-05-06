package com.android.beyikyol2.feature_other.data.remote.dto.speech

data class SpeechRequest(
    val audioConfig: AudioConfig,
    val input: Input,
    val voice: Voice
)