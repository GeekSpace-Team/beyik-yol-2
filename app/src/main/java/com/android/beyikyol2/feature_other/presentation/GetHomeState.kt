package com.android.beyikyol2.feature_other.presentation

import com.android.beyikyol2.feature_other.data.remote.dto.GetConstantDto
import com.android.beyikyol2.feature_other.data.remote.dto.GetHomeDto
import com.android.beyikyol2.feature_other.data.remote.dto.evacuator.GetEvacuatorDto
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDto
import com.android.beyikyol2.feature_other.data.remote.dto.map.SearchFromMap
import com.android.beyikyol2.feature_other.data.remote.dto.map.autocomplete.GetAutoComplete
import com.android.beyikyol2.feature_other.data.remote.dto.map.direction.GetDirection
import com.android.beyikyol2.feature_other.data.remote.dto.speech.response.SpeechResponse
import com.android.beyikyol2.feature_other.data.remote.dto.weather.GetWeather

data class GetHomeState(
    val homeInfo: GetHomeDto? = null,
    val userInboxState: GetUserInboxDto? = null,
    val evacuatorDto: GetEvacuatorDto? = null,
    val constantData: GetConstantDto? = null,
    val autoComplete: GetAutoComplete? = null,
    var searchFromMap: SearchFromMap? = null,
    var ttsResult: SpeechResponse? = null,
    var weather: GetWeather? = null,
    var directionState: GetDirection? = null,
    val isLoading: Boolean = false
)
