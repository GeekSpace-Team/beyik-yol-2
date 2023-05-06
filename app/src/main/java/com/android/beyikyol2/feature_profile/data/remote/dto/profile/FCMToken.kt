package com.android.beyikyol2.feature_profile.data.remote.dto.profile

data class FCMToken(
    val createdAt: String,
    val device: String,
    val id: Int,
    val token: String,
    val updatedAt: String,
    val userId: Int
)