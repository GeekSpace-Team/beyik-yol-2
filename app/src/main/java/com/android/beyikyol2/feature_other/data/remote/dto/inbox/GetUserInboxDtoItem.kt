package com.android.beyikyol2.feature_other.data.remote.dto.inbox

data class GetUserInboxDtoItem(
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