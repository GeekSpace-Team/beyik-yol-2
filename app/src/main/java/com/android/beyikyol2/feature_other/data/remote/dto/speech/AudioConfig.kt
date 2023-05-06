package com.android.beyikyol2.feature_other.data.remote.dto.speech

data class AudioConfig(
    val audioEncoding: String,
    val effectsProfileId: List<String>,
    val pitch: Int,
    val speakingRate: Int
)