package com.android.beyikyol2.feature_other.data.remote.dto.map.direction

data class GetDirection(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)