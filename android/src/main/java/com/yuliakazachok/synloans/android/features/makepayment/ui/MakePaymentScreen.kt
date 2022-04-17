package com.yuliakazachok.synloans.android.features.makepayment.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.yuliakazachok.synloans.android.features.makepayment.presentation.MakePaymentAction
import com.yuliakazachok.synloans.android.features.makepayment.presentation.MakePaymentEffect
import com.yuliakazachok.synloans.android.features.makepayment.presentation.MakePaymentState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun MakePaymentScreen(
    state: MakePaymentState,
    effectFlow: Flow<MakePaymentEffect>?,
    onActionSent: (action: MakePaymentAction) -> Unit,
    onNavigationRequested: (navigationEffect: MakePaymentEffect.Navigation) -> Unit
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is MakePaymentEffect.Navigation ->
                    onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        topBar = {
            TopBarBackView(
                title = stringResource(R.string.make_payment),
                onIconClicked = { onActionSent(MakePaymentAction.BackClicked) },
            )
        }
    ) {
        when {
            state.loading -> LoadingView()

            state.hasError -> ErrorView(
                onUpdateClicked = { onActionSent(MakePaymentAction.RepeatClicked) },
            )

            else -> MakePaymentView(state.sum, onActionSent)
        }
    }
}

@Composable
fun MakePaymentView(
    sum: String,
    onActionSent: (action: MakePaymentAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
    ) {
        item {
            EditNumberTextView(
                text = sum,
                label = stringResource(R.string.request_sum),
                onTextChange = { onActionSent(MakePaymentAction.SumChanged(it)) },
            )
        }
        item {
            Button(
                onClick = { onActionSent(MakePaymentAction.MakePaymentClicked) },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.send))
            }
        }
    }
}