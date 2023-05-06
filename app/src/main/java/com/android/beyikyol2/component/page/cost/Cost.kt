package com.android.beyikyol2.component.page.cost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.*
import com.android.beyikyol2.component.page.car.isScrollingUp
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.presentation.CarViewModel
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDtoItem
import com.android.beyikyol2.feature_cost.presentation.CostViewModel
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.Clear
import com.android.beyikyol2.ui.theme.Warning
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.google.accompanist.pager.*
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun Cost(navController: NavController) {
    val carViewModel: CarViewModel = hiltViewModel()
    val carState = carViewModel.state.value
    val pagerState = rememberPagerState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var carsTabs by remember {
        mutableStateOf(listOf<TabRowItem>())
    }
    LaunchedEffect(true) {
        carViewModel.getProfile(Utils.getToken(context = context))
    }



    if (carState.cars == null) {
        Loading()
    } else {

        var currentType by remember {
            mutableStateOf("")
        }

        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState =
            BottomSheetState(BottomSheetValue.Collapsed)
        )

        carsTabs = carState.cars.mapIndexed { index, it ->
            TabRowItem(it.id, index) {
                TabScreen(
                    it,
                    navController,
                    pagerState.currentPage,
                    index,
                    type = currentType
                )
            }
        }

        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(3.dp)
                                    .background(
                                        Color(0xFFDDDDDD),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.filter),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                    currentType = ""
                                }
                                .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.all),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                if (currentType.isEmpty()) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.check_circle),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                    currentType = CostType.FUEL
                                }
                                .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.fuel),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                if (currentType == CostType.FUEL) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.check_circle),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                    currentType = CostType.CHANGE
                                }
                                .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.change),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                if (currentType == CostType.CHANGE) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.check_circle),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                    currentType = CostType.REPAIR
                                }
                                .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                androidx.compose.material3.Text(
                                    text = stringResource(id = R.string.repair),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                if (currentType == CostType.REPAIR) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.check_circle),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                    }
                }
            },
            sheetPeekHeight = 0.dp,
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.tertiary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.tertiary)
                            .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(id = R.string.cost),
                                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(onClick = {
                            coroutineScope.launch {
                                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.filter),
                                contentDescription = null,
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    if (carState.cars.size <= 0) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            StateMessage(
                                message = R.string.no_cars,
                                actionText = R.string.add_car,
                                lottieFile = R.raw.no_data_empty_folder
                            ) {
                                navController.navigate(Routes.AddCar.route)
                            }
                        }
                    } else {
                        ScrollableTabRow(
                            selectedTabIndex = pagerState.currentPage,
                            edgePadding = 16.dp,
                            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiary,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                )
                            },
                            divider = {
                                TabRowDefaults.Divider(thickness = 0.dp)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            carState.cars.forEachIndexed { index, item ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    selectedContentColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                    unselectedContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(
                                                index
                                            )
                                        }
                                    },
                                    text = {
                                        Text(
                                            text = item.name,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                )
                            }
                        }

                    }
                }
                HorizontalPager(
                    count = carState.cars.size,
                    state = pagerState,
                ) {
                    carsTabs[pagerState.currentPage].screen()
                }
            }
        }
    }
}


data class TabRowItem(
    val id: Int,
    val pos: Int,
    val screen: @Composable () -> Unit,
)

