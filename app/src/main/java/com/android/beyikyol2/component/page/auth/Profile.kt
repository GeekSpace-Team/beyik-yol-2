package com.android.beyikyol2.component.page.auth

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.ColorFilter
import android.net.Uri
import android.os.Build
import android.widget.Space
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.BuildConfig
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.Alert
import com.android.beyikyol2.component.common.AlertProperties
import com.android.beyikyol2.component.common.ImageLoader
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.core.util.ImageUrl
import com.android.beyikyol2.core.util.LocaleUtils
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_profile.presentation.ProfileViewModel
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.BgDark
import com.android.beyikyol2.ui.theme.Tonal
import com.android.beyikyol2.ui.theme.Tonal10
import com.android.beyikyol2.ui.theme.Warning
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Profile(
    navController: NavController,
    onToggleTheme: () -> Unit,
    onChangeLanguage: (String) -> Unit
) {
    val context: Context = LocalContext.current
    val viewModel: ProfileViewModel = hiltViewModel()
    val state = viewModel.state.value
    val isLoad = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        viewModel.getProfile(Utils.getToken(context))
    }
    if (state.profileState == null && Utils.isLogin(context)) {
        Loading()
    } else {

        isLoad.value = true
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState =
            BottomSheetState(BottomSheetValue.Collapsed)
        )

        // Declaring Coroutine scope
        val coroutineScope = rememberCoroutineScope()

        val currentLanguage = remember {
            mutableStateOf(Utils.getLanguage(context))
        }


        var uri = "play.google.com/store/apps/details?id=${context.packageName}"

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Download Beyik yol App from here $uri")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)

        var open = remember {
            mutableStateOf(false)
        }

        if (open.value) {
            Alert(props = AlertProperties(
                openDialog = open,
                title = R.string.attention,
                body = R.string.want_logout,
                onOkPressed = {
                    Utils.setPreference("token", "", context)
                    navController.popBackStack()
                },
                onCancelPressed = {
                    open.value = false
                }
            ))
        }

        val configuration = LocalConfiguration.current
        // Creating a Bottom Sheet
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
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
                        Text(
                            text = stringResource(id = R.string.select_language),
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
//                                    onChangeLanguage(Locales.TM)
                                    Utils.setPreference("language", Locales.TM, context)
                                    Lingver
                                        .getInstance()
                                        .setLocale(context, Locales.TM)
                                    currentLanguage.value = Locales.TM
                                }
                                .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = stringResource(id = R.string.turkmen),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                if (currentLanguage.value == Locales.TM) {
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
                                    Utils.setPreference("language", Locales.RU, context)
//                                    val locale = Locale(Locales.RU)
//
//                                    configuration.setLocale(locale)
//                                    context.createConfigurationContext(configuration)
                                    Lingver
                                        .getInstance()
                                        .setLocale(context, Locales.RU)
                                    currentLanguage.value = Locales.RU
                                }
                                .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = stringResource(id = R.string.russian),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                if (currentLanguage.value == Locales.RU) {
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
            sheetBackgroundColor = BgDark.copy(alpha = 0.3f),
            backgroundColor = BgDark.copy(alpha = 0.3f),
            drawerBackgroundColor = BgDark.copy(alpha = 0.3f),
            sheetPeekHeight = 0.dp
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(355.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        if (state.profileState != null) {

                            ImageLoader(
                                url = ImageUrl().getFullUrl(
                                    state.profileState.image,
                                    ImageUrl().USER
                                )
                            )
                        } else {
                            Icon(
                                Icons.Default.Person, contentDescription = null, modifier = Modifier
                                    .align(
                                        Alignment.Center
                                    )
                                    .size(100.dp), tint = Color.White
                            )
                        }


                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Text(
                            text = stringResource(id = com.android.beyikyol2.R.string.back),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 340.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
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


                        if (state.profileState != null) {
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = state.profileState.fullname.ifBlank {
                                    state.profileState.username
                                },
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column() {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Tel:",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Spacer(modifier = Modifier.width(22.dp))
                                        Text(
                                            text = state.profileState.phonenumber,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stringResource(id = R.string.created_at),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Spacer(modifier = Modifier.width(22.dp))
                                        Text(
                                            text = state.profileState.createdAt.split("T")[0],
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                                if (Utils.isLogin(context)) {
                                    IconButton(onClick = {
                                        navController.navigate(Routes.EditProfile.route)
                                    }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = Tonal
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(20.dp))

                            ElevatedButton(
                                onClick = { navController.navigate(Routes.SignIn.route + "?type=home") },
                                colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (currentLanguage.value == Locales.RU) "Войти" else "Içeri gir",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(7.dp))
                        }


                        Spacer(modifier = Modifier.height(25.dp))

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { context.startActivity(shareIntent) },
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = Tonal10
                            ),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Tonal)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (currentLanguage.value == Locales.RU) "Поделиться с друзьями" else "Dostlaryň bilen paýlaş",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Tonal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            if (state.profileState != null) {
                                InfoItem(
                                    icon = R.drawable.my_cars,
                                    title = R.string.my_cars_title,
                                    data = state.profileState.cars?.size.toString()
                                ) {
                                    navController.navigate(Routes.Cars.route)
                                }
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(0.2.dp)
                                        .background(MaterialTheme.colorScheme.onSecondary)
                                )
                                InfoItem(
                                    icon = R.drawable.my_costs,
                                    title = R.string.my_costs,
                                    data = "${state.profileState.costs} TMT"
                                ) {
                                    navController.navigate(Routes.Costs.route)
                                }
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(0.2.dp)
                                        .background(MaterialTheme.colorScheme.onSecondary)
                                )
                            }

                            InfoItem(
                                icon = R.drawable.twotone_info_24,
                                title = R.string.about,
                                data = ""
                            ) {
                                navController.navigate(Routes.ConstantPage.route + "/ABOUT_US")
                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.2.dp)
                                    .background(MaterialTheme.colorScheme.onSecondary)
                            )
                            InfoItem(
                                icon = R.drawable.twotone_privacy_tip_24,
                                title = R.string.privacy,
                                data = ""
                            ) {
                                navController.navigate(Routes.ConstantPage.route + "/PRIVACY_POLICY")
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            SettingItem(
                                icon = R.drawable.language,
                                title = R.string.language,
                                data = if (currentLanguage.value == Locales.RU) stringResource(R.string.russian) else stringResource(
                                    R.string.turkmen
                                )
                            ) {
                                coroutineScope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.2.dp)
                                    .background(MaterialTheme.colorScheme.onSecondary)
                            )
//                            SettingItemWithSwitch(
//                                icon = R.drawable.accident,
//                                title = R.string.accident,
//                                checked = false,
//                                onChange = {
//
//                                })
//                            Divider(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(0.2.dp)
//                                    .background(MaterialTheme.colorScheme.onSecondary)
//                            )
                            SettingItemWithSwitch(
                                icon = R.drawable.notification,
                                title = stringResource(id = R.string.notifications),
                                checked = Utils.getSharedPreference(
                                    context,
                                    "showNotification"
                                ) != "0",
                                onChange = {
                                    if (it) {
                                        Utils.setPreference("showNotification", "1", context)
                                    } else {
                                        Utils.setPreference("showNotification", "0", context)
                                    }
                                })
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.2.dp)
                                    .background(MaterialTheme.colorScheme.onSecondary)
                            )
                            SettingItemWithSwitch(
                                icon = R.drawable.baseline_nights_stay_24,
                                title = if (currentLanguage.value == Locales.RU) "Темный режим" else "Garaňky tema",
                                checked = Utils.isDark(context),
                                onChange = {
                                    onToggleTheme()
                                })
                        }


                        if (state.profileState != null) {
                            Spacer(modifier = Modifier.height(25.dp))
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
                                        open.value = true
                                    }
                                    .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.logout),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = stringResource(id = R.string.logout),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Warning
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Beyik yol App",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                text = "v. ${BuildConfig.VERSION_NAME}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }


                }
            }


        }


    }
}

@Composable
fun InfoItem(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    data: String,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = data,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SettingItem(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    data: String,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = data,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SettingItemWithSwitch(
    @DrawableRes icon: Int,
    title: String,
    onChange: (Boolean) -> Unit,
    checked: Boolean = false
) {
    val check = remember {
        mutableStateOf(checked)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Switch2(onChange = {
            onChange(it)
        }, checked)
    }
}

@Composable
fun Switch2(
    onChange: (Boolean) -> Unit,
    checked: Boolean = false,
    scale: Float = 2f,
    width: Dp = 30.dp,
    height: Dp = 16.dp,
    strokeWidth: Dp = 1.dp,
    checkedTrackColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedTrackColor: Color = MaterialTheme.colorScheme.onSecondary,
    gapBetweenThumbAndTrackEdge: Dp = 4.dp,
) {

    val switchON = remember {
        mutableStateOf(checked) // Initially the switch is ON
    }

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    Canvas(
        modifier = Modifier
            .padding(end = 16.dp)
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onChange(!switchON.value)
                        // This is called when the user taps on the canvas
                        switchON.value = !switchON.value

                    }
                )
            }
    ) {
        // Track
        drawRoundRect(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx()),
        )

        // Thumb
        drawCircle(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}
