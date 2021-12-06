package com.yuliakazachok.synloans.android.features.paymentschedule.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleAction
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleEffect
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleState
import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun PaymentScheduleScreen(
	state: PaymentScheduleState,
	effectFlow: Flow<PaymentScheduleEffect>?,
	onActionSent: (action: PaymentScheduleAction) -> Unit,
	onNavigationRequested: (navigationEffect: PaymentScheduleEffect.Navigation) -> Unit
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
				title = stringResource(R.string.request_payment_schedule),
				onIconClicked = { onActionSent(PaymentScheduleAction.BackClicked) },
			)
		}
	) {
		when {
			state.loading -> LoadingView()

			state.data == null -> ErrorView()

			else -> PaymentScheduleView(state.data)
		}
	}
}

@Composable
fun PaymentScheduleView(
	data: List<Payment>
) {
	val listState = rememberLazyListState()
	val textSumUnit = stringResource(R.string.requests_sum_million)
	val textPaid = stringResource(R.string.payment_paid)
	val textNotPaid = stringResource(R.string.payment_not_paid)

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
	) {
		data.forEach { payment ->
			item {
				TextTwoLinesView(
					textOne = if (payment.paid) {
						textPaid
					} else {
						textNotPaid
					},
					textTwo = payment.sum.toString() + textSumUnit + stringResource(R.string.request_divider) + payment.date,
				)
			}
		}
	}
}