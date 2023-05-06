package com.android.beyikyol2.component.page.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.ImageLoader
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.common.RatingBar
import com.android.beyikyol2.feature_other.data.remote.dto.map.autocomplete.Prediction
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import kotlinx.coroutines.delay

data class MapResult(
    val lat: Double = 0.0,
    val lng: Double  = 0.0,
    val title: String = "",
    val desc: String = ""
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FindPlaceUi(
    onResultClick: (MapResult) -> Unit
) {

    var query by rememberSaveable {
        mutableStateOf("")
    }
    val viewModel: GetHomeViewModel = hiltViewModel()
    val state = viewModel.state.value

    LaunchedEffect(query) {
        viewModel.getAutoComplete(query)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    
    
    var result by remember {
        mutableStateOf(state.searchFromMap?.results)
    }

    LaunchedEffect(key1 = query) {
        // this check is optional if you want the value to emit from the start
        if (query.isBlank()) return@LaunchedEffect

        delay(2000)
        // print or emit to your viewmodel
        viewModel.searchFromMap(query)
    }
    val focusRequester = remember {
       FocusRequester()
    }
    Column(modifier = Modifier.padding(16.dp)) {

        AutoCompleteUI<Prediction>(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            queryLabel = stringResource(id = com.android.beyikyol2.R.string.search_place),
            useOutlined = true,
            onQueryChanged = { updatedAddress ->
                query = updatedAddress
                viewModel.getAutoComplete(updatedAddress)
            },
            predictions = state.autoComplete?.predictions,
            onClearClick = {
                query = ""
//                state.searchFromMap = null
                try{
                    focusRequester.requestFocus()
                } catch (ex:Exception){}
            },
            onDoneActionClick = {
                viewModel.searchFromMap(query)
                try{
                    focusRequester.requestFocus()
                } catch (ex:Exception){}
            },
            onItemClick = { placeItem ->
                query = placeItem.structured_formatting.main_text
                viewModel.searchFromMap(query)
                try{
                    focusRequester.requestFocus()
                } catch (ex:Exception){}
            },
            colors=TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                leadingIconColor = MaterialTheme.colorScheme.onBackground,
                trailingIconColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            showItems = state.searchFromMap==null || state.searchFromMap!!.results.isEmpty(),
            focusRequester = focusRequester
        ) {
            if(state.searchFromMap==null || state.searchFromMap!!.results.isEmpty()){
                if (state.autoComplete == null) {
                    CircularProgressIndicator()
                } else {
                    Text(it.structured_formatting.main_text, style = MaterialTheme.typography.titleSmall,color=MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        if(state.isLoading && query.isNotBlank()){
            Loading()
        } else {
            LazyColumn(){
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(state.searchFromMap?.results?.size?:0){
                    val item = try {
                        state.searchFromMap?.results?.get(it)
                    } catch (ex: Exception){null}
                    item?.let { it1 ->
                        Spacer(modifier = Modifier.height(8.dp))
                        ElevatedCard(modifier = Modifier
                            .fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                ElevatedCard(modifier = Modifier
                                    .height(150.dp)
                                    .fillMaxWidth()
                                ){
                                    ImageLoader(url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${it1.photos?.get(0)?.photo_reference}&key=AIzaSyDJy-_ydiaAH6z2A0exJETzhKDlUhX7vyE")
                                }
                                Text(text = it1.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = it1.rating.toString(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    RatingBar(
                                        rating = it1.rating
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "${it1.user_ratings_total} ${stringResource(id = R.string.reviews)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondary)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row() {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Text(text = it1.formatted_address, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                ElevatedButton(onClick = {
                                    onResultClick(
                                        MapResult(
                                            item.geometry.location.lat,
                                            item.geometry.location.lng,
                                            item.name,
                                            item.formatted_address
                                        )
                                    )
                                }) {
                                    Text(text = stringResource(id = R.string.show_on_map), style = MaterialTheme.typography.titleSmall,color=MaterialTheme.colorScheme.onBackground)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

    }
}