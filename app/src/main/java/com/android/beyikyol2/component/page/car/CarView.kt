package com.android.beyikyol2.component.page.car

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.*
import com.android.beyikyol2.component.page.floorMod
import com.android.beyikyol2.component.widget.Indicators
import com.android.beyikyol2.core.util.ImageUrl
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_car.presentation.CarViewModel
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.Clear
import com.android.beyikyol2.ui.theme.Warning
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun CarView(navController: NavController, id: String) {
    val viewModel: CarViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context = LocalContext.current
    val loading = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        viewModel.getCarById(Utils.getToken(context), id)
    }
    if (state.singleCar == null) {
        Loading()
    } else {
        var open = remember {
            mutableStateOf(false)
        }

        val isRu = Utils.getLanguage(context) == Locales.RU

        if(loading.value){
            ProgressDialog {
                loading.value=false
            }
        }

        var showToast = remember {
            mutableStateOf(false)
        }

        var message = remember {
            mutableStateOf(R.string.error)
        }

        if(showToast.value){
            SweetToastUtil.SweetError(message = stringResource(id = message.value))
            showToast.value=false
        }

        if(open.value){
            Alert(props = AlertProperties(
                openDialog = open,
                title = R.string.attention,
                body = R.string.do_you_want_delete,
                onOkPressed = {
                    loading.value=true
                    viewModel.deleteCar(
                        Utils.getToken(context),
                        id,
                        onSuccess = {
                            loading.value=false
                            navController.popBackStack()
                        }, onError = {
                            loading.value=false
                            showToast.value=true
                            message.value=it
                        })
                },
                onCancelPressed = {
                    open.value=false
                }
            ))
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.car_settings),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
//                IconButton(onClick = {
//                    navController.popBackStack()
//                }) {
//                    Icon(
//                        Icons.Default.Share,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onSecondary
//                    )
//                }
                Spacer(modifier = Modifier.width(12.dp))
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



            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.padding(16.dp)) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp),
                    ) {
                        Banner(banner = state.singleCar.images)
                        Spacer(modifier = Modifier.height(22.dp))
                        Text(
                            text = state.singleCar.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Label(
                            title = stringResource(R.string.brand),
                            value = state.singleCar.carModel.brand.name
                        )
                        Label(
                            title = stringResource(R.string.model),
                            value = state.singleCar.carModel.name
                        )
                        Label(
                            title = stringResource(R.string.type),
                            value = if(isRu) state.singleCar.carOption.name_ru else state.singleCar.carOption.name_tm
                        )
                        Label(
                            title = stringResource(R.string.transmission),
                            value = if (isRu) state.singleCar.carOption.name_ru else state.singleCar.carOption.name_tm
                        )
                        Label(
                            title = stringResource(R.string.energy_type),
                            value = if (isRu) state.singleCar.carEngineType.name_ru else state.singleCar.carEngineType.name_tm
                        )
                        Label(
                            title = stringResource(R.string.power),
                            value = state.singleCar.enginePower.toString()
                        )
                        Label(
                            title = stringResource(R.string.year),
                            value = state.singleCar.year.toString()
                        )
                        Label(
                            title = stringResource(R.string.km_miles),
                            value = state.singleCar.lastMile.toString()
                        )
                        Label(title = stringResource(R.string.vin), value = state.singleCar.vinCode)
                        Label(
                            title = stringResource(R.string.car_phone),
                            value = state.singleCar.phoneNumber
                        )
                        Spacer(modifier = Modifier.height(22.dp))
                        Button(
                            onClick = {
                                      navController.navigate(Routes.EditCar.route+"/"+state.singleCar.id)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(id = R.string.edit_car),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(22.dp))
                        TextButton(
                            onClick = {
                                open.value=true
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row() {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Warning
                                )
                                Text(
                                    text = stringResource(id = R.string.delete_car),
                                    color = Warning,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Label(title: String, value: String) {
    Spacer(modifier = Modifier.height(22.dp))
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.weight(
                1f
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(
                1f
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(banner: List<Image>?) {
    Box(
        Modifier
            .fillMaxWidth()
    ) {
        // Display 10 items
        val pageCount = banner?.size ?: 0
        // We start the pager in the middle of the raw number of pages
        val startIndex = Int.MAX_VALUE / 2
        val pagerState = rememberPagerState(initialPage = startIndex)

        HorizontalPager(
            // Set the raw page count to a really large number
            pageCount = Int.MAX_VALUE,
            state = pagerState,
            // Add 32.dp horizontal padding to 'center' the pages
            contentPadding = PaddingValues(horizontal = 0.dp),
            // Add some horizontal spacing between items
            pageSpacing = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // We calculate the page from the given index
            PagerSampleItem(
                item = try {
                    banner?.get((pagerState.currentPage - startIndex).floorMod(pageCount))
                } catch (ex: Exception) {
                    null
                }
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Indicators(
                size = pageCount,
                index = (pagerState.currentPage - startIndex).floorMod(pageCount),
                activeColor = Clear,
                passiveColor = Color.White.copy(alpha = 0.4f)
            )
        }
    }


}

@Composable
internal fun PagerSampleItem(
    item: Image?
) {
    ElevatedCard(modifier = Modifier.height(240.dp)) {
        // Our page content, displaying a random image
        Box(modifier = Modifier.fillMaxSize()) {
            ImageLoader(
                url = ImageUrl().getFullUrl(
                    item?.url ?: "",
                    ImageUrl().CAR
                ),
                contentScale = ContentScale.Inside
            )
        }
    }
}