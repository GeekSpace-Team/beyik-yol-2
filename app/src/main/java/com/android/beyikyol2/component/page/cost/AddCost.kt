package com.android.beyikyol2.component.page.cost

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.ImageLoader
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.page.FuelTypes
import com.android.beyikyol2.component.page.LabelHomeCar
import com.android.beyikyol2.core.util.ImageUrl
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_cost.data.remote.dto.CreateCostBody
import com.android.beyikyol2.feature_cost.presentation.CostViewModel
import com.android.beyikyol2.ui.theme.inputFontStyle
import com.talhafaki.composablesweettoast.util.SweetToastUtil

@Composable
fun AddCost(
    navController: NavController,
    type: String,
    title: String,
    image: String,
    carId: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.add_cost),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            Box(modifier = Modifier.height(170.dp)) {
                ImageLoader(url = ImageUrl().getFullUrl(image, ImageUrl().CAR))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .align(
                            Alignment.BottomStart
                        )
                        .padding(16.dp)
                )
            }


        }

       if(type==CostType.REPAIR){
           Repair(
               try {
                   carId.toInt()
               } catch (ex: Exception) {
                   0
               }
           )
       }

        if(type==CostType.FUEL){
            Fuel(
                try {
                    carId.toInt()
                } catch (ex: Exception) {
                    0
                }
            )
        }

        if(type==CostType.CHANGE){
            Change(
                try {
                    carId.toInt()
                } catch (ex: Exception) {
                    0
                }
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Repair(id: Int) {
    val context = LocalContext.current
    val viewModel: CostViewModel = hiltViewModel()
    val state = viewModel.state.value
    var desc by remember {
        mutableStateOf("")
    }

    var km by remember {
        mutableStateOf("0")
    }
    var price by remember {
        mutableStateOf("0")
    }

    val isRu = Utils.getLanguage(context) == Locales.RU


    var showToast = remember {
        mutableStateOf(false)
    }

    var loading = remember {
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = desc,
                onValueChange = {
                    desc = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                label = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.about_repairs),
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
                    imeAction = ImeAction.Default,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = km,
                onValueChange = {
                    km = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                label = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.enter_km),
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
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                label = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.enter_price),
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
        }

        ElevatedButton(
            onClick = {
                if (desc.isBlank()) {
                    showToast.value = true
                    message.value = R.string.enter_desc
                } else if (km.isBlank() || km == "0") {
                    showToast.value = true
                    message.value = R.string.enter_km
                } else {
                    loading.value=true
                    var kmD: Double = try {
                        km.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    var priceD: Double = try {
                        price.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    var body = CreateCostBody(
                        carId = id,
                        costType = CostType.CostType.REPAIR,
                        description = desc,
                        mile = kmD,
                        nextMile = 0.0,
                        price = priceD,
                        reminder = false,
                        typeIds = emptyList(),
                        volume = 0.0
                    )
                    viewModel.createCost(
                        Utils.getToken(context),
                        body, onSuccess = {
                            desc = ""
                            km = "0"
                            price = "0"
                            loading.value = false
                            message.value = R.string.success
                            toastType.value = "success"
                            showToast.value = true
                        }, onError = {
                            loading.value = false
                            message.value = it
                            toastType.value = "error"
                            showToast.value = true
                        })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if(loading.value){
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = stringResource(id = R.string.save_changes),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Fuel(id: Int) {
    val context = LocalContext.current
    val viewModel: CostViewModel = hiltViewModel()
    val state = viewModel.state.value
    var volume by remember {
        mutableStateOf("0")
    }

    var km by remember {
        mutableStateOf("0")
    }
    var price by remember {
        mutableStateOf("0")
    }

    val isRu = Utils.getLanguage(context) == Locales.RU


    var showToast = remember {
        mutableStateOf(false)
    }

    var loading = remember {
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = volume,
                onValueChange = {
                    volume = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                label = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.volume),
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
                    imeAction = ImeAction.Default,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = km,
                onValueChange = {
                    km = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                label = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.enter_km),
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
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                label = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.enter_price),
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

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                LabelHomeCar(
                    title = R.string.fuel_80,
                    value = Utils.getSharedPreference(context,FuelTypes.FUEL_80).plus(" TMT"))
                LabelHomeCar(
                    title = R.string.fuel_92,
                    value = Utils.getSharedPreference(context,FuelTypes.FUEL_92).plus(" TMT"))
                LabelHomeCar(
                    title = R.string.fuel_95,
                    value = Utils.getSharedPreference(context,FuelTypes.FUEL_95).plus(" TMT"))
            }
        }



        ElevatedButton(
            onClick = {
                if (volume.isBlank() || volume == "0") {
                    showToast.value = true
                    message.value = R.string.enter_volume
                } else if (km.isBlank() || km == "0") {
                    showToast.value = true
                    message.value = R.string.enter_km
                } else {
                    loading.value=true
                    var volD: Double = try {
                        volume.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    var kmD: Double = try {
                        km.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    var priceD: Double = try {
                        price.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    var body = CreateCostBody(
                        carId = id,
                        costType = CostType.CostType.FUEL,
                        description = "",
                        mile = kmD,
                        nextMile = 0.0,
                        price = priceD,
                        reminder = false,
                        typeIds = emptyList(),
                        volume = volD
                    )
                    viewModel.createCost(
                        Utils.getToken(context),
                        body, onSuccess = {
                            volume = "0"
                            km = "0"
                            price = "0"
                            loading.value = false
                            message.value = R.string.success
                            toastType.value = "success"
                            showToast.value = true
                        }, onError = {
                            loading.value = false
                            message.value = it
                            toastType.value = "error"
                            showToast.value = true
                        })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if(loading.value){
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = stringResource(id = R.string.save_changes),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Change(id: Int) {
    val context = LocalContext.current
    val viewModel: CostViewModel = hiltViewModel()
    val state = viewModel.state.value
    var nextChangeKm by remember {
        mutableStateOf("0")
    }

    var km by remember {
        mutableStateOf("0")
    }
    var price by remember {
        mutableStateOf("0")
    }

    val isRu = Utils.getLanguage(context) == Locales.RU


    var showToast = remember {
        mutableStateOf(false)
    }

    var loading = remember {
        mutableStateOf(false)
    }

    var reminder = remember {
        mutableStateOf(false)
    }

    var message = remember {
        mutableStateOf(R.string.error)
    }

    var toastType = remember {
        mutableStateOf("error")
    }
    
    LaunchedEffect(true){
        viewModel.getChangeTypes()
    }


    if (showToast.value) {
        if (toastType.value == "error") {
            SweetToastUtil.SweetError(message = stringResource(id = message.value))
        } else {
            SweetToastUtil.SweetSuccess(message = stringResource(id = message.value))
        }
        showToast.value = false
    }
    
    var selectedTypes by remember {
        mutableStateOf<List<Int>?>(emptyList())
    }

    if(state.changeTypes==null){
        Loading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Spacer(modifier = Modifier.height(32.dp))
                repeat(state.changeTypes.size){ it ->
                    var item = state.changeTypes[it]
                    var selected by remember {
                        mutableStateOf(selectedTypes?.contains(item.id)?:false)
                    }
                    fun check(){
                        if(!selectedTypes?.contains(item.id)!!){
                            selectedTypes=selectedTypes?.plus(item.id)
                            selected=true
                        } else {
                            selected=false
                            selectedTypes=selectedTypes?.minusElement(item.id)
                        }
                        Log.e("Selected",selectedTypes?.size.toString())
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Checkbox(checked = selected, onCheckedChange = {
                            check()
                        })
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if(isRu)item.name_ru else item.name_tm,
                            style = MaterialTheme.typography.titleSmall,
                            color=MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clickable{
                                check()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = km,
                    onValueChange = {
                        km = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.enter_km),
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
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = nextChangeKm,
                    onValueChange = {
                        nextChangeKm = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.next_change_km),
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
                        imeAction = ImeAction.Default,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    label = {
                        androidx.compose.material3.Text(
                            text = stringResource(id = R.string.enter_price),
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
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = reminder.value, onCheckedChange = {
                        reminder.value=!reminder.value
                    })
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.reminder),
                        style = MaterialTheme.typography.titleSmall,
                        color=MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable{
                            reminder.value=!reminder.value
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedButton(
                onClick = {
                    var nextD: Double = try {
                        nextChangeKm.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    var kmD: Double = try {
                        km.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    var priceD: Double = try {
                        price.toDouble()
                    } catch (ex: java.lang.Exception) {
                        0.0
                    }
                    if (selectedTypes?.isEmpty()!!) {
                        showToast.value = true
                        message.value = R.string.select_change_types
                    }  else if (km.isBlank() || km == "0") {
                        showToast.value = true
                        message.value = R.string.enter_km
                    } else if (nextChangeKm.isBlank() || nextChangeKm == "0" || kmD>=nextD) {
                        showToast.value = true
                        message.value = R.string.enter_next_change_km
                    } else {
                        loading.value=true

                        var body = CreateCostBody(
                            carId = id,
                            costType = CostType.CostType.CHANGE,
                            description = "",
                            mile = kmD,
                            nextMile = nextD,
                            price = priceD,
                            reminder = reminder.value,
                            typeIds = selectedTypes,
                            volume = 0.0
                        )
                        viewModel.createCost(
                            Utils.getToken(context),
                            body, onSuccess = {
                                nextChangeKm = "0"
                                km = "0"
                                price = "0"
                                selectedTypes= emptyList()
                                loading.value = false
                                message.value = R.string.success
                                toastType.value = "success"
                                showToast.value = true
                            }, onError = {
                                loading.value = false
                                message.value = it
                                toastType.value = "error"
                                showToast.value = true
                            })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if(loading.value){
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(
                        text = stringResource(id = R.string.save_changes),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }

}
