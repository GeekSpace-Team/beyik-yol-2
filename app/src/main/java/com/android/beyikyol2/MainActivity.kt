package com.android.beyikyol2

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.beyikyol2.component.page.MainScreen
import com.android.beyikyol2.component.page.SplashScreen
import com.android.beyikyol2.component.page.auth.*
import com.android.beyikyol2.component.page.car.AddCar
import com.android.beyikyol2.component.page.car.CarView
import com.android.beyikyol2.component.page.car.CarsPage
import com.android.beyikyol2.component.page.car.EditCar
import com.android.beyikyol2.component.page.cost.AddCost
import com.android.beyikyol2.component.page.cost.Cost
import com.android.beyikyol2.component.page.cost.EditCost
import com.android.beyikyol2.component.page.inbox.Inbox
import com.android.beyikyol2.component.page.other.ConstantPage
import com.android.beyikyol2.component.page.speedometer.LocationDetails
import com.android.beyikyol2.component.page.speedometer.SpeedTestScreen
import com.android.beyikyol2.component.widget.OnBoarding
import com.android.beyikyol2.core.util.LocaleUtils
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.lyricist.*
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.BeyikYol2Theme
import com.android.beyikyol2.ui.theme.Bg
import com.android.beyikyol2.ui.theme.BgDark
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.sqrt


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var application: BeyikYolApp

    private var locationCallback: LocationCallback? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        setContent {
            val context = LocalContext.current
            LaunchedEffect(true) {
//                Toast.makeText(context,Utils.getLanguage(context),Toast.LENGTH_SHORT).show()
                LocaleUtils.setLocale(context, Utils.getLanguage(context))
                application.isDark.value = Utils.isDark(context = context)
            }

            var currentLocation by remember {
                mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble(), 0, null))
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    for (lo in p0.locations) {
                        // Update UI with location data
                        val speed = (lo.speed * 3.6f)

                        currentLocation =
                            LocationDetails(lo.latitude, lo.longitude, speed.toInt(), lo)
                    }
                }
            }

            val launcherMultiplePermissions = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionsMap ->
                val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                if (areGranted) {
                    locationRequired = true
                    startLocationUpdates()
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }

            val permissions = arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )


            fun startLocation() {
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    // Get the location
                    startLocationUpdates()
                } else {
                    launcherMultiplePermissions.launch(permissions)
                }
            }

            fun changeLanguage(lang: String) {
                Utils.setPreference("language", lang, context)
//                LocaleUtils.setLocale(context, lang)
//                recreate()
            }
            BeyikYol2Theme(
                darkTheme = application.isDark.value
            ) {
                val systemUiController = rememberSystemUiController()




                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = if (application.isDark.value) BgDark else Bg
                    )
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Navigation(onToggleTheme = {
                        Log.e("Mode", "Working")
                        application.toggleMode(context)
                    }, onChangeLanguage = {
                        changeLanguage(it)
                    }, startLocation = {
                        startLocation()
                    }, locationDetails = currentLocation,
                        stopLocation = {
                            locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
                        }
                    )
                }
            }


        }




    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 10
                fastestInterval = 5
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }


    fun speak() {

    }

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }



    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }

    // [START ask_post_notifications]
// Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {

        }
    }

    private fun askNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
// [END ask_post_notifications]
}


@Composable
fun Navigation(
    onToggleTheme: () -> Unit,
    onChangeLanguage: (String) -> Unit,
    startLocation: () -> Unit,
    locationDetails: LocationDetails,
    stopLocation: () -> Unit
) {
    val navController = rememberNavController()
    val navController2 = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }



        composable(Routes.OnBoarding.route) {
            OnBoarding(navController)
        }

        composable(
            Routes.SignIn.route + "?type={type}",
            arguments = listOf(navArgument("type") { defaultValue = "start" })
        ) { backStackEntry ->
            SignIn(navController, backStackEntry.arguments?.getString("type")!!)
        }

        composable(Routes.ConfirmCode.route + "/{uuid}/{phone}/{sms_phone}/{is_exist}?type={type}",
            arguments = listOf(
                navArgument("uuid") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType },
                navArgument("sms_phone") { type = NavType.StringType },
                navArgument("is_exist") { type = NavType.BoolType },
                navArgument("type") { defaultValue = "start" }
            )) { backStackEntry ->

            ConfirmCode(
                navController,
                backStackEntry.arguments?.getString("uuid")!!,
                backStackEntry.arguments?.getString("phone")!!,
                backStackEntry.arguments?.getString("sms_phone")!!,
                backStackEntry.arguments?.getBoolean("is_exist")!!,
                backStackEntry.arguments?.getString("type")!!
            )
        }

        composable(Routes.SignUp.route) {
            SignUp(navController)
        }

        composable(Routes.Cars.route) {
            CarsPage(navController)
        }

        composable(
            Routes.CarView.route + "/{id}",
            arguments = listOf(navArgument("id") { defaultValue = "0" })
        ) { backStackEntry ->
            CarView(navController, backStackEntry.arguments?.getString("id")!!)
        }


        composable(Routes.EditProfile.route) {
            EditProfile(navController)
        }

        composable(Routes.Inbox.route) {
            Inbox(navController)
        }

        composable(Routes.Speedometer.route) {
            SpeedTestScreen(startLocation, stopLocation, locationDetails, navController)
        }
        composable(Routes.AddCar.route) {
            AddCar(navController)
        }

        composable(Routes.Costs.route) {
            Cost(navController)
        }

        composable(
            Routes.AddCost.route + "/{type}/{title}/{image}/{carId}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType },
                navArgument("carId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            AddCost(
                navController,
                backStackEntry.arguments?.getString("type")!!,
                backStackEntry.arguments?.getString("title")!!,
                backStackEntry.arguments?.getString("image")!!,
                backStackEntry.arguments?.getString("carId")!!,
            )
        }

        composable(
            Routes.EditCost.route + "/{type}/{title}/{image}/{id}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            EditCost(
                navController,
                backStackEntry.arguments?.getString("type")!!,
                backStackEntry.arguments?.getString("title")!!,
                backStackEntry.arguments?.getString("image")!!,
                backStackEntry.arguments?.getString("id")!!,
            )
        }

        composable(
            Routes.ConstantPage.route + "/{type}",
            arguments = listOf(navArgument("type") { defaultValue = "start" })
        ) { backStackEntry ->
            ConstantPage(navController, backStackEntry.arguments?.getString("type")!!)
        }

        composable(
            Routes.EditCar.route + "/{id}",
            arguments = listOf(navArgument("id") { defaultValue = "0" })
        ) { backStackEntry ->
            EditCar(navController, backStackEntry.arguments?.getString("id")!!)
        }

        composable(Routes.Profile.route) {
            Profile(navController, onToggleTheme = onToggleTheme, onChangeLanguage = {
                onChangeLanguage(it)
            })
        }

        composable(Routes.MainScreen.route) {
            MainScreen(
                navController = navController2,
                navController,
                startLocation,
                stopLocation,
                locationDetails
            )
        }
    }
}


