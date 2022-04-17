package com.yuliakazachok.synloans.android.features.paymentschedule.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.ScheduleType.*
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetActualScheduleUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetPlannedScheduleUseCase
import kotlinx.coroutines.launch

class PaymentScheduleViewModel(
    private val getPlannedScheduleUseCase: GetPlannedScheduleUseCase,
    private val getActualScheduleUseCase: GetActualScheduleUseCase,
    private val scheduleType: ScheduleType,
    private val requestId: Int,
) : BaseViewModel<PaymentScheduleAction, PaymentScheduleState, PaymentScheduleEffect>() {

    override fun setInitialState(): PaymentScheduleState =
        PaymentScheduleState(planned = null, actual = null, loading = false)

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
                when (scheduleType) {
                    PLANNED -> {
                        val data = getPlannedScheduleUseCase(requestId)
                        setState { copy(planned = data, loading = false) }
                    }
                    ACTUAL -> {
                        val data = getActualScheduleUseCase(requestId)
                        setState { copy(actual = data, loading = false) }
                    }
                }
            } catch (e: Throwable) {
                setState { copy(planned = null, actual = null, loading = false) }
            }
        }
    }
}