package com.yuliakazachok.synloans.android.features.makepayment.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.usecase.MakePaymentUseCase
import kotlinx.coroutines.launch

class MakePaymentViewModel(
    private val makePaymentUseCase: MakePaymentUseCase,
    private val requestId: Int,
) : BaseViewModel<MakePaymentAction, MakePaymentState, MakePaymentEffect>() {

    override fun setInitialState(): MakePaymentState =
        MakePaymentState(sum = "", loading = false, hasError = false)

    override fun handleActions(action: MakePaymentAction) {
        when (action) {
            is MakePaymentAction.BackClicked -> {
                setEffect { MakePaymentEffect.Navigation.ToBack }
            }

            MakePaymentAction.RepeatClicked -> {
                setState { copy(hasError = false) }
            }

            is MakePaymentAction.MakePaymentClicked -> {
                makePayment()
            }

            is MakePaymentAction.SumChanged -> {
                setState { copy(sum = action.newValue) }
            }
        }
    }

    private fun makePayment() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                makePaymentUseCase(requestId, Payment(viewState.value.sum.toLong()))
                setEffect { MakePaymentEffect.Navigation.ToBack }
            } catch (e: Throwable) {
                setState { copy(loading = false, hasError = true) }
            }
        }
    }
}