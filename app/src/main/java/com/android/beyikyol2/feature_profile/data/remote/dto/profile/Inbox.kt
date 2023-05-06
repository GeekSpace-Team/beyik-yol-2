package com.android.beyikyol2.feature_profile.data.remote.dto.profile

data class Inbox(
    val createdAt: String,
    val id: Int,
    val isRead: Boolean,
    val messageRu: String,
    val messageTm: String,
    val titleRu: String,
    val titleTm: String,
    val updatedAt: String,
    val url: String,
    val userId: Int
)