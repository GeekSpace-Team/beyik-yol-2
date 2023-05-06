package com.android.beyikyol2.feature_other.data.remote.dto.map.autocomplete

data class GetAutoComplete(
    val predictions: List<Prediction>,
    val status: String
)