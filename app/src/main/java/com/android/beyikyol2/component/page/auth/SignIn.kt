package com.android.beyikyol2.component.page.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.component.common.Alert
import com.android.beyikyol2.component.common.AlertProperties
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_profile.presentation.ProfileViewModel
import com.android.beyikyol2.helper.PrefixTransformation
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.*
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetError
import kotlinx.coroutines.Job

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignIn(navController: NavController, type: String) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state = viewModel.state.value

    val openDialog = remember { mutableStateOf(false) }
    var phoneNumber by rememberSaveable() {
        mutableStateOf("")
    }
    var phoneError by remember {
        mutableStateOf(phoneNumber.length > 8)
    }
    var accepted = remember {
        mutableStateOf(false)
    }

    var showToast = remember {
        mutableStateOf(false)
    }

    var message = remember {
        mutableStateOf(R.string.error)
    }

    var job: Job = Job()


    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val context: Context = LocalContext.current
    val termsError = stringResource(id = R.string.accept_terms)
    fun next(){

        if(accepted.value && phoneNumber.length==8){
            viewModel.sendPost(
                "+993$phoneNumber",
                onError = {
                    message.value=it
                    showToast.value=true
                },
                onSuccess = {
                    Utils.setPreference("uuid",it?.uuid, context)
                    Utils.setPreference("sms_phone",it?.sms_phone, context)
                    Utils.setPreference("is_exists",it?.is_exists.toString(), context)
                    Utils.setPreference("phone_number",it?.phone.toString(), context)
                    Utils.setPreference("is_sent","1", context)
                    navController.navigate(Routes.ConfirmCode.route+"/"+it?.uuid+"/"+it?.phone+"/"+it?.sms_phone+"/"+it?.is_exists+"?type="+type)
                })
        } else {
            if(!accepted.value){
                Toast.makeText(context, termsError,Toast.LENGTH_SHORT).show()
            }
        }
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

        Column {
            Text(
                text = stringResource(R.string.hasaba_dur),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.login_desc),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    phoneError = it.length > 8
                },
                textStyle = inputFontStyle,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide()
                        next()
                    }
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                isError = phoneError,
                label = { Text(text = stringResource(id = R.string.phone_number), style = inputFontStyle) },
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    if (phoneError)
                        Icon(Icons.Filled.Info, "error", tint = Warning)
                    else
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .clickable { phoneNumber = "" }
                        )
                },
                visualTransformation = PrefixTransformation("+993")
            )
            if (phoneError) {
                Text(
                    text = stringResource(R.string.phone_error),
                    color = Warning,
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = accepted.value,
                    onCheckedChange = { accepted.value = !accepted.value },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(id = R.string.accept_terms),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.ConstantPage.route + "/PRIVACY_POLICY")
                        }
                )
            }
        }
        Alert(
            props = AlertProperties(
                openDialog = openDialog,
                onOkPressed = {
                    openDialog.value = false
                    navController.navigate(Routes.ConfirmCode.route)
                },
                onCancelPressed = { openDialog.value = false },
                title = R.string.not_user_found,
                body = R.string.want_you_create_account
            )
        )

        if(showToast.value){
            SweetError(message = stringResource(id = message.value))
            showToast.value=false
        }





        Column {
            ElevatedButton(
                onClick = {
                   next()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (state.isLoading) {
                    androidx.compose.material.CircularProgressIndicator()
                } else {
                    Text(
                        text = stringResource(id = R.string.next),
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(10.dp),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
//            OutlinedButton(
//                onClick = {
//
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = "Skip",
//                    color = BgDark,
//                    fontSize = 16.sp,
//                    modifier = Modifier.padding(10.dp),
//                    style = MaterialTheme.typography.titleSmall
//                )
//            }
        }


    }


}




