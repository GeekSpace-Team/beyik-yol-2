package com.android.beyikyol2.component.page

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.android.beyikyol2.component.page.speedometer.LocationDetails
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import com.android.beyikyol2.router.BottomNavHost
import com.android.beyikyol2.router.BottomNavigationScreen
import com.android.beyikyol2.router.Screens
import com.android.beyikyol2.ui.theme.Bg
import com.android.beyikyol2.ui.theme.BgDark
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    navController1: NavHostController,
    startLocation: () -> Unit,
    stopLocation: () -> Unit,
    locationDetails: LocationDetails,
) {

    val listItems = listOf(
        Screens.Home,
        Screens.Map,
        Screens.Service
    )
    Scaffold(
        bottomBar = {
            BottomNavigationScreen(navController = navController, items = listItems,stopLocation)
        }
    ) { contentPadding ->
        // Screen content
        Box(modifier = Modifier.padding(contentPadding).fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
            BottomNavHost(navHostController = navController,navController1,startLocation, stopLocation, locationDetails)
        }
    }
}