package com.android.beyikyol2.feature_other.data.remote.dto

data class Users(
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