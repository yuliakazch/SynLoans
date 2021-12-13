package com.yuliakazachok.synloans.android.features.requestdetail.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.CancelRequestUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetRequestDetailUseCase
import kotlinx.coroutines.launch

class RequestDetailViewModel(
    private val getRequestDetailUseCase: GetRequestDetailUseCase,
    private val cancelRequestUseCase: CancelRequestUseCase,
    private val isCreditOrganisationUseCase: IsCreditOrganisationUseCase,
    private val requestId: Int,
) : BaseViewModel<RequestDetailAction, RequestDetailState, RequestDetailEffect>() {

    override fun setInitialState(): RequestDetailState =
        RequestDetailState(request = null, creditOrganisation = false, loading = true)

    init {
        setState { copy(creditOrganisation = isCreditOrganisationUseCase()) }

        loadInfoRequest()
    }

    private fun loadInfoRequest() {
        viewModelScope.launch {
            try {
                val detail = getRequestDetailUseCase(requestId)
                setState { copy(request = detail, loading = false) }
            } catch (e: Throwable) {
                setState { copy(request = null, loading = false) }
            }
        }
    }

    override fun handleActions(action: RequestDetailAction) {
        when (action) {
            is RequestDetailAction.BackClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToBack }
            }

            is RequestDetailAction.CancelRequestClicked -> {
                cancelRequest()
            }

            is RequestDetailAction.PaymentScheduleClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToPaymentSchedule }
            }

            is RequestDetailAction.JoinSyndicateClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToJoinSyndicate(requestId) }
            }

            is RequestDetailAction.BankItemClicked -> {
                setEffect { RequestDetailEffect.Navigation.ToBankItem(action.id) }
            }
        }
    }

    private fun cancelRequest() {
        viewModelScope.launch {
            try {
                setState { copy(loading = true) }
                cancelRequestUseCase(requestId)
                setEffect { RequestDetailEffect.Navigation.ToBack }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setEffect { RequestDetailEffect.Error() }
            }
        }
    }
}