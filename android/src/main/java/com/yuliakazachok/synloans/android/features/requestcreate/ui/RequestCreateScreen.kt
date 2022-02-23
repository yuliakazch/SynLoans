package com.yuliakazachok.synloans.android.features.requestcreate.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.textfield.EditNumberTextView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.CreateData
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.RequestCreateAction
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.RequestCreateEffect
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.RequestCreateState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun RequestCreateScreen(
    state: RequestCreateState,
    effectFlow: Flow<RequestCreateEffect>?,
    onActionSent: (action: RequestCreateAction) -> Unit,
    onNavigationRequested: (navigationEffect: RequestCreateEffect.Navigation) -> Unit
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is RequestCreateEffect.Navigation ->
                    onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        topBar = {
            TopBarBackView(
                title = stringResource(R.string.request_create_title),
                onIconClicked = { onActionSent(RequestCreateAction.BackClicked) },
            )
        }
    ) {
        when {
            state.loading -> LoadingView()

            state.hasError -> ErrorView(
                onUpdateClicked = { onActionSent(RequestCreateAction.RepeatClicked) },
            )

            else -> RequestCreateView(state.data, onActionSent)
        }
    }
}

@Composable
fun RequestCreateView(
    data: CreateData,
    onActionSent: (action: RequestCreateAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
    ) {
        item {
            EditNumberTextView(
                text = data.sum,
                label = stringResource(R.string.request_sum),
                onTextChange = { onActionSent(RequestCreateAction.SumChanged(it)) },
            )
        }
        item {
            EditNumberTextView(
                text = data.maxRate,
                label = stringResource(R.string.request_max_rate),
                onTextChange = { onActionSent(RequestCreateAction.RateChanged(it)) },
            )
        }
        item {
            EditNumberTextView(
                text = data.term,
                label = stringResource(R.string.request_term),
                onTextChange = { onActionSent(RequestCreateAction.TermChanged(it)) },
            )
        }
        item {
            Button(
                onClick = { onActionSent(RequestCreateAction.SendRequestClicked) },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.request_create_send))
            }
        }
    }
}