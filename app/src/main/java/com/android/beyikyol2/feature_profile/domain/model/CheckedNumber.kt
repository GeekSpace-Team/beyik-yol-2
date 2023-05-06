package com.android.beyikyol2.feature_profile.domain.model

data class CheckedNumber(
    val accepted: Boolean,
    val createdAt: String,
    val id: Int,
    val is_exists: Boolean,
    val phone: String,
    val sms_phone: String,
    val updatedAt: String,
    val used: Boolean,
    val uuid: String
)
