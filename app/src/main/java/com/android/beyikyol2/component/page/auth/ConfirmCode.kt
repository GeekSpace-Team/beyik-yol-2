package com.android.beyikyol2.component.page.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_profile.data.remote.dto.SaveFcmTokenDto
import com.android.beyikyol2.feature_profile.presentation.ProfileViewModel
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.BgDark
import com.android.beyikyol2.ui.theme.Primary
import com.talhafaki.composablesweettoast.util.SweetToastUtil


@Composable
fun ConfirmCode(
    navController: NavController,
    uuid: String,
    phone: String,
    sms_phone: String,
    is_exist: Boolean,
    type: String
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context: Context = LocalContext.current
    var showToast = remember {
        mutableStateOf(false)
    }

    var message = remember {
        mutableStateOf(R.string.error)
    }

    if (showToast.value) {
        SweetToastUtil.SweetError(message = stringResource(id = message.value))
        showToast.value = false
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_white_logo),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(60.dp))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.enter_code),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(MaterialTheme.colorScheme.onBackground)) {
                    append("$phone -> ")
                }
                withStyle(style = SpanStyle(MaterialTheme.colorScheme.primary)) {
                    append(stringResource(id = R.string.send_sms))
                }
                withStyle(style = SpanStyle(MaterialTheme.colorScheme.onBackground)) {
                    append(" -> $sms_phone")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = annotatedString,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$sms_phone"))
                        intent.putExtra("sms_body", ".")
                        context.startActivity(intent)
                    }
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$sms_phone"))
                intent.putExtra("sms_body", ".")
                context.startActivity(intent)
            }, colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )) {
                Text(text = stringResource(id = R.string.send_sms), style = MaterialTheme.typography.titleSmall, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(30.dp))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    viewModel.checkCode(phone, uuid, {
                        if (it != null) {
                            if (!it.blocked) {
                                Utils.setPreference("user_id", it.id.toString(), context)
                                Utils.setPreference("fullname", it.fullname.toString(), context)
                                Utils.setPreference("username", it.username.toString(), context)
                                Utils.setPreference(
                                    "phonenumber",
                                    it.phonenumber.toString(),
                                    context
                                )
                                Utils.setPreference("dob", it.dob.toString(), context)
                                Utils.setPreference("status", it.status.toString(), context)
                                Utils.setPreference("image", it.image.toString(), context)
                                Utils.setPreference("token", it.access_token.toString(), context)
                                viewModel.saveFcmToken(
                                    SaveFcmTokenDto(
                                        "ANDROID",
                                        Utils.getSharedPreference(context, "fcm_token")
                                    ), Utils.getToken(context), {}, {})
                                if (type == "home") {
                                    navController.popBackStack()
                                    navController.popBackStack()
                                } else {
                                    try {
                                        navController.navigate(Routes.MainScreen.route) {
                                            popUpTo(0) {
                                                inclusive = true
                                            }
                                        }
                                    } catch (ex: Exception) {
                                    }
                                }

                            } else {
                                showToast.value = true
                                message.value = R.string.blocked
                            }
                        } else {
                            showToast.value = true
                            message.value = R.string.error
                        }
                    }, {
                        showToast.value = true
                        message.value = it
                    })
                }
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}