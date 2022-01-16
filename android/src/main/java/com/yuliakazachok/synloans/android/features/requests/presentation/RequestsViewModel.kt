package com.yuliakazachok.synloans.android.features.requests.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequest
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.sum.Sum
import com.yuliakazachok.synloans.shared.request.domain.entity.sum.SumUnit
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBorrowRequestsUseCase
import kotlinx.coroutines.launch

class RequestsViewModel(
    private val getBorrowRequestsUseCase: GetBorrowRequestsUseCase,
    private val isCreditOrganisationUseCase: IsCreditOrganisationUseCase,
) : BaseViewModel<RequestsAction, RequestsState, RequestsEffect>() {

    override fun setInitialState(): RequestsState =
        RequestsState(borrowRequests = null, bankRequests = null, creditOrganisation = false, loading = true)

    override fun handleActions(action: RequestsAction) {
        when (action) {
            is RequestsAction.CreateRequestClicked -> {
                setEffect { RequestsEffect.Navigation.ToCreateRequest }
            }

            is RequestsAction.RequestClicked -> {
                setEffect { RequestsEffect.Navigation.ToRequest(action.id) }
            }

            is RequestsAction.ProfileClicked -> {
                setEffect { RequestsEffect.Navigation.ToProfile }
            }
        }
    }

    init {
        setState { copy(creditOrganisation = isCreditOrganisationUseCase()) }

        if (viewState.value.creditOrganisation) {
            loadBankRequests()
        } else {
            loadBorrowerRequests()
        }
    }

    private fun loadBankRequests() {
        viewModelScope.launch {
            try {
                // TODO get bank requests use case
                setState { copy(bankRequests = getBankRequestsMock(), loading = false) }
            } catch (e: Throwable) {
                setState { copy(bankRequests = null, loading = false) }
            }
        }
    }

    private fun loadBorrowerRequests() {
        viewModelScope.launch {
            try {
                val data = getBorrowRequestsUseCase()
                setState { copy(borrowRequests = data, loading = false) }
            } catch (e: Throwable) {
                setState { copy(borrowRequests = null, loading = false) }
            }
        }
    }

    private fun getBankRequestsMock(): BankRequests = BankRequests(
        own = listOf(
            BankRequest(
                id = 4,
                name = "ПАО “Компания Первая”",
                sum = Sum(12, SumUnit.MILLION),
            ),
        ),
        other = listOf(
            BankRequest(
                id = 101,
                name = "ПАО “Компания Первая”",
                sum = Sum(7, SumUnit.MILLION),
            ),
            BankRequest(
                id = 56,
                name = "ПАО “Компания Вторая”",
                sum = Sum(2, SumUnit.MILLION),
            ),
            BankRequest(
                id = 11,
                name = "ПАО “Компания Третья”",
                sum = Sum(7, SumUnit.MILLION),
            ),
        ),
    )
}