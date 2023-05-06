package com.android.beyikyol2.component.common

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.android.beyikyol2.R

@Composable
fun Loading() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@Composable
fun StateMessage(
    @StringRes message: Int,
    @StringRes actionText: Int,
    @RawRes lottieFile: Int,
    onClick: ()->Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottieFile))
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(150.dp).clickable { onClick() },
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(id = message),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(22.dp))
        ElevatedButton(onClick = { onClick() }, colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary)) {
            androidx.compose.material3.Text(
                text = stringResource(id = actionText),
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
    }
}