package com.android.beyikyol2.component.page.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.beyikyol2.R
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.ShowcaseStyle
import androidx.compose.material.Scaffold

@Composable
fun ShowcaseSample() {
    var showAppIntro by remember {
        mutableStateOf(true)
    }

    IntroShowCaseScaffold(
        showIntroShowCase = showAppIntro,
        onShowCaseCompleted = {
            //App Intro finished!!
            showAppIntro = false
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.introShowCaseTarget(
                                index = 4,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = Color(0xFF7C99AC), // specify color of background
                                    backgroundAlpha = 0.98f, // specify transparency of background
                                    targetCircleColor = Color.White // specify color of target circle
                                ),
                                content = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painterResource(id = R.drawable.baseline_arrow_back_24),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(100.dp)
                                                .padding(top = 10.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "Go back!!",
                                                color = Color.White,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "You can go back by clicking here.",
                                                color = Color.White,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                },
                            )
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Search")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.introShowCaseTarget(
                                index = 2,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = Color(0xFF9AD0EC), // specify color of background
                                    backgroundAlpha = 0.98f, // specify transparency of background
                                    targetCircleColor = Color.White // specify color of target circle
                                ),
                                content = {
                                    Column {
                                        Image(
                                            painterResource(id = R.drawable.car_marker),
                                            contentDescription = null,
                                            modifier = Modifier.size(100.dp)
                                        )

                                        Text(
                                            text = "Search anything!!",
                                            color = Color.Black,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "You can search anything by clicking here.",
                                            color = Color.Black,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            )
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    modifier = Modifier.introShowCaseTarget(
                        index = 1,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color(0xFF1C0A00), // specify color of background
                            backgroundAlpha = 0.98f, // specify transparency of background
                            targetCircleColor = Color.White // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = "Check emails",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Click here to check/send emails",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Icon(
                                    painterResource(id = R.drawable.fuel),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .align(Alignment.End),
                                    tint = Color.White
                                )
                            }
                        }
                    ),
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                ) {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                }
            },
            content = {paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    Box(modifier = Modifier.fillMaxHeight(0.3f)) {

                        Column(
                            Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(90.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Intro Showcase view", fontWeight = FontWeight.Bold,
                                fontSize = 24.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                text = "This is an example of Intro Showcase view",
                                fontSize = 20.sp, color = Color.Black, textAlign = TextAlign.Center
                            )

                        }

                        Image(
                            painter = painterResource(id = R.drawable.add_photo_alternate),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .clip(CircleShape)
                                .introShowCaseTarget(
                                    index = 0, // specify index to show feature in order
                                    // ShowcaseStyle is optional
                                    style = ShowcaseStyle.Default.copy(
                                        backgroundColor = Color(0xFFFFCC80), // specify color of background
                                        backgroundAlpha = 0.98f, // specify transparency of background
                                        targetCircleColor = Color.White // specify color of target circle
                                    ),
                                    content = {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 20.dp)
                                        ) {
                                            Text(
                                                text = "User profile",
                                                color = Color.White,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Click here to update your profile",
                                                color = Color.White,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                )
                        )
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 16.dp)
                            .introShowCaseTarget(
                                index = 3,
                                content = {
                                    Column {
                                        Text(
                                            text = "Follow me",
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Click here to follow",
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            )
                    ) {
                        Text(text = "Follow")
                    }
                }
            }
        )
    }
}