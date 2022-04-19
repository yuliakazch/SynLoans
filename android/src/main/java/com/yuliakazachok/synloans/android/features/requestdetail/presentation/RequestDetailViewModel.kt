package com.yuliakazachok.synloans.android.features.requestdetail.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.ScheduleType.ACTUAL
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.ScheduleType.PLANNED
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.CancelRequestUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.ExitSyndicateUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetRequestDetailUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.StartCreditUseCase
import kotlinx.coroutines.launch

class RequestDetailViewModel(
    private val getRequestDetailUseCase: GetRequestDetailUseCase,
    private val cancelRequestUseCase: CancelRequestUseCase,
    private val exitSyndicateUseCase: ExitSyndicateUseCase,
    private val startCreditUseCase: StartCreditUseCase,
    private val isCreditOrganisationUseCase: IsCreditOrganisationUseCase,
    private val requestId: Int,
) : BaseViewModel<RequestDetailAction, RequestDetailState, RequestDetailEffect>() {

    override fun setInitialState(): RequestDetailState =
        RequestDetailState(request = null, creditOrganisation = false, loading = false)

    override fun handleActions(action: RequestDetailAction) {
        when (action) {
            is RequestDetailAction.BackClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToBack }
            }

            is RequestDetailAction.RepeatClicked -> {
                loadInfoRequest()
            }

            is RequestDetailAction.CancelRequestClicked -> {
                cancelRequest()
            }

            is RequestDetailAction.PaymentScheduleClicked -> {
                val schedule = when (action.scheduleType) {
                    PLANNED -> 0
                    ACTUAL -> 1
                }
                setEffect { RequestDetailEffect.Navigation.ToPaymentSchedule(schedule, requestId) }
            }

            is RequestDetailAction.JoinSyndicateClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToJoinSyndicate(requestId) }
            }

            is RequestDetailAction.BankItemClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToBankItem(action.id) }
            }

            is RequestDetailAction.MakePaymentClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToMakePayment(requestId) }
            }

            is RequestDetailAction.ExitSyndicateClicked -> {
                exitSyndicate()
            }

            is RequestDetailAction.StartCreditClicked -> {
                startCredit()
            }
        }
    }

    init {
        setState { copy(creditOrganisation = isCreditOrganisationUseCase()) }

        loadInfoRequest()
    }

    private fun loadInfoRequest() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val detail = getRequestDetailUseCase(requestId)
                setState { copy(request = detail, loading = false) }
            } catch (e: Throwable) {
                setState { copy(request = null, loading = false) }
            }
        }
    }

    private fun cancelRequest() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                cancelRequestUseCase(requestId)
                setEffect { RequestDetailEffect.Navigation.ToBack }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setEffect { RequestDetailEffect.Error() }
            }
        }
    }

    private fun exitSyndicate() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                exitSyndicateUseCase(requestId)
                setEffect { RequestDetailEffect.Navigation.ToBack }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setEffect { RequestDetailEffect.Error() }
            }
        }
    }

    private fun startCredit() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                startCreditUseCase(requestId)
                loadInfoRequest()
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setEffect { RequestDetailEffect.Error() }
            }
        }
    }
}