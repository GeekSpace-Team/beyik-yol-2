package com.android.beyikyol2.component.page.car

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.ImageLoader
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.common.ProgressDialog
import com.android.beyikyol2.core.util.FileUtils
import com.android.beyikyol2.core.util.ImageUrl
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_car.data.remote.dto.CarBrand
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.AddCarBody
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.Brand
import com.android.beyikyol2.feature_car.presentation.CarViewModel
import com.android.beyikyol2.ui.theme.Tonal
import com.android.beyikyol2.ui.theme.inputFontStyle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditCar(navController: NavController, id: String) {
    val viewModel: CarViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.getAddCarDetails()
    }

    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    var selectImages by remember { mutableStateOf(listOf<Uri>()) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            var newImages = selectImages.plus(it)
            selectImages = newImages
        }
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
    val isRu = Utils.getLanguage(context) == Locales.RU


    var showToast = remember {
        mutableStateOf(false)
    }

    var message = remember {
        mutableStateOf(R.string.error)
    }

    var toastType = remember {
        mutableStateOf("error")
    }


    if (showToast.value) {
        if (toastType.value == "error") {
            SweetToastUtil.SweetError(message = stringResource(id = message.value))
        } else {
            SweetToastUtil.SweetSuccess(message = stringResource(id = message.value))
        }
        showToast.value = false
    }

    if (state.addCarDetails == null) {
        Loading()
    } else if(state.singleCar==null){
        Loading()
        LaunchedEffect(true){
            viewModel.getCarById(Utils.getToken(context),id)
        }
    } else {


        var brandId by remember {
            mutableStateOf(state.singleCar.carModel.brandId)
        }
        var brand by remember {
            mutableStateOf<Brand?>(state.singleCar.carModel.brand)
        }
        var modelId by remember {
            mutableStateOf(state.singleCar.modelId)
        }
        var optionId by remember {
            mutableStateOf(state.singleCar.optionId)
        }
        var engineTypeId by remember {
            mutableStateOf(state.singleCar.engineTypeId)
        }
        var transmitionId by remember {
            mutableStateOf(state.singleCar.transmitionId)
        }
        var year by remember {
            mutableStateOf(state.singleCar.year.toString())
        }
        var lastMile by remember {
            mutableStateOf(state.singleCar.lastMile.toString())
        }
        var power by remember {
            mutableStateOf(state.singleCar.enginePower.toString())
        }
        var vinCode by remember {
            mutableStateOf(state.singleCar.vinCode.toString())
        }
        var phoneNumber by remember {
            mutableStateOf(state.singleCar.phoneNumber)
        }
        var name by remember {
            mutableStateOf(state.singleCar.name)
        }
        var loading = remember {
            mutableStateOf(false)
        }

        if(loading.value){
            ProgressDialog {
                loading.value=false
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollInterop),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.new_car),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ImageSelectorEdit(photos = selectImages, oldImages = state.singleCar.images, onClick = {
                    if (permissionState.status != PermissionStatus.Granted) {
                        permissionState.launchPermissionRequest()
                    } else {
                        galleryLauncher.launch("image/*")
                    }
                }, onOldDelete = {itt->
                    loading.value=true
                    viewModel.removeCarImage(itt.id.toString(),Utils.getToken(context),
                        onSuccess = {
                            loading.value=false
                            message.value=R.string.success
                            toastType.value="success"
                            showToast.value=true
                            state.singleCar.images = state.singleCar.images.filter { item -> item != itt }
                        },
                        onError = {
                            loading.value=false
                            message.value=it
                            toastType.value="error"
                            showToast.value=true
                        }
                    )
                }, onDelete = {
                    selectImages = selectImages.filter { item -> item != it }
                })
            }


            item {
                Spacer(modifier = Modifier.height(16.dp))



                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.car_name),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    textStyle = inputFontStyle,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                )



                DropDown(
                    items = state.addCarDetails.brand.map { SelectItem(it.name, it.id) },
                    onItemSelected = {
                        brandId = it
                        brand = state.addCarDetails.brand.find { item -> item.id == it }
                    }, stringResource(id = R.string.brand), selectedItem = SelectItem(state.singleCar.carModel.brand.name,state.singleCar.carModel.brandId)
                )

                brand?.models?.map { SelectItem(it.name, it.id) }?.let {
                    DropDown(
                        items = it,
                        onItemSelected = {
                            modelId = it
                        }, stringResource(id = R.string.model), selectedItem = SelectItem(state.singleCar.carModel.name,state.singleCar.modelId)
                    )
                }

                DropDown(
                    items = state.addCarDetails.option.map {
                        SelectItem(
                            if (isRu) it.name_ru else it.name_tm,
                            it.id
                        )
                    },
                    onItemSelected = {
                        optionId = it
                    }, stringResource(id = R.string.type),
                    selectedItem = SelectItem(
                        label = if(isRu) state.singleCar.carOption.name_ru else state.singleCar.carOption.name_tm,
                        value = state.singleCar.optionId
                    )
                )

                DropDown(
                    items = state.addCarDetails.engine.map {
                        SelectItem(
                            if (isRu) it.name_ru else it.name_tm,
                            it.id
                        )
                    },
                    onItemSelected = {
                        engineTypeId = it
                    }, stringResource(id = R.string.energy_type),
                    selectedItem = SelectItem(
                        label = if(isRu) state.singleCar.carEngineType.name_ru else state.singleCar.carEngineType.name_tm,
                        value = state.singleCar.engineTypeId
                    )
                )


                DropDown(
                    items = state.addCarDetails.transmition.map {
                        SelectItem(
                            if (isRu) it.name_ru else it.name_tm,
                            it.id
                        )
                    },
                    onItemSelected = {
                        transmitionId = it
                    }, stringResource(id = R.string.transmission),
                    selectedItem = SelectItem(
                        label = if(isRu) state.singleCar.carTransmition.name_ru else state.singleCar.carTransmition.name_tm,
                        value = state.singleCar.transmitionId
                    )
                )

                OutlinedTextField(
                    value = year.toString(),
                    onValueChange = {
                        year = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.year),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    textStyle = inputFontStyle,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                )


                OutlinedTextField(
                    value = power.toString(),
                    onValueChange = {
                        power = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.power),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    textStyle = inputFontStyle,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                )

                OutlinedTextField(
                    value = lastMile.toString(),
                    onValueChange = {
                        lastMile = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.lastMile),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    textStyle = inputFontStyle,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                )

                OutlinedTextField(
                    value = vinCode,
                    onValueChange = {
                        if(it.length<=17) vinCode = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.vin),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    textStyle = inputFontStyle,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                )


                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.car_phone),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onBackground
                    ),
                    textStyle = inputFontStyle,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                )

                fun uploadImages(id: String){
                    val files = selectImages.map {
                        FileUtils.getFile(context, it)
                    }
                    viewModel.addCarImage(
                        files,
                        id,
                        Utils.getToken(context),
                        onSuccess = {
                            loading.value=false
                            selectImages = emptyList()
                            navController.popBackStack()
                            message.value=R.string.success
                            toastType.value="success"
                            showToast.value=true

                        },
                        onError = {
                            loading.value=false
                            message.value=it
                            toastType.value="error"
                            showToast.value=true

                        }
                    )
                }

                ElevatedButton(
                    onClick = {
                        if(selectImages.isEmpty() && state.singleCar.images.isEmpty()){
                            message.value=R.string.select_images
                            showToast.value=true
                        } else if(brandId==0){
                            message.value=R.string.select_brand
                            showToast.value=true
                        }else if(engineTypeId==0){
                            message.value=R.string.select_engine_type
                            showToast.value=true
                        }else if(modelId==0){
                            message.value=R.string.select_model
                            showToast.value=true
                        }else if(optionId==0){
                            message.value=R.string.select_option
                            showToast.value=true
                        }else if(transmitionId==0){
                            message.value=R.string.select_transmition
                            showToast.value=true
                        }else if(name.isBlank()){
                            message.value=R.string.enter_car_name
                            showToast.value=true
                        }else if(vinCode.length<17){
                            message.value=R.string.enter_vin_code
                            showToast.value=true
                        }else if(year=="0"){
                            message.value=R.string.enter_year
                            showToast.value=true
                        }else if(lastMile=="0"){
                            message.value=R.string.enter_last_mile
                            showToast.value=true
                        }else if(power=="0.0"){
                            message.value=R.string.enter_power
                            showToast.value=true
                        } else {
                            if(phoneNumber=="+9936"){
                                phoneNumber=""
                            }
                            loading.value=true
                            viewModel.updateCar(
                                id=id,
                                body = AddCarBody(
                                    power.toDouble(),
                                    engineTypeId,
                                    lastMile.toInt(),
                                    modelId,
                                    name,
                                    optionId,
                                    phoneNumber,
                                    "ACTIVE",
                                    transmitionId,
                                    Utils.getSharedPreference(context,"user_id")?.toInt()?:0,
                                    vinCode,
                                    year.toInt()
                                ),
                                token = Utils.getToken(context),
                                onSuccess = {
                                    if(selectImages.isNotEmpty()){
                                        uploadImages(it.id.toString())
                                    } else {
                                        loading.value=false
                                        navController.popBackStack()
                                        message.value=R.string.success
                                        toastType.value="success"
                                        showToast.value=true
                                    }
                                },
                                onError = {
                                    loading.value=false
                                    message.value=it
                                    toastType.value="error"
                                    showToast.value=true
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.edit),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }


            }


        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageSelectorEdit(photos: List<Uri>,
                      oldImages: List<Image>,
                      onClick: () -> Unit,
                      onDelete: (Uri) -> Unit,
                      onOldDelete: (Image)->Unit
) {
    val listState = rememberLazyGridState()
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp),
        maxItemsInEachRow = 3
    ) {
        repeat(oldImages.size) { photo ->
            OldPhotoItemEdit(oldImages[photo], onDelete = {
                onOldDelete(it)
            })
        }
        repeat(photos.size) { photo ->
            PhotoItemEdit(photos[photo], onDelete = {
                onDelete(it)
            })
        }
        AddPhoto {
            onClick()
        }


    }
}


@Composable
fun OldPhotoItemEdit(photo: Image, onDelete: (Image) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .height(110.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.BottomStart)
                .height(100.dp), elevation = CardDefaults.cardElevation(0.dp)
        ) {
            ImageLoader(url = ImageUrl().getFullUrl(photo.url,ImageUrl().CAR))
        }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                .padding(3.dp)
                .align(Alignment.TopEnd)
                .size(24.dp)
        ) {
            IconButton(onClick = { onDelete(photo) }) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun PhotoItemEdit(photo: Uri, onDelete: (Uri) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .height(110.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.BottomStart)
                .height(100.dp), elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(photo),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                .padding(3.dp)
                .align(Alignment.TopEnd)
                .size(24.dp)
        ) {
            IconButton(onClick = { onDelete(photo) }) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

