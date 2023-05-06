package com.android.beyikyol2.feature_profile.data.remote.dto.profile

data class GetProfileDto(
    val FCMToken: List<FCMToken>?,
    val blocked: Boolean,
    val cars: List<Car>?,
    val createdAt: String,
    val dob: String,
    val fullname: String,
    val id: Int,
    var image: String,
    val inbox: List<Inbox>,
    val password: String,
    val phonenumber: String,
    val status: String,
    val updatedAt: String,
    val username: String,
    val costs: Double?=0.0
)