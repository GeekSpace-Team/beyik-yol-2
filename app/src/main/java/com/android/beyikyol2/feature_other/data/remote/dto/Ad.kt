package com.android.beyikyol2.feature_other.data.remote.dto

data class Ad(
    val adsImage: List<AdsImage>,
    val adsType: String,
    val createdAt: String,
    val id: Int,
    val index: Int,
    val status: String,
    val titleRu: String,
    val titleTm: String,
    val updatedAt: String,
    val url: String
)