package com.android.beyikyol2.component.page.other

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.common.PageBodyWithWebView
import com.android.beyikyol2.component.common.WebViewComponent
import com.android.beyikyol2.core.util.LocaleUtils
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import kotlinx.coroutines.delay
import java.util.logging.Handler

@Composable
fun ConstantPage(navController: NavController, type: String) {
    val viewModel: GetHomeViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.getConstantPage(type)
    }

    BackHandler {
        LocaleUtils.setLocale(context, Utils.getLanguage(context))
        navController.popBackStack()
    }

    if (state.constantData == null) {
        Loading()
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    LocaleUtils.setLocale(context, Utils.getLanguage(context))
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (Utils.getLanguage(context) == Locales.RU) state.constantData.name_ru else state.constantData.name_tm,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

            }

            PageBodyWithWebView(
                htmlPage = if (Utils.getLanguage(context) == Locales.RU) state.constantData.content_ru else state.constantData.content_tm,
                onWebpageScroll = { _, _ -> {} },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surface
            )
        }


    }
}