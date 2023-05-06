package com.android.beyikyol2.component.common

import android.util.LayoutDirection
import android.util.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.size.Scale
import com.android.beyikyol2.R
import com.android.beyikyol2.ui.theme.*
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideRequestType
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.coroutines.withTimeout
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

data class AlertProperties(
    val openDialog: MutableState<Boolean>,
    val onOkPressed: () -> Unit,
    val onCancelPressed: () -> Unit,
    val title: Int,
    val body: Int,
    val okText: Int = R.string.yes,
    val cancelText: Int = R.string.no
)

val sampleUrl = "https://tmcars.info/tmcars/images/original/2023/02/15/12/30/8b4d373b-4500-4e9e-b47b-2987ed4eaee8.jpg"

@Composable
fun ProgressDialog(onDismissRequest:()->Unit) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment= Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun Alert(props: AlertProperties) {

    if (props.openDialog.value) {
        androidx.compose.material.AlertDialog(
            modifier = Modifier.padding(22.dp),
            onDismissRequest = { props.openDialog.value = false },
            title = {
                Text(
                    text = stringResource(id = props.title),
                    style = MaterialTheme.typography.titleMedium,
                    color = BgDark
                )
            },
            text = {
                Text(
                    text = stringResource(id = props.body),
                    style = MaterialTheme.typography.bodySmall,
                    color = BgDark
                )
            },
            confirmButton = {
                TextButton(onClick = props.onOkPressed) {
                    Text(
                        text = stringResource(id = props.okText),
                        style = MaterialTheme.typography.titleSmall,
                        color = Primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = props.onCancelPressed) {
                    Text(
                        text = stringResource(id = props.cancelText),
                        style = MaterialTheme.typography.titleSmall,
                        color = Primary
                    )
                }
            },
            shape = MaterialTheme.shapes.extraLarge,
            backgroundColor = AlertBg
        )
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun UserHead(
    id: String,
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        val color = remember(id, firstName, lastName) {
            val name = listOf(firstName, lastName)
                .joinToString(separator = "")
                .uppercase()
            Color("$id / $name".toHslColor())
        }
        val initials = (firstName.take(1) + lastName.take(1)).uppercase()
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color))
        }
        Text(text = initials, style = textStyle, color = Color.White)
    }
}

@Composable
fun ImageLoader(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds
) {

    val context = LocalContext.current
    val imageLoader = coil.ImageLoader.Builder(LocalContext.current)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale=contentScale,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )

//    GlideImage(
//        imageModel = { url }, // loading a network image using an URL.
//        failure={
//
//        },
//        glideRequestType=GlideRequestType.DRAWABLE,
//        imageOptions = ImageOptions(
//            contentScale = contentScale,
//            alignment = Alignment.Center
//        ),
//        component = rememberImageComponent {
//            // shows a shimmering effect when loading an image.
//            +ShimmerPlugin(
//                baseColor = Skeleton,
//                highlightColor = Clear
//            )
//            +CrossfadePlugin(
//                duration = 550
//            )
//        },
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//    )
}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(
                painterResource(id = R.drawable.baseline_star_half_24),
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                painterResource(id = R.drawable.baseline_star_outline_24),
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}


@Composable
fun FullScreenDialog(onDismiss: () -> Unit, label: String, content: @Composable () -> Unit) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false // experimental
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material.Text(
                        text = label,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        onDismiss()
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        }
    }

}