@Composable
fun TabScreen(
    car: GetUserCarsDtoItem,
    navController: NavController,
    currentTab: Int,
    pos: Int,
    type: String = "",
) {
    val context = LocalContext.current
    val viewModel: CostViewModel = hiltViewModel()
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    LaunchedEffect(currentTab, type) {
        if (pos == currentTab) {
            viewModel.getCosts(Utils.getToken(context), car.id.toString(), type)
        }
    }
    if (state.cost == null) {
        Loading()
    } else {
        var open by remember {
            mutableStateOf(false)
        }
        if (open) {
            Dialog(onDismissRequest = { open = false }) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(
                            color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.select_cost_type),
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    ElevatedButton(
                        onClick = {
                            val img = if(car.images.isNotEmpty()) car.images[0].url else  "car_image_1.png"
                            navController.navigate(
                                Routes.AddCost.route + "/" + CostType.CHANGE + "/" + "${car.name} | ${car.carModel.name}" + "/" + img + "/" + car.id
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painterResource(id = R.drawable.change),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(id = R.string.change),
                            style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    ElevatedButton(
                        onClick = {
                            val img = if(car.images.isNotEmpty()) car.images[0].url else  "car_image_1.png"
                            navController.navigate(
                                Routes.AddCost.route + "/" + CostType.REPAIR + "/" + "${car.name} | ${car.carModel.name}" + "/" + img + "/" + car.id
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painterResource(id = R.drawable.repair),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(id = R.string.repair),
                            style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    ElevatedButton(
                        onClick = {
                            val img = if(car.images.isNotEmpty()) car.images[0].url else  "car_image_1.png"
                            navController.navigate(
                                Routes.AddCost.route + "/" + CostType.FUEL + "/" + "${car.name} | ${car.carModel.name}" + "/" + img + "/" + car.id
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painterResource(id = R.drawable.fuel),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(id = R.string.fuel),
                            style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
        if (try {
                state.cost?.size!! <= 0
            } catch (ex: Exception) {
                true
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StateMessage(
                    message = R.string.no_costs,
                    actionText = R.string.add_cost,
                    lottieFile = R.raw.no_report
                ) {
                    open = true
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(22.dp))

                LazyColumn(state = listState) {
                    item {
                        MyBarChartParent(state.cost)
                    }
                    items(state.cost!!.size) {
                        ChangeItem(navController, state.cost!![it], car, viewModel)
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .padding(16.dp), horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(id = R.string.add_cost),
                                style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                color = Color.White
                            )
                        },
                        icon = {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        },
                        onClick = {
                            open = true
                        },
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        contentColor = Clear,
                        expanded = listState.isScrollingUp()
                    )
                }
            }
        }

    }
}

class CostType {
    companion object CostType {
        const val FUEL = "FUEL"
        const val CHANGE = "CHANGE"
        const val REPAIR = "REPAIR"
    }
}

@Composable
fun ChangeItem(
    navController: NavController,
    item: GetCostsDtoItem,
    car: GetUserCarsDtoItem,
    viewModel: CostViewModel
) {
    val context = LocalContext.current
    val state = viewModel.state.value
    val isRu = Utils.getLanguage(LocalContext.current) == Locales.RU
    var open = remember {
        mutableStateOf(false)
    }


    var showToast = remember {
        mutableStateOf(false)
    }

    var loading = remember {
        mutableStateOf(false)
    }

    var message = remember {
        mutableStateOf(R.string.error)
    }

    var toastType = remember {
        mutableStateOf("error")
    }


    if (showToast.value) {
        if (toastType.value == "error") {
            SweetToastUtil.SweetError(message = stringResource(id = message.value))
        } else {
            SweetToastUtil.SweetSuccess(message = stringResource(id = message.value))
        }
        showToast.value = false
    }

    if (loading.value) {
        ProgressDialog {
            loading.value = false
        }
    }



    if (open.value) {
        Alert(props = AlertProperties(
            openDialog = open,
            title = R.string.attention,
            body = R.string.do_you_want_delete,
            onOkPressed = {
                loading.value = true
                open.value = false
                viewModel.deleteCost(Utils.getToken(context), item.id.toString(), onSuccess = {
                    state.cost =
                        state.cost?.filter { dItem -> it.toInt() != dItem.id } as GetCostsDto?
                    loading.value = false
                    message.value = R.string.success
                    toastType.value = "success"
                    showToast.value = true
                }, onError = {
                    loading.value = false
                    message.value = it
                    toastType.value = "error"
                    showToast.value = true
                })
            },
            onCancelPressed = {
                open.value = false
            }
        ))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ElevatedCard(
            shape = MaterialTheme.shapes.extraLarge.copy(CornerSize(8.dp)),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.clickable {
                navController.navigate(
                    Routes.EditCost.route + "/" + item.costType + "/" + "${car.name} | ${car.carModel.name}" + "/" + try {
                        car.images[0].url
                    } catch (ex: Exception) {
                        ""
                    } + "/" + item.id.toString()
                )
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(
                            id = when (item.costType) {
                                CostType.CHANGE -> R.drawable.change
                                CostType.FUEL -> R.drawable.fuel
                                else -> R.drawable.repair
                            }
                        ),
                        contentDescription = null,
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(
                            id = when (item.costType) {
                                CostType.CHANGE -> R.string.change
                                CostType.FUEL -> R.string.fuel
                                else -> R.string.repair
                            }
                        ),
                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                    )
                }
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    var expanded by remember { mutableStateOf(false) }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.tertiary)
                    ) {
                        DropdownMenuItem(onClick = {
                            navController.navigate(
                                Routes.EditCost.route + "/" + item.costType + "/" + "${car.name} | ${car.carModel.name}" + "/" + try {
                                    car.images[0].url
                                } catch (ex: Exception) {
                                    ""
                                } + "/" + item.id.toString()
                            )
                        }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                stringResource(id = R.string.edit),
                                style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                            )
                        }
                        DropdownMenuItem(onClick = {
                            open.value = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Warning)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                stringResource(id = R.string.delete),
                                style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                color = Warning
                            )
                        }
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.4.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = when (item.costType) {
                    CostType.CHANGE -> item.CostToType.map { if (isRu) it.changeType.name_ru else it.changeType.name_tm }
                        .toString()
                    CostType.FUEL -> "${item.volume} L / ${item.price} TMT"
                    else -> "${item.description} / ${item.price} TMT"
                },
                color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(12.dp)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.4.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.road),
                        contentDescription = null,
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${item.mile} km",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_date_range_24),
                        contentDescription = null,
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item.createdAt.split("T")[0],
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }

}

@Composable
fun MyBarChartParent(cost: GetCostsDto?) {
    BarChart(
        barChartData = BarChartData(
            bars = listOf(
                BarChartData.Bar(
                    label = stringResource(id = R.string.fuel),
                    value = try{cost?.filter { it.costType==CostType.FUEL }?.map { it.price }?.reduce { a, b -> a + b }?.toFloat()?:0f}catch (ex: Exception){0f},
                    color = Color.Red
                ),
                BarChartData.Bar(
                    label = stringResource(id = R.string.change),
                    value = try{cost?.filter { it.costType==CostType.CHANGE }?.map { it.price }?.reduce { a, b -> a + b }?.toFloat()?:0f}catch (ex: Exception){0f},
                    color = Color.Yellow
                ),
                BarChartData.Bar(
                    label = stringResource(id = R.string.repair),
                    value = try{cost?.filter { it.costType==CostType.REPAIR }?.map { it.price }?.reduce { a, b -> a + b }?.toFloat()?:0f}catch (ex: Exception){0f},
                    color = Color.Blue
                )
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 22.dp, horizontal = 16.dp)
            .height(200.dp),
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
        labelDrawer = SimpleValueDrawer()
    )
}