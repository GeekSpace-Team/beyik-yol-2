package com.android.beyikyol2.feature_profile.data.remote.dto

data class CheckCodeDto(
    val access_token: String,
    val blocked: Boolean,
    val createdAt: String,
    val dob: String,
    val fullname: String,
    val id: Int,
    val image: String,
    val password: String,
    val phonenumber: String,
    val status: String,
    val updatedAt: String,
    val username: String
)