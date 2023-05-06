package com.android.beyikyol2.feature_other.data.remote.dto.map

data class SearchFromMap(
    val html_attributions: List<Any>,
    val next_page_token: String,
    var results: List<Result>,
    val status: String
)