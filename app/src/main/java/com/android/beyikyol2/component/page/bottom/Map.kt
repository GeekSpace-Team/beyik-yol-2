package com.android.beyikyol2.component.page.bottom

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.OnLifecycleEvent
import com.android.beyikyol2.component.page.map.FindPlaceUi
import com.android.beyikyol2.component.page.map.MapResult
import com.android.beyikyol2.component.page.speedometer.LocationDetails
import com.android.beyikyol2.core.util.Utils.Utils.bitmapDescriptor
import com.android.beyikyol2.core.util.Utils.Utils.isPackageInstalled
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map(
    startLocation: () -> Unit,
    stopLocation: () -> Unit,
    locationDetails: LocationDetails,
) {

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    var isMoveWith by remember {
        mutableStateOf(true)
    }

    SideEffect {
        if (!locationPermissionsState.allPermissionsGranted) {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size
            locationPermissionsState.launchMultiplePermissionRequest()
        }

    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var searchResult by remember {
        mutableStateOf(MapResult())
    }

    val viewModel: GetHomeViewModel = hiltViewModel()
    val state = viewModel.state.value

    val zoom by remember {
        mutableStateOf(17f)
    }
    val singapore =
        if (locationDetails.latitude == 0.0) LatLng(37.9600766, 58.3260629) else LatLng(
            locationDetails.latitude,
            locationDetails.longitude
        )
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, zoom)
    }

    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        Dialog(onDismissRequest =  {
            showDialog=false
        },properties = DialogProperties(
            usePlatformDefaultWidth = false // experimental
        ) ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                color = MaterialTheme.colorScheme.surface
            ) {
                FindPlaceUi {
                    isMoveWith=false
                    searchResult=it
                    showDialog=false
                    coroutineScope.launch {
                        val pos = CameraPosition(LatLng(it.lat,it.lng),cameraPositionState.position.zoom,cameraPositionState.position.tilt,0f)
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newCameraPosition(
                                CameraPosition(pos.target, pos.zoom, pos.tilt, pos.bearing)
                            ),
                            durationMs = 1000
                        )
                    }
                }
            }
        }
    }

    val context = LocalContext.current

    fun getLocationManager(context: Context): LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun isLocEnable(context: Context): Boolean {
        val locationManager = getLocationManager(context)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    var isDraw by rememberSaveable {
        mutableStateOf(true)
    }

    fun checkGps(): Boolean{
        if(!isLocEnable(context)) {
            Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS,
                Uri.fromParts(
                    "package",
                    context.packageName,
                    null
                )
            ).also { intent ->
                try {
                    context.startActivity(
                        intent
                    )
                } catch (e: ActivityNotFoundException) {
                    Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    ).also { intentCatch ->
                        context.startActivity(
                            intentCatch
                        )
                    }
                }

            }
        }
        return isLocEnable(context)
    }

    LaunchedEffect(true) {
        if(locationPermissionsState.allPermissionsGranted){
            startLocation()
        }

        checkGps()

    }

    var rotate by rememberSaveable {
        mutableStateOf(0f)
    }

    var currentRotation = 0f
    var lastRotation = 0f
    var lastTime: Long = 0
    val filterFactor = 0.2f

    var oldRotate = 0f

    fun createSensorEventListener(): SensorEventListener {
        return object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                        val rotationMatrix = FloatArray(9)
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)

                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(rotationMatrix, orientation)

                        val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                        val y = Math.toDegrees(orientation[1].toDouble()).toFloat()
                        val z = Math.toDegrees(orientation[2].toDouble()).toFloat()

                        val dif = abs(azimuth)-abs(oldRotate)
                        if(abs(dif)>3f)
                        {
                            rotate = azimuth
                            oldRotate = azimuth
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    val sensorEventListener = createSensorEventListener()
    LaunchedEffect(true){
        sensorManager.registerListener(sensorEventListener, rotationSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW)
    }



    var mode by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(searchResult){
        if(searchResult.lat!=0.0 && searchResult.lng!=0.0){
            val hashmap = hashMapOf<String,Any>()
            hashmap["origin"]="${locationDetails.latitude},${locationDetails.longitude}"
            hashmap["destination"]="${searchResult.lat},${searchResult.lng}"
            hashmap["sensor"] = "sensor=false"
            viewModel.getDirection(hashmap)
            isDraw=true
        }
    }


    OnLifecycleEvent { owner, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                startLocation()
            }
             Lifecycle.Event.ON_DESTROY -> {
                 sensorManager.unregisterListener(sensorEventListener)
             }
            else -> {}
        }
    }


    if(locationPermissionsState.allPermissionsGranted) {
        LaunchedEffect(rotate,locationDetails) {
            if(isMoveWith){
                val pos = CameraPosition(singapore,cameraPositionState.position.zoom,cameraPositionState.position.tilt,rotate)
//                if(pos.bearing!=0f){
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(pos.target, pos.zoom, pos.tilt, rotate)
                    ),
                    durationMs = 200
                )
//                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        var search = rememberSaveable {
            mutableStateOf("")
        }
        
        
        

        Column {

            var mapUiSettings by remember {
                mutableStateOf(
                    MapUiSettings(mapToolbarEnabled = true)
                )
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionsState.allPermissionsGranted,
                    isBuildingEnabled = true,
                    isTrafficEnabled = false,
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context,if(mode) R.raw.dark_mode else R.raw.standart)
                ),
                uiSettings = mapUiSettings,
                onMapClick = {
                    searchResult=MapResult(it.latitude,it.longitude,"Selected point","")
                },
                onMyLocationButtonClick = {
                    isMoveWith=true
                    false
                },
            ) {
                val icon = bitmapDescriptor(
                    context, R.drawable.user_arrow
                )
                Marker(
                    state = MarkerState(position = singapore),
                    title = "My location",
                    snippet = "My location",
                    icon = icon,
                    rotation = rotate,
                    flat = true,
                    anchor = Offset(0.5f, 0.5f),
                    zIndex = 10f
                )

                if(searchResult.lat!=0.0 && searchResult.lng!=0.0){
                    Marker(
                        state = MarkerState(position = LatLng(searchResult.lat,searchResult.lng)),
                        title = searchResult.title,
                        snippet = searchResult.desc
                    )


                }

                if(state.directionState!=null && isDraw){
                    val polypts = try{
                        state.directionState?.routes?.get(0)?.legs?.get(0)?.steps?.flatMap { decodePoly(it.polyline.points) }
                    } catch (ex: Exception){null}
                    if(polypts!=null){
                        Polyline(
                            points = polypts,
                            color = MaterialTheme.colorScheme.primary,
                            width = 20f
                        )
                    }
                }
            }
        }

