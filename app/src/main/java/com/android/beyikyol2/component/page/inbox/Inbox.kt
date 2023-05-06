package com.android.beyikyol2.component.page.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.beyikyol2.R
import com.android.beyikyol2.component.common.Loading
import com.android.beyikyol2.component.common.StateMessage
import com.android.beyikyol2.component.page.car.CarItem
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDtoItem
import com.android.beyikyol2.feature_other.presentation.GetHomeViewModel
import com.android.beyikyol2.router.Routes

@Composable
fun Inbox(navController: NavController) {
    val viewModel: GetHomeViewModel = hiltViewModel()
    val state = viewModel.state.value
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.getUserInbox(Utils.getToken(context))
    }
    val listState = rememberLazyListState()

    if (state.userInboxState == null) {
        Loading()
    } else {

        Box(modifier = Modifier.fillMaxSize()) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.notifications),
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
                if (state.userInboxState.size <= 0) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StateMessage(
                            message = R.string.no_data,
                            actionText = R.string.refresh,
                            lottieFile = R.raw.no_data_found
                        ) {
                            viewModel.getUserInbox(Utils.getToken(context))
                        }
                    }
                } else {
                    LazyColumn(state = listState) {
                        items(state.userInboxState.size) {
                            InboxItem(inbox = state.userInboxState[it])
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun InboxItem(inbox: GetUserInboxDtoItem?) {
    val viewModel: GetHomeViewModel = hiltViewModel()
    val context = LocalContext.current
    if (inbox != null) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .alpha(if (inbox.isRead) 0.8f else 1f)
            ) {
                Text(
                    text = if (Utils.getLanguage(context) == Locales.RU) inbox.titleRu else inbox.titleTm,
                    color = if (inbox.isRead) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground,
                    style = if (inbox.isRead) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    viewModel.deleteInbox(
                        Utils.getToken(context),
                        inbox.id.toString(),
                        onSuccess = {
                            viewModel.getUserInbox(Utils.getToken(context))
                        },
                        onError = {

                        })
                }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.2.dp)
                    .background(MaterialTheme.colorScheme.onSecondary)
            )
            Text(
                text = if (Utils.getLanguage(context) == Locales.RU) inbox.messageRu else inbox.messageTm,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.2.dp)
                    .background(MaterialTheme.colorScheme.onSecondary)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_date_range_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = inbox.createdAt.split("T")[0],
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}