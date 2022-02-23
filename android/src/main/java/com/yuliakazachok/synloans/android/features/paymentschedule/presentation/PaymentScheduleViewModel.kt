package com.yuliakazachok.synloans.android.features.paymentschedule.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetPlannedScheduleUseCase
import kotlinx.coroutines.launch

class PaymentScheduleViewModel(
	private val getPlannedScheduleUseCase: GetPlannedScheduleUseCase,
	private val requestId: Int,
) : BaseViewModel<PaymentScheduleAction, PaymentScheduleState, PaymentScheduleEffect>() {

	override fun setInitialState(): PaymentScheduleState =
		PaymentScheduleState(data = null, loading = false)

	override fun handleActions(action: PaymentScheduleAction) {
		when (action) {
			is PaymentScheduleAction.BackClicked -> {
				setEffect { PaymentScheduleEffect.Navigation.ToBack }
			}

			is PaymentScheduleAction.RepeatClicked -> {
				loadPaymentSchedule()
			}
		}
	}

	init {
		loadPaymentSchedule()
	}

	private fun loadPaymentSchedule() {
		viewModelScope.launch {
			setState { copy(loading = true) }
			try {
				val data = getPlannedScheduleUseCase(requestId)
				setState { copy(data = data, loading = false) }
			} catch (e: Throwable) {
				setState { copy(data = null, loading = false) }
			}
		}
	}
}