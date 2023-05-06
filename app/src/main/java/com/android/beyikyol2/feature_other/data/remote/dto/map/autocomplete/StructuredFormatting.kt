package com.android.beyikyol2.feature_other.data.remote.dto.map.autocomplete

data class StructuredFormatting(
    val main_text: String,
    val main_text_matched_substrings: List<MainTextMatchedSubstring>,
    val secondary_text: String
)