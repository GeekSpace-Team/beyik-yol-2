package com.android.beyikyol2.component.page.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.beyikyol2.component.common.Alert
import com.android.beyikyol2.component.common.AlertProperties
import com.android.beyikyol2.R
import com.android.beyikyol2.helper.PrefixTransformation
import com.android.beyikyol2.router.Routes
import com.android.beyikyol2.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavController) {
    var name by remember {
        mutableStateOf("")
    }
    val error = remember {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(42.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.tell_about_yourself),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = BgDark
            )
            Spacer(modifier = Modifier.height(40.dp))

            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = Clear
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { },
                shape = MaterialTheme.shapes.medium.copy(CornerSize(10.dp))
            ) {
                Column() {
                    Box() {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = 240.dp)
                                .align(Alignment.Center),
                            tint = DADADA
                        )
                        Text(
                            text = stringResource(id = R.string.want_add_image),
                            style = MaterialTheme.typography.titleSmall,
                            color = BgDark,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Primary
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.add),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            androidx.compose.material3.OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    error.value=it.trim().isEmpty()
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = BgDark,
                    focusedBorderColor = Primary,
                    focusedLabelColor = Primary
                ),
                textStyle = inputFontStyle,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true
                ),
                isError = error.value,
                label = { Text(text = stringResource(id = R.string.your_name), style = inputFontStyle) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (error.value)
                        Icon(Icons.Filled.Info, "error", tint = Warning)
                    else
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            tint = Grey,
                            modifier = Modifier
                                .clickable { name = "" }
                        )
                },
            )
            if (error.value) {
                Text(
                    text = "Adyňyzy giriziň",
                    color = Warning,
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                )
            }
        }

        ElevatedButton(
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                if(name.trim().isEmpty()){
                    error.value=true
                } else {
                    error.value=false
                    navController.navigate(Routes.MainScreen.route){
                        popUpTo(0){
                            inclusive=true
                        }
                    }
                }
            }
        ) {
            Text(
                text = stringResource(id = R.string.finish),
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
