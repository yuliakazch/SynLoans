package com.yuliakazachok.synloans.android.features.paymentschedule.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.text.TextFullScreenView
import com.yuliakazachok.synloans.android.components.text.TextThreeLinesView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleAction
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleEffect
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleState
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun PaymentScheduleScreen(
    state: PaymentScheduleState,
    effectFlow: Flow<PaymentScheduleEffect>?,
    onActionSent: (action: PaymentScheduleAction) -> Unit,
    onNavigationRequested: (navigationEffect: PaymentScheduleEffect.Navigation) -> Unit,
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is PaymentScheduleEffect.Navigation ->
                    onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        topBar = {
            TopBarBackView(
                title = stringResource(R.string.payments),
                onIconClicked = { onActionSent(PaymentScheduleAction.BackClicked) },
            )
        }
    ) {
        when {
            state.loading -> LoadingView()

            state.planned != null -> PlannedPaymentScheduleView(state.planned)

            state.actual != null -> ActualPaymentScheduleView(state.actual)

            else -> ErrorView(
                onUpdateClicked = { onActionSent(PaymentScheduleAction.RepeatClicked) },
            )
        }
    }
}

@Composable
fun PlannedPaymentScheduleView(
    data: List<PaymentInfo>,
) {
    val textSumUnit = stringResource(R.string.requests_sum)
    val textDatePayment = stringResource(R.string.date_payment)
    val textPrincipal = stringResource(R.string.principal)
    val textPercents = stringResource(R.string.percents)

    LazyColumn(
        modifier = Modifier.padding(top = 12.dp),
    ) {
        data.forEach { payment ->
            item {
                TextThreeLinesView(
                    textOne = textDatePayment + payment.date,
                    textTwo = textPrincipal + payment.principal.toString() + textSumUnit,
                    textThree = textPercents + payment.percent + textSumUnit,
                )
            }
        }
    }
}

@Composable
fun ActualPaymentScheduleView(
    data: List<Payment>,
) {
    val textSumUnit = stringResource(R.string.requests_sum)
    val textDatePayment = stringResource(R.string.date_payment)
    val textSum = stringResource(R.string.sum)

    if (data.isEmpty()) {
        TextFullScreenView(stringResource(R.string.no_payments))
    } else {
        LazyColumn(
            modifier = Modifier.padding(top = 12.dp),
        ) {
            data.forEach { payment ->
                item {
                    TextTwoLinesView(
                        textOne = textDatePayment + payment.date,
                        textTwo = textSum + payment.payment.toString() + textSumUnit,
                    )
                }
            }
        }
    }
}