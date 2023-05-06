package com.android.beyikyol2.component.page.auth

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.ImageLoader
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.core.util.FileUtils
import com.android.beyikyol2.core.util.ImageUrl
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_profile.presentation.ProfileViewModel
import com.android.beyikyol2.ui.theme.inputFontStyle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.talhafaki.composablesweettoast.util.SweetToastUtil
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(navController: NavController) {
    val context: Context = LocalContext.current
    val viewModel: ProfileViewModel = hiltViewModel()
    val state = viewModel.state.value
    val imageLoading = remember {
        mutableStateOf(false)
    }
    val editLoading = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        viewModel.getProfile(Utils.getToken(context))
    }
    var showToast = remember {
        mutableStateOf(false)
    }

    var message = remember {
        mutableStateOf(R.string.error)
    }

    var toastType = remember {
        mutableStateOf("error")
    }

    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    }





    if (showToast.value) {
        if (toastType.value == "error") {
            SweetToastUtil.SweetError(message = stringResource(id = message.value))
        } else {
            SweetToastUtil.SweetSuccess(message = stringResource(id = message.value))
        }
        showToast.value = false
    }
    if (state.profileState == null) {
        Loading()
    } else {
        var fullname by remember {
            mutableStateOf(state.profileState.fullname)
        }

        var dob by remember {
            mutableStateOf(state.profileState.dob.split("T")[0])
        }

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        var mYear = mCalendar.get(Calendar.YEAR)
        var mMonth = mCalendar.get(Calendar.MONTH)
        var mDay = mCalendar.get(Calendar.DAY_OF_MONTH)


        // Fetching current year, month and day
        LaunchedEffect(true) {
            if (dob.isNotBlank()) {
                mYear = dob.split("-")[0].toInt()
                mMonth = dob.split("-")[1].toInt()
                mDay = dob.split("-")[2].toInt()
            }
        }

        mCalendar.time = Date()
        val mDatePickerDialog =
            DatePickerDialog(context, { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                dob = "$mYear-${mMonth + 1}-$mDayOfMonth"
            }, mYear, mMonth, mDay)

        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                val file = FileUtils.getFile(context, uri)
                if (file != null) {
                    imageLoading.value = true
                    viewModel.changeImage(file, Utils.getToken(context), {
                        imageLoading.value = false
                        message.value = R.string.success
                        showToast.value = true
                        toastType.value = "success"
                        state.profileState.image = it?.image ?: state.profileState.image
                    }, {
                        imageLoading.value = false
                        message.value = it
                        showToast.value = true
                        toastType.value = "error"
                    })
                }
            }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(355.dp)
                    ) {
                        ImageLoader(
                            url = ImageUrl().getFullUrl(
                                state.profileState.image,
                                ImageUrl().USER
                            )
                        )

                        ElevatedButton(
                            onClick = {
                                if (permissionState.status != PermissionStatus.Granted) {
                                    permissionState.launchPermissionRequest()
                                } else {
                                    galleryLauncher.launch("image/*")
                                }
                            }, colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            ), modifier = Modifier.align(Alignment.Center)
                        ) {
                            if (imageLoading.value) {
                                CircularProgressIndicator()
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.change_image),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = stringResource(id = R.string.change_photo),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.back),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 340.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp)
                    ) {

                        Spacer(modifier = Modifier.height(30.dp))

                        OutlinedTextField(
                            value = fullname,
                            onValueChange = {
                                fullname = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = stringResource(id = R.string.fullname),
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

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(value = dob, onValueChange = {
                            dob = it
                        }, modifier = Modifier
                            .fillMaxWidth(), label = {
                            Text(
                                text = stringResource(id = R.string.dob),
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
                            trailingIcon = {
                                IconButton(onClick = { mDatePickerDialog.show() }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_date_range_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }

                            }
                        )

                    }


                }
            }
            ElevatedButton(
                onClick = {
                    editLoading.value=true
                    viewModel.editProfile(
                        com.android.beyikyol2.feature_profile.data.remote.dto.EditProfile(
                            dob,
                            fullname,
                            "",
                            "",
                            "",
                            ""
                        ),
                        Utils.getToken(context),
                        {
                            editLoading.value = false
                            message.value = R.string.success
                            showToast.value = true
                            toastType.value = "success"
                            state.profileState.image = it?.image ?: state.profileState.image
                        }, {
                            editLoading.value = false
                            message.value = it
                            showToast.value = true
                            toastType.value = "error"
                        }
                    )
                },
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                if(editLoading.value){
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(
                        text = stringResource(id = R.string.save_changes),
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}
