package com.android.beyikyol2.component.page.bottom

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.common.StateMessage
import com.android.beyikyol2.component.page.inbox.InboxItem
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_other.data.remote.dto.evacuator.Data
import com.android.beyikyol2.feature_other.data.remote.dto.evacuator.Region
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.Grey

@Composable
fun Service(navController: NavController) {
    val viewModel: GetHomeViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context = LocalContext.current
    val loading = rememberSaveable {
        mutableStateOf(true)
    }
    val region = rememberSaveable {
        mutableStateOf("0")
    }
    LaunchedEffect(true) {
        viewModel.getEvacuator(region.value)
    }
    val listState = rememberLazyListState()
    val listState2 = rememberLazyListState()

    if (state.evacuatorDto == null) {
        Loading()
    } else {
        loading.value = true
        Box(modifier = Modifier.fillMaxSize()) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material.Text(
                        text = stringResource(id = R.string.evacuators),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(state = listState, contentPadding = PaddingValues(8.dp)) {
                    items(state.evacuatorDto.regions.size) {
                        RegionItem(
                            state.evacuatorDto.regions[it],
                            onClick = { id ->
                                region.value = id.toString()
                                viewModel.getEvacuator(id.toString())
                            },
                            selected = state.evacuatorDto.regions[it].id.toString() == region.value
                        )
                    }
                }

                if(state.evacuatorDto.data.isEmpty()){
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        StateMessage(message = R.string.no_data, actionText = R.string.refresh, lottieFile = R.raw.no_data_found) {
                            viewModel.getEvacuator(region.value)
                        }
                    }
                } else {
                    LazyColumn(state = listState2) {
                        items(state.evacuatorDto.data.size) {
                            EvacuatorItem(state.evacuatorDto.data[it])
                        }
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun EvacuatorItem(data: Data) {
    val context = LocalContext.current
    Row(modifier = Modifier
        .clickable { }
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(10.dp))
        .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(
                text = data.phoneNumber,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = if (Utils.getLanguage(context) == Locales.RU) data.subRegion.name_ru else data.subRegion.name_tm,
                style = MaterialTheme.typography.bodySmall,
                color = Grey,
            )
        }
        IconButton(onClick = {
            val u = Uri.parse("tel:" + data.phoneNumber)
            val i = Intent(Intent.ACTION_DIAL, u)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(i)
            } catch (s: SecurityException) {
                s.printStackTrace()
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_phone_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegionItem(region: Region, onClick: (Int) -> Unit, selected: Boolean = false) {
    val context = LocalContext.current
    Chip(
        onClick = { onClick(region.id) },
        colors = ChipDefaults.chipColors(
            backgroundColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
        ),
        modifier = Modifier.padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp))
    ) {
        Text(
            text = if (Utils.getLanguage(context) == Locales.RU) region.name_ru else region.name_tm,
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onBackground
        )
    }
}
