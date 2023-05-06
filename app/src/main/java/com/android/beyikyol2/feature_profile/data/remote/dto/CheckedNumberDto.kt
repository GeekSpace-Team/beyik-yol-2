package com.android.beyikyol2.feature_profile.data.remote.dto

import com.android.beyikyol2.feature_profile.domain.model.CheckedNumber

data class CheckedNumberDto(
    val accepted: Boolean,
    val createdAt: String,
    val id: Int,
    val is_exists: Boolean,
    val phone: String,
    val sms_phone: String,
    val updatedAt: String,
    val used: Boolean,
    val uuid: String
) {
    fun toCheckedNumber(): CheckedNumber {
        return CheckedNumber(
            accepted, createdAt, id, is_exists, phone, sms_phone, updatedAt, used, uuid
        )
    }
}