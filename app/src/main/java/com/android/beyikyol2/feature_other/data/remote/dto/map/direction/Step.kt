package com.android.beyikyol2.feature_other.data.remote.dto.map.direction

data class Step(
    val distance: Distance,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
    val travel_mode: String
)