package com.android.beyikyol2.component.page.car

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.FullScreenDialog
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.common.ProgressDialog
import com.android.beyikyol2.core.util.FileUtils
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.AddCarBody
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.Brand
import com.android.beyikyol2.feature_car.presentation.CarViewModel
import com.android.beyikyol2.ui.theme.Tonal
import com.android.beyikyol2.ui.theme.inputFontStyle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import java.io.File
import java.io.IOException
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddCar(navController: NavController) {
    val viewModel: CarViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context = LocalContext.current
    var brandId by remember {
        mutableStateOf(0)
    }
    var brand by remember {
        mutableStateOf<Brand?>(null)
    }
    var modelId by remember {
        mutableStateOf(0)
    }
    var optionId by remember {
        mutableStateOf(0)
    }
    var engineTypeId by remember {
        mutableStateOf(0)
    }
    var transmitionId by remember {
        mutableStateOf(0)
    }
    var year by remember {
        mutableStateOf("2000")
    }
    var lastMile by remember {
        mutableStateOf("0")
    }
    var power by remember {
        mutableStateOf("0.0")
    }
    var vinCode by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf(Utils.getSharedPreference(context, "phonenumber"))
    }
    var name by remember {
        mutableStateOf("")
    }
    var loading = remember {
        mutableStateOf(false)
    }
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
    } else {

        if (loading.value) {
            ProgressDialog {
                loading.value = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {

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



            Spacer(modifier = Modifier.height(16.dp))
            ImageSelector(photos = selectImages, onClick = {
                if (permissionState.status != PermissionStatus.Granted) {
                    permissionState.launchPermissionRequest()
                } else {
                    galleryLauncher.launch("image/*")
                }
            }) {
                selectImages = selectImages.filter { item -> item != it }
            }




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
                }, stringResource(id = R.string.brand)
            )

            brand?.models?.map { SelectItem(it.name, it.id) }?.let {
                DropDown(
                    items = it,
                    onItemSelected = {
                        modelId = it
                    }, stringResource(id = R.string.model)
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
                }, stringResource(id = R.string.type)
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
                }, stringResource(id = R.string.energy_type)
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
                }, stringResource(id = R.string.transmission)
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
                    if (it.length <= 17) vinCode = it
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

            fun done() {
                loading.value = false
                selectImages = emptyList()
                brandId = 0
                engineTypeId = 0
                modelId = 0
                optionId = 0
                transmitionId = 0
                year = "0"
                lastMile = "0"
                power = "0.0"
                vinCode = ""
                name = ""
                message.value = R.string.success
                toastType.value = "success"
                showToast.value = true
                navController.popBackStack()
            }

            fun uploadImages(id: String) {
                var files = listOf<File?>()
                if (selectImages.isEmpty()) {
                    done()
                } else {
                    files = selectImages.map {
                        FileUtils.getFile(context, it)
                    }
                    viewModel.addCarImage(
                        files,
                        id,
                        Utils.getToken(context),
                        onSuccess = {
                            done()
                        },
                        onError = {
//                            loading.value = false
//                            message.value = it
//                            toastType.value = "error"
//                            showToast.value = true
                            done()

                        }
                    )
                }


            }

            ElevatedButton(
                onClick = {
//                        if(selectImages.isEmpty()){
//                            message.value=R.string.select_images
//                            showToast.value=true
//                        } else
                    if (brandId == 0) {
                        message.value = R.string.select_brand
                        showToast.value = true
                    } else if (engineTypeId == 0) {
                        message.value = R.string.select_engine_type
                        showToast.value = true
                    } else if (modelId == 0) {
                        message.value = R.string.select_model
                        showToast.value = true
                    } else if (optionId == 0) {
                        message.value = R.string.select_option
                        showToast.value = true
                    } else if (transmitionId == 0) {
                        message.value = R.string.select_transmition
                        showToast.value = true
                    } else if (name.isBlank()) {
                        message.value = R.string.enter_car_name
                        showToast.value = true
                    } else if (vinCode.length < 17) {
                        message.value = R.string.enter_vin_code
                        showToast.value = true
                    } else if (year == "0") {
                        message.value = R.string.enter_year
                        showToast.value = true
                    } else if (lastMile == "0") {
                        message.value = R.string.enter_last_mile
                        showToast.value = true
                    } else if (power == "0.0") {
                        message.value = R.string.enter_power
                        showToast.value = true
                    } else {
                        if (phoneNumber == "+9936") {
                            phoneNumber = ""
                        }
                        loading.value = true
                        viewModel.addCar(
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
                                Utils.getSharedPreference(context, "user_id")?.toInt() ?: 0,
                                vinCode,
                                year.toInt()
                            ),
                            token = Utils.getToken(context),
                            onSuccess = {
                                uploadImages(it.id.toString())
                            },
                            onError = {
                                loading.value = false
                                message.value = it
                                toastType.value = "error"
                                showToast.value = true
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
                    text = stringResource(id = R.string.add),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

data class SelectItem(
    val label: String,
    val value: Int
)


@Throws(IOException::class)
fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
    .also {
        if (!it.exists()) {
            it.outputStream().use { cache ->
                context.assets.open(fileName).use { inputStream ->
                    inputStream.copyTo(cache)
                }
            }
        }
    }


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    items: List<SelectItem>,
    onItemSelected: (Int) -> Unit,
    label: String = "",
    selectedItem: SelectItem? = null,
    modifier: Modifier = Modifier,

    ) {
    var exp by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(selectedItem?.label ?: "") }


    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                exp = true
            }
            .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
            .border(
                0.6.dp, MaterialTheme.colorScheme.onBackground,
                RoundedCornerShape(8.dp)
            )
            .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedOption.ifEmpty { label },
            style = inputFontStyle,
            color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(
                1f
            ),
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = { exp = true }) {
            Icon(
                painter = painterResource(id = if (exp) R.drawable.baseline_arrow_drop_up_24 else R.drawable.baseline_arrow_drop_down_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

//    ExposedDropdownMenuBox(
//        expanded = exp, onExpandedChange = { exp = !exp }, modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .background(Color.Transparent)
//    ) {
//        OutlinedTextField(
//            value = selectedOption,
//            onValueChange = {
//                selectedOption = it
//            },
//            readOnly = true,
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable {
////                    exp = true
//                },
//            label = {
//                androidx.compose.material3.Text(
//                    text = label,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onBackground
//                )
//            },
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp)
//            },
//            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
//                backgroundColor = Color.Transparent,
//                textColor = MaterialTheme.colorScheme.onBackground,
//                placeholderColor = MaterialTheme.colorScheme.onBackground,
//                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
//                focusedLabelColor = MaterialTheme.colorScheme.primary,
//                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
//                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                trailingIconColor = MaterialTheme.colorScheme.onBackground,
//                disabledTextColor = MaterialTheme.colorScheme.onBackground,
//                disabledPlaceholderColor = MaterialTheme.colorScheme.onBackground
//
//            ),
//            textStyle = inputFontStyle,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Done,
//                capitalization = KeyboardCapitalization.None,
//                autoCorrect = true
//            ),
//        )
//        // filter options based on text field value (i.e. crude autocomplete)
    //        if (filterOpts.isNotEmpty()) {
//            ExposedDropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
    if (exp) {
        FullScreenDialog(onDismiss = { exp = false }, label = label) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                items.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = option.label
                            exp = false
                            onItemSelected(option.value)
                        }
                    ) {
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (selectedOption == option.label) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
//            }
//        }
//    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageSelector(photos: List<Uri>, onClick: () -> Unit, onDelete: (Uri) -> Unit) {
    val listState = rememberLazyGridState()
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp),
        maxItemsInEachRow = 3
    ) {
        repeat(photos.size) { photo ->
            PhotoItem(photos[photo], onDelete = {
                onDelete(it)
            })
        }
        AddPhoto {
            onClick()
        }


    }
}


@Composable
fun PhotoItem(photo: Uri, onDelete: (Uri) -> Unit) {
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

@Composable
fun AddPhoto(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .clickable { onClick() }
            .padding(top = 10.dp)
            .background(Tonal.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .height(100.dp)
    ) {
        IconButton(onClick = { onClick() }, modifier = Modifier.align(Alignment.Center)) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.add_photo_alternate),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}
