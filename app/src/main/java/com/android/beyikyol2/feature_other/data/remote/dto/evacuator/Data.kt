package com.android.beyikyol2.feature_other.data.remote.dto.evacuator

data class Data(
    val createdAt: String,
    val description: String,
    val id: Int,
    val phoneNumber: String,
    val status: String,
    val subRegion: SubRegion,
    val subRegionId: Int,
    val updatedAt: String
)