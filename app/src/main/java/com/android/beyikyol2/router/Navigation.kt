package com.android.beyikyol2.router

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.beyikyol2.component.page.Home
import com.android.beyikyol2.R
import com.android.beyikyol2.component.page.bottom.Map
import com.android.beyikyol2.component.page.bottom.Service
import com.android.beyikyol2.component.page.speedometer.LocationDetails
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import com.android.beyikyol2.ui.theme.BgDark
import com.android.beyikyol2.ui.theme.Clear
import com.android.beyikyol2.ui.theme.Primary

sealed class Screens(val title: String, val route: String, @DrawableRes val icons: Int, @DrawableRes val selectedIcon: Int){
    object Home: Screens(
        title = "home",
        route = "home_route",
        icons = R.drawable.home_passive,
        selectedIcon = R.drawable.home_active
    )
    object Map: Screens(
        title = "Map",
        route = "map_route",
        icons = R.drawable.map_passive,
        selectedIcon = R.drawable.map_active
    )
    object Service: Screens(
        title = "Service",
        route = "service_route",
        icons = R.drawable.service_passive,
        selectedIcon = R.drawable.service_active
    )
    object Shop: Screens(
        title = "Shop",
        route = "shop_route",
        icons = R.drawable.shop_passive,
        selectedIcon = R.drawable.shop_active
    )
}

sealed class Routes(val route: String){
    object Splash: Routes("splash_screen")
    object OnBoarding: Routes("on_boarding")
    object SignIn: Routes("sign_in")
    object ConfirmCode: Routes("confirm_code")
    object SignUp: Routes("sign_up")
    object Cars: Routes("cars")
    object CarView: Routes("car_view")
    object MainScreen: Routes("main_screen")
    object Profile: Routes("profile")
    object EditProfile: Routes("edit_profile")
    object Inbox: Routes("inbox")
    object Evacuator: Routes("evacuator")
    object ConstantPage: Routes("constant_page")
    object Speedometer: Routes("speedometer")
    object AddCar: Routes("add_car")
    object EditCar: Routes("edit_car")
    object Costs: Routes("costs")
    object AddCost: Routes("add_cost")
    object EditCost: Routes("edit_cost")

}

@Composable
fun BottomNavHost(
    navHostController: NavHostController,
    navController1: NavHostController,
    startLocation: () -> Unit,
    stopLocation: () -> Unit,
    locationDetails: LocationDetails,
) {
    NavHost(navController = navHostController, startDestination = Screens.Home.route){
        composable(route = Screens.Home.route){
            Home(navController1)
        }
        composable(route = Screens.Map.route){
            Map(startLocation, stopLocation, locationDetails)
        }
        composable(route = Screens.Service.route){
            Service(navController1)
        }
    }
}

@Composable
fun BottomNavigationScreen(navController: NavController,items: List<Screens>,stopLocation: () -> Unit,) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.height(80.dp)
    ) {
        items.forEach{screens ->
            val selected = currentDestination?.route == screens.route
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    if(screens.route==Screens.Map.route){
                        stopLocation()
                    }
                          navController.navigate(screens.route){
                              launchSingleTop = true
                              popUpTo(navController.graph.findStartDestination().id){
                                  saveState = true
                              }
                              restoreState = true
                          }
                },
                icon = {
                       Box(modifier = Modifier.align(Alignment.CenterVertically)){
                           if(selected){
                               Icon(painter = painterResource(id = screens.selectedIcon), contentDescription = null, modifier = Modifier.height(40.dp))
                           } else {
                               Icon(painter = painterResource(id = screens.icons), contentDescription = null, modifier = Modifier.height(40.dp))
                           }
                       }
                },
                label = {},
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSecondary,

            )
        }
    }
}