//        ElevatedCard(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            shape = MaterialTheme.shapes.extraLarge.copy(CornerSize(28.dp))
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Icon(
//                    Icons.Default.Search,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary
//                )
//                BasicTextField(
//                    value = search.value,
//                    onValueChange = {
//                        search.value = it
//                    },
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(start = 12.dp),
//                    textStyle = inputFontStyle.copy(color = MaterialTheme.colorScheme.onBackground),
//                    decorationBox = { innerTextField ->
//                        if (search.value.isEmpty()) {
//                            Text(
//                                text = stringResource(id = R.string.search),
//                                style = inputFontStyle,
//                                color = Grey
//                            )
//                        }
//                        innerTextField()
//                    },
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
//                )
//            }
//        }
        
        if(state.directionState!=null && isDraw){
            if(state.directionState?.routes==null || state.directionState?.routes?.size!! <=0){

            } else {
                val route = state.directionState?.routes?.get(0)?.legs?.get(0)
                ElevatedCard(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        androidx.compose.material.IconButton(onClick = {
                            isDraw=false
                            state.directionState=null
                        }, modifier = Modifier.background(MaterialTheme.colorScheme.primary, shape = CircleShape)) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(id = R.string.distance), color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleSmall)
                            Text(text = route?.distance?.text?:"0 km", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                        }
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(id = R.string.time), color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleSmall)
                            Text(text = route?.duration?.text?:"0 min", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }



        FloatingActionButton(
            onClick = {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "You can find me from here: beyikyol.com/loc=${locationDetails.latitude},${locationDetails.longitude}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 120.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ){
            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
        }

        Row(modifier = Modifier.align(Alignment.BottomStart)) {
            FloatingActionButton(
                onClick = {
                    if(searchResult.lat!=0.0 && searchResult.lng!=0.0){
                        drawTrack(searchResult.lat.toString(),searchResult.lng.toString(),context)
                    } else {
                        val pm: PackageManager = context.packageManager
                        val isInstalled: Boolean = isPackageInstalled("com.mapswithme.maps.pro", pm)
//                if(isInstalled){
                        val urlIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://ge0.me")
                        )
                        context.startActivity(urlIntent)
                    }
//                } else {
//                    val urlIntent = Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("https://play.google.com/store/apps/details?id=com.mapswithme.maps.pro")
//                    )
//                    context.startActivity(urlIntent)
//                }
                },
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ){
                Icon(painterResource(id = R.drawable.outline_directions_24), contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            FloatingActionButton(
                onClick = {
                    showDialog=true
                },
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ){
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            FloatingActionButton(
                onClick = {
                    mode=!mode
                },
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ){
                Icon(painterResource(id = if(mode) R.drawable.baseline_wb_sunny_24 else R.drawable.baseline_nights_stay_24), contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            FloatingActionButton(
                onClick = {
                    isMoveWith=!isMoveWith
                },
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ){
                Icon(painterResource(id = if(isMoveWith) R.drawable.baseline_videocam_24 else R.drawable.baseline_videocam_off_24), contentDescription = null, tint = Color.White)
            }
        }


    }



}

private fun decodePoly(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(lat.toDouble() / 1E5,
            lng.toDouble() / 1E5)
        poly.add(p)
    }

    return poly
}

private fun drawTrack(lat: String, lon: String, context: Context) {
    try {
        // create a uri
        val uri: Uri = Uri.parse("google.navigation:q=$lat,$lon")
        // initializing a intent with action view.
        val i = Intent(Intent.ACTION_VIEW, uri)
        // below line is to set maps package name
        i.setPackage("com.google.android.apps.maps")
        // below line is to set flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // start activity
        context.startActivity(i)
    } catch (e: ActivityNotFoundException) {
        // when the google maps is not installed on users device
        // we will redirect our user to google play to download google maps.
        val uri: Uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
        // initializing intent with action view.
        val i = Intent(Intent.ACTION_VIEW, uri)
        // set flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // to start activity
        context.startActivity(i)
    }
}