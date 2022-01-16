package com.yuliakazachok.synloans.android.features.paymentschedule.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentScheduleViewModel : BaseViewModel<PaymentScheduleAction, PaymentScheduleState, PaymentScheduleEffect>() {

	override fun setInitialState(): PaymentScheduleState =
		PaymentScheduleState(data = null, loading = true)

	override fun handleActions(action: PaymentScheduleAction) {
		when (action) {
			is PaymentScheduleAction.BackClicked -> {
				setEffect { PaymentScheduleEffect.Navigation.ToBack }
			}
		}
	}

	init {
		loadPaymentSchedule()
	}

	private fun loadPaymentSchedule() {
		viewModelScope.launch {
			delay(2_000) // TODO delete

			try {
				// TODO get payment schedule use case
				setState { copy(data = getPaymentScheduleMock(), loading = false) }
			} catch (e: Throwable) {
				setState { copy(data = null, loading = false) }
			}
		}
	}

	private fun getPaymentScheduleMock(): List<Payment> = listOf(
		Payment(
			sum = 200,
			date = "11.03.2021",
			paid = true,
		),
		Payment(
			sum = 200,
			date = "11.04.2021",
			paid = true,
		),
		Payment(
			sum = 200,
			date = "11.05.2021",
			paid = true,
		),
		Payment(
			sum = 200,
			date = "11.06.2021",
			paid = true,
		),
		Payment(
			sum = 200,
			date = "11.07.2021",
			paid = false,
		),
		Payment(
			sum = 200,
			date = "11.08.2021",
			paid = false,
		),
	)
}