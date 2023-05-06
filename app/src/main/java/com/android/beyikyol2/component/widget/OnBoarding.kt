package com.android.beyikyol2.component.widget

import android.content.Context
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.BgDark
import com.android.beyikyol2.ui.theme.Clear
import com.android.beyikyol2.ui.theme.Primary
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoarding(navController: NavController) {
    val items = OnBoardingItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    val context: Context = LocalContext.current
    Utils.setPreference("isFirst", "0", context)

    var languageOpen = rememberSaveable {
        mutableStateOf(true)
    }

    val currentLanguage = rememberSaveable {
        mutableStateOf(Utils.getLanguage(context))
    }



    if(languageOpen.value){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.select_language),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(
                            if (currentLanguage.value == Locales.TM) 1f
                            else 0.4f
                        )
                        .clickable {
                            Lingver
                                .getInstance()
                                .setLocale(context, Locales.TM)
                            currentLanguage.value = Locales.TM
                            Utils.setPreference("language", Locales.TM, context)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tm),
                        contentDescription = null
                    )
                    Text(
                        text = "Türkmen dili",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (currentLanguage.value == Locales.TM) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(
                            if (currentLanguage.value == Locales.RU) 1f
                            else 0.4f
                        )
                        .clickable {
                            Lingver
                                .getInstance()
                                .setLocale(context, Locales.RU)
                            currentLanguage.value = Locales.RU
                            Utils.setPreference("language", Locales.RU, context)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ru),
                        contentDescription = null
                    )
                    Text(
                        text = "Русский",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (currentLanguage.value == Locales.RU) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }



            }

            Spacer(modifier = Modifier.height(16.dp))

            androidx.compose.material.IconButton(onClick = {
                languageOpen.value=false
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_circle_right_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp)
                )
            }

        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {


            HorizontalPager(
                count = items.size,
                state = pageState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                OnBoardingItem(items = items[page])
            }

            TopSection(
                onSkipClick = {
                    if (pageState.currentPage + 1 < items.size) scope.launch {
                        pageState.scrollToPage(items.size - 1)
                    } else {
                        navController.navigate(Routes.MainScreen.route) {
                            popUpTo(Routes.OnBoarding.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            )

            BottomSection(
                size = items.size,
                index = pageState.currentPage,
                navController = navController,
                onButtonClick = {
                    if (pageState.currentPage + 1 < items.size) scope.launch {
                        pageState.scrollToPage(pageState.currentPage + 1)
                    }
                })
        }
    }





}

@ExperimentalPagerApi
@Composable
fun TopSection(onSkipClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        // Skip Button
        TextButton(
            onClick = onSkipClick,
            modifier = Modifier.align(Alignment.CenterEnd),
            contentPadding = PaddingValues(0.dp)
        ) {


            Text(
                text = stringResource(id = R.string.skip),
                color = Clear,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun BottomSection(
    size: Int,
    index: Int,
    onButtonClick: () -> Unit = {},
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        // Indicators
        Indicators(size, index)
        Spacer(modifier = Modifier.height(40.dp))
        val commentsAlpha = if (index != 2) 1f else 0f
        OutlinedButton(
            onClick = { onButtonClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .alpha(commentsAlpha)
        ) {
            Text(
                text = stringResource(id = R.string.next),
                color = Clear,
                fontSize = 16.sp,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(
            onClick = {
                navController.navigate(Routes.SignIn.route) {
                    popUpTo(Routes.OnBoarding.route) {
                        inclusive = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_sign_in),
                color = BgDark,
                fontSize = 16.sp,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun Indicators(
    size: Int,
    index: Int,
    activeColor: Color = Primary,
    passiveColor: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index, activeColor, passiveColor)
        }
    }
}

@Composable
fun Indicator(
    isSelected: Boolean,
    activeColor: Color = Primary,
    passiveColor: Color = Color.White
) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) activeColor else passiveColor
            )
    ) {

    }
}


@Composable
fun OnBoardingItem(items: OnBoardingItems) {
    val colorStops = arrayOf(
        0.0f to Color(0, 0, 0, 0),
        0.59f to Color(0f, 0f, 0f, 0.63f),
        1f to Color(22, 22, 22, 1)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .paint(painter = painterResource(id = items.image), contentScale = ContentScale.Crop)
            .background(Brush.verticalGradient(colorStops = colorStops))
    ) {

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = stringResource(id = items.title),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = items.desc),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            letterSpacing = 1.sp,
        )
    }
}