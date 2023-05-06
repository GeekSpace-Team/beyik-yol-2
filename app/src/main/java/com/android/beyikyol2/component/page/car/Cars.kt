package com.android.beyikyol2.component.page.car

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.ImageLoader
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.common.StateMessage
import com.android.beyikyol2.core.util.ImageUrl
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.presentation.CarViewModel
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.Clear
import com.android.beyikyol2.ui.theme.Tonal

@Composable
fun CarsPage(navController: NavHostController) {
    val viewModel: CarViewModel = hiltViewModel()
    val homeViewModel: GetHomeViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.getProfile(Utils.getToken(context))
    }
    if (state.cars == null) {
        Loading()
    } else {
        val listState = rememberLazyListState()
        Box(modifier = Modifier.fillMaxSize()) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = com.android.beyikyol2.R.string.my_cars_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if(state.cars.size<=0){
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        StateMessage(message = R.string.no_cars, actionText = R.string.add_car, lottieFile = R.raw.no_data_empty_folder) {
                            navController.navigate(Routes.AddCar.route)
                        }
                    }
                } else {
                    LazyColumn(state = listState) {
                        items(state.cars.size) {
                            CarItem(state.cars[it]) {
                                navController.navigate(Routes.CarView.route+"/${state.cars[it].id}")
                            }
                        }
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .padding(16.dp), horizontalArrangement = Arrangement.End
            ) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(id = com.android.beyikyol2.R.string.new_car),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    },
                    icon = {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    },
                    onClick = {
                        navController.navigate(Routes.AddCar.route)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Clear,
                    expanded = listState.isScrollingUp()
                )
            }
        }
    }
}

@Composable
fun CarItem(item: GetUserCarsDtoItem, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
        ElevatedCard(
            shape = MaterialTheme.shapes.extraLarge.copy(CornerSize(8.dp)),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.clickable { onClick() }
        ) {
            Row() {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${item.carModel.name} ${item.year} ${stringResource(id = R.string.year)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "${item.lastMile} KM",
                        style = MaterialTheme.typography.bodySmall,
                        color = Tonal
                    )

                }
                Box(modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary)){
                    ImageLoader(
                        url = ImageUrl().getFullUrl(
                            try {
                                item.images?.get(0)?.url ?: ""
                            } catch (ex: Exception) {
                                ""
                            }, ImageUrl().CAR
                        ), modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Inside
                    )
                }
            }
        }
    }
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}