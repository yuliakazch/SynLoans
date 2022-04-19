package com.yuliakazachok.synloans.android.features.requests.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBankRequestsUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBorrowRequestsUseCase
import kotlinx.coroutines.launch

class RequestsViewModel(
    private val getBankRequestsUseCase: GetBankRequestsUseCase,
    private val getBorrowRequestsUseCase: GetBorrowRequestsUseCase,
    private val isCreditOrganisationUseCase: IsCreditOrganisationUseCase,
) : BaseViewModel<RequestsAction, RequestsState, RequestsEffect>() {

    override fun setInitialState(): RequestsState =
        RequestsState(borrowRequests = null, bankRequests = null, creditOrganisation = false, loading = false)

    override fun handleActions(action: RequestsAction) {
        when (action) {
            is RequestsAction.CreateRequestClicked -> {
                setEffect { RequestsEffect.Navigation.ToCreateRequest }
            }

            is RequestsAction.RequestClicked -> {
                setEffect { RequestsEffect.Navigation.ToRequest(action.id, action.participant) }
            }

            is RequestsAction.ProfileClicked -> {
                setEffect { RequestsEffect.Navigation.ToProfile }
            }

            is RequestsAction.RepeatClicked -> {
                loadData()
            }
        }
    }

    init {
        setState { copy(creditOrganisation = isCreditOrganisationUseCase()) }

        loadData()
    }

    private fun loadData() {
        if (viewState.value.creditOrganisation) {
            loadBankRequests()
        } else {
            loadBorrowerRequests()
        }
    }

    private fun loadBankRequests() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val data = getBankRequestsUseCase()
                setState { copy(bankRequests = data, loading = false) }
            } catch (e: Throwable) {
                setState { copy(bankRequests = null, loading = false) }
            }
        }
    }

    private fun loadBorrowerRequests() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val data = getBorrowRequestsUseCase()
                setState { copy(borrowRequests = data, loading = false) }
            } catch (e: Throwable) {
                setState { copy(borrowRequests = null, loading = false) }
            }
        }
    }
}