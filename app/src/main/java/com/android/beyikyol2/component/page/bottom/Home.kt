package com.android.beyikyol2.component.page

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.*
import com.android.beyikyol2.component.page.car.isScrollingUp
import com.android.beyikyol2.component.page.cost.CostType
import com.android.beyikyol2.component.page.other.ShowcaseSample
import com.android.beyikyol2.component.widget.Indicators
import com.android.beyikyol2.core.util.ImageUrl
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_other.data.remote.dto.Ad
import com.android.beyikyol2.feature_other.data.remote.dto.Car
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.*
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.IntroShowCaseState
import com.canopas.lib.showcase.introShowCaseTarget
import com.canopas.lib.showcase.rememberIntroShowCaseState
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.io.IOException
import java.sql.RowId

class FuelTypes {
    companion object FuelTypes {
        const val FUEL_80 = "FUEL_80"
        const val FUEL_92 = "FUEL_92"
        const val FUEL_95 = "FUEL_95"
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    navController1: NavHostController,
) {
    val viewModel: GetHomeViewModel = hiltViewModel()
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val isLoad = rememberSaveable {
        viewModel.isLoad
    }
    var mediaPlayer: MediaPlayer

    val scope = rememberCoroutineScope()
    val context: Context = LocalContext.current

    OnLifecycleEvent { owner, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.onRefresh(Utils.getToken(context), !isLoad.value)
            }
            else -> { /* other stuff */
            }
        }
    }



    LaunchedEffect(key1 = true) {
        scope.launch {
            if (!isLoad.value) {
                viewModel.onRefresh(Utils.getToken(context), !isLoad.value)
//            viewModel.getWeather()
            }

        }
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is GetHomeViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }


    }

//    LaunchedEffect(state.ttsResult){
//        if(state.ttsResult!=null){
//            viewModel.onRefresh(Utils.getToken(context))
//        }
//    }

    var assistantOpen by remember {
        mutableStateOf(false)
    }

    var assistantOpened by rememberSaveable {
        mutableStateOf(false)
    }

