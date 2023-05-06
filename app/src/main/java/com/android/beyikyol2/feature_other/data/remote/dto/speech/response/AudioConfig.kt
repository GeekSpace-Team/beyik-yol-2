package com.android.beyikyol2.feature_other.data.remote.dto.speech.response

data class AudioConfig(
    val audioEncoding: String,
    val effectsProfileId: List<Any>,
    val pitch: Int,
    val sampleRateHertz: Int,
    val speakingRate: Int,
    val volumeGainDb: Int
)