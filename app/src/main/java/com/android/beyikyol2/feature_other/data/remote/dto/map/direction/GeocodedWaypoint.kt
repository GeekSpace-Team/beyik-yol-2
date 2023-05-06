package com.android.beyikyol2.feature_other.data.remote.dto.map.direction

data class GeocodedWaypoint(
    val geocoder_status: String,
    val partial_match: Boolean,
    val place_id: String,
    val types: List<String>
)