//    viewModel.textToSpeech("Привет! Добро пожаловать в наше приложение! Сегодня температура 27°С.")


    if (state.weather != null) {
        LaunchedEffect(state.weather) {
            viewModel.textToSpeech(
                "Привет! Добро пожаловать в наше приложение! Сегодня температура ${
                    state.weather?.main?.temp?.toInt().toString()
                }°."
            )
        }
    }


    var isPopupShown by rememberSaveable {
        mutableStateOf(false) // Initially dialog is closed
    }

    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        if ((state.isLoading || state.homeInfo == null)) {
            Loading()
        } else {

            var tourIndex by remember {
                mutableStateOf(0)
            }
            if (state.homeInfo.tts != null && !assistantOpened) {
                assistantOpen = true
            }



            Utils.setPreference("image", state.homeInfo?.user?.image.toString(), context)
            try {
                Utils.setPreference(
                    FuelTypes.FUEL_80,
                    state.homeInfo?.fuel_price?.filter { it.type == FuelTypes.FUEL_80 }
                        ?.get(0)?.value.toString(),
                    context)
            } catch (_: Exception) {
            }
            try {
                Utils.setPreference(
                    FuelTypes.FUEL_92,
                    state.homeInfo?.fuel_price?.filter { it.type == FuelTypes.FUEL_92 }
                        ?.get(0)?.value.toString(),
                    context)
            } catch (_: Exception) {
            }
            try {
                Utils.setPreference(
                    FuelTypes.FUEL_95,
                    state.homeInfo?.fuel_price?.filter { it.type == FuelTypes.FUEL_95 }
                        ?.get(0)?.value.toString(),
                    context)
            } catch (_: Exception) {
            }
            var openDialog by remember {
                mutableStateOf(false) // Initially dialog is closed
            }


            LaunchedEffect(true) {
                if (!isPopupShown) {
                    isPopupShown = true
                    if (state.homeInfo.popup != null) {
                        openDialog = true
                    }
                }
            }
            if (openDialog && !assistantOpen) {

                DialogBox2FA(popup = state.homeInfo?.popup) {
                    openDialog = false
                    isPopupShown = true
                }
            }
            Column(
                modifier = Modifier
                    .verticalScroll(scroll)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                val showCaseState = rememberIntroShowCaseState(initialIndex = 0);
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IntroShowCaseScaffold(
                        showIntroShowCase = true,
                        onShowCaseCompleted = {
                            //App Intro finished!!
                            tourIndex = 1
                        },
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
//                            if (Utils.getSharedPreference(context, "token").toString().isBlank()) {
//                                navController1.navigate(Routes.SignIn.route + "?type=home")
//                            } else {
                                    navController1.navigate(Routes.Profile.route)
//                            }
                                }
                                .introShowCaseTarget(
                                    content = {
                                        TourContent(
                                            title = stringResource(id = R.string.profile_tour_title),
                                            body = stringResource(id = R.string.profile_tour_title)
                                        )
                                    },
                                    index = 0,
                                    state = showCaseState
                                )
                        ) {
                            UserHead(
                                id = state.homeInfo?.user?.id.toString(),
                                firstName = if (state.homeInfo?.user?.fullname.toString()
                                        .isBlank()
                                ) {
                                    state.homeInfo?.user?.username ?: "?"
                                } else {
                                    state.homeInfo?.user?.fullname ?: "?"
                                },
                                lastName = ""
                            )
                            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                                Text(
                                    text = if (state.homeInfo?.user?.fullname.toString()
                                            .isBlank()
                                    ) {
                                        state.homeInfo?.user?.username ?: "?"
                                    } else {
                                        state.homeInfo?.user?.fullname
                                            ?: stringResource(id = R.string.to_sign_in)
                                    },
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = stringResource(id = R.string.profile_settings),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Tonal
                                )
                            }
                        }
                    }






                    fun getInbox(): String {
                        return if (state.homeInfo?.inboxCount == null) {
                            "0"
                        } else {
                            state.homeInfo.inboxCount.toString()
                        }
                    }

                    IconButton(onClick = {
                        if (Utils.getSharedPreference(context, "token").toString().isBlank()) {
                            navController1.navigate(Routes.SignIn.route)
                        } else {
                            navController1.navigate(Routes.Inbox.route)


                        }
                    }) {
                        Box(modifier = Modifier.size(35.dp)) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(25.dp)
                                    .align(Alignment.BottomStart)
                            )
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(18.dp)
                                    .background(
                                        Warning,
                                        shape = CircleShape
                                    )
                                    .align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = getInbox(),
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(40.dp))



                Banner(state.homeInfo?.banner)

                Spacer(modifier = Modifier.height(40.dp))

                Assistant(
                    card = WeatherCard(
                        try{state.homeInfo.weatherInfo?.weather?.get(0)?.description?:""}catch (e: java.lang.Exception){""},
                        "Привет! Добро пожаловать в наше приложение! Сегодня температура ${
                            state.homeInfo.weatherInfo?.main?.temp?.toInt().toString()
                        }°С.",
                        try{state.homeInfo.weatherInfo?.weather?.get(0)?.icon?:""}catch (e: java.lang.Exception){""}
                    ),
                    onOpen = {
                        if(!assistantOpened){
                            assistantOpened = true
                            try {
                                val url = "data:audio/mp3;base64,${state.homeInfo.tts?.audioContent}"
                                Log.e("TTS-p", url)
                                mediaPlayer = MediaPlayer().apply {
                                    setDataSource(url)
                                    prepare()
                                    start()
                                }
                            } catch (IoEx: IOException) {
                                IoEx.printStackTrace()
                                // TODO: error handling
                            }
                            assistantOpen=false
                        }
                    })

                Spacer(modifier = Modifier.height(40.dp))

                Speedometer(navController1)

                Spacer(modifier = Modifier.height(40.dp))

                if (!Utils.isLogin(context)) {
                    StateMessage(
                        message = R.string.please_login,
                        actionText = R.string.to_sign_in,
                        lottieFile = if (Utils.isDark(context)) R.raw.login_dark else R.raw.login_light
                    ) {
                        navController1.navigate(Routes.SignIn.route + "?type=home")
                    }
                } else {
                    if (try {
                            state.homeInfo?.cars?.isEmpty()!!
                        } catch (ex: Exception) {
                            true
                        }
                    ) {
                        StateMessage(
                            message = R.string.no_cars,
                            actionText = R.string.add_car,
                            lottieFile = R.raw.no_data_empty_folder
                        ) {
                            navController1.navigate(Routes.AddCar.route)
                        }
                    } else {
                        MyCars(state.homeInfo?.cars, navController1)

                        Spacer(modifier = Modifier.height(40.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ElevatedButton(
                                onClick = { navController1.navigate(Routes.Costs.route) },
                                colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.see_all_cost),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun MyCars(cars: List<Car>?, navController1: NavHostController) {
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.my_cars),
                style = MaterialTheme.typography.titleSmall
            )
            IconButton(onClick = {
                navController1.navigate(Routes.Cars.route) {

                }
            }) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(cars?.size ?: 0) { it ->
                HomeCarItem(cars?.get(it), navController1)
            }
            item {
                OutlinedCard(
                    border = BorderStroke(1.dp, DDD),
                    modifier = Modifier
                        .width(300.dp)
                        .height(350.dp)
                        .clickable {
                            navController1.navigate(Routes.AddCar.route)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ExtendedFloatingActionButton(
                            text = {
                                androidx.compose.material.Text(
                                    text = stringResource(id = R.string.new_car),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White
                                )
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            },
                            onClick = {
                                navController1.navigate(Routes.AddCar.route)
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Clear,
                            expanded = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeCarItem(car: Car?, navController1: NavHostController) {


    OutlinedCard(
        border = BorderStroke(1.dp, DDD),
        modifier = Modifier
            .width(300.dp)
            .clickable {
                navController1.navigate(Routes.CarView.route + "/" + car?.id)
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = car?.name ?: "",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                ImageLoader(
                    url = ImageUrl().getFullUrl(
                        try {
                            car?.images?.get(0)?.url ?: ""
                        } catch (ex: Exception) {
                            ""
                        }, ImageUrl().CAR
                    ),
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            val noData = stringResource(id = R.string.no_data)
            LabelHomeCar(title = R.string.change, value = try {
                car?.costChange?.filter { it.costType == CostType.CHANGE }
                    ?.map { it.mile.toString() }
                    ?.get(0).plus(" KM")
            } catch (ex: Exception) {
                noData
            }
            )
            LabelHomeCar(
                title = R.string.repair,
                value = try {
                    car?.costChange?.filter { it.costType == CostType.REPAIR }
                        ?.map { it.mile.toString() }
                        ?.get(0).plus(" KM")
                } catch (ex: Exception) {
                    noData
                })
            LabelHomeCar(
                title = R.string.fuel,
                value = try {
                    car?.costChange?.filter { it.costType == CostType.FUEL }
                        ?.map { it.mile.toString() }
                        ?.get(0).plus(" KM")
                } catch (ex: Exception) {
                    noData
                })
            Spacer(modifier = Modifier.height(12.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp), color = DDD
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.cost),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = try {
                        car?.costChange
                            ?.map { it.price }
                            ?.reduce { a, b -> a + b }?.toString().plus(" TMT")
                    } catch (ex: Exception) {
                        "0 TMT"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun LabelHomeCar(title: Int, value: String) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        ) {

            drawLine(
                color = Grey,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(banner: List<Ad>?) {
    Box(
        Modifier
            .fillMaxWidth()
    ) {
        // Display 10 items
        val pageCount = banner?.size ?: 0
        Log.e("PageCount", "$pageCount")
        // We start the pager in the middle of the raw number of pages
        val startIndex = Int.MAX_VALUE / 2
        val pagerState = rememberPagerState(initialPage = startIndex)
        val fling = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(pageCount)
        )
//        LaunchedEffect(key1 = pagerState.currentPage) {
//            if (pageCount > 1) {
//
//                delay(3000)
//                var newPosition = pagerState.currentPage + 1
//                if (newPosition > pageCount) newPosition = 0
//                // scrolling to the new position.
//                pagerState.animateScrollToPage(newPosition)
//
//            }
//        }

        LaunchedEffect(Unit) {
            while (true) {
                yield()
                delay(8000)
                try {
                    pagerState.animateScrollToPage(
                        page = (pagerState.currentPage + 1) % (pageCount),
                        animationSpec = tween(600)
                    )
                } catch (ex: ArithmeticException) {
                }
            }
        }

        HorizontalPager(
            // Set the raw page count to a really large number
            pageCount = Int.MAX_VALUE,
            state = pagerState,
            // Add 32.dp horizontal padding to 'center' the pages
            contentPadding = PaddingValues(horizontal = 0.dp),
            // Add some horizontal spacing between items
            pageSpacing = 4.dp,
            beyondBoundsPageCount = pageCount,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // We calculate the page from the given index
            PagerSampleItem(
                item = try {
                    banner?.get((it - startIndex).floorMod(pageCount))
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

fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}


@Composable
internal fun PagerSampleItem(
    item: Ad?
) {
    val context = LocalContext.current
    ElevatedCard(modifier = Modifier.height(180.dp)) {
        // Our page content, displaying a random image
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {
                try {
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(item?.url)
                    )
                    context.startActivity(urlIntent)
                } catch (ex: Exception) {

                }
            }) {
            ImageLoader(
                url = ImageUrl().getFullUrl(
                    item?.adsImage?.get(0)?.url ?: "",
                    ImageUrl().ADS
                )
            )
        }
    }
}

@Composable
fun DialogBox2FA(popup: Ad?, onDismiss: () -> Unit) {
    val contextForToast = LocalContext.current.applicationContext

    if (popup != null) {
        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.Transparent,
                elevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(400.dp)
                ) {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(0.dp),
                        shape = MaterialTheme.shapes.extraLarge.copy(CornerSize(16.dp)),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            ImageLoader(
                                url = ImageUrl().getFullUrl(
                                    popup.adsImage[0].url.toString(),
                                    ImageUrl().ADS
                                ),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(16.dp)
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                BgDark.copy(0f),
                                                BgDark.copy(0.5f), BgDark.copy(1f)
                                            )
                                        )
                                    )
                                    .fillMaxSize()
                            ) {

                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onDismiss() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = if (Utils.getLanguage(contextForToast) == Locales.RU) popup.titleRu else popup.titleTm,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )


                    }
                    ElevatedButton(
                        onClick = {
                            onDismiss()
                            try {
                                val urlIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(popup.url)
                                )
                                urlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                contextForToast.startActivity(urlIntent)
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        },
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.White),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.go_to_page),
                            style = MaterialTheme.typography.titleSmall,
                            color = BgDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonClick(
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = {
            onButtonClick()
        }) {
        Text(
            text = buttonText,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun Speedometer(navController1: NavHostController) {
    val colorStops = arrayOf(
        0.0f to Color(0xFF522631),
        0.35f to Color(0xFF231F20),
        1f to Color(0xFF231F20),
    )
    Box(
        modifier = Modifier
            .height(120.dp)
            .background(
                Brush.horizontalGradient(colorStops = colorStops),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                navController1.navigate(Routes.Speedometer.route)
            }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(32.dp)) {
                Text(
                    text = stringResource(id = R.string.speedometer),
                    style = MaterialTheme.typography.titleSmall,
                    color = Clear
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.speedometer_desc),
                    style = MaterialTheme.typography.labelMedium,
                    color = Grey
                )
            }
            Image(
                painter = painterResource(id = R.drawable.speedometer),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(24.dp))
            )
        }
    }
}




