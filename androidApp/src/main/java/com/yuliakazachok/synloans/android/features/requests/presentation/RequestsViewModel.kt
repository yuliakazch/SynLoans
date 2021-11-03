package com.yuliakazachok.synloans.android.features.requests.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.features.requests.domain.entity.BorrowRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestsViewModel : BaseViewModel<RequestsAction, RequestsState, RequestsEffect>() {

    override fun setInitialState(): RequestsState =
        RequestsState(requests = null, loading = true)

    override fun handleActions(action: RequestsAction) {
        when (action) {
            is RequestsAction.CreateRequestClicked -> {
                setEffect { RequestsEffect.Navigation.ToCreateRequest }
            }

            is RequestsAction.RequestClicked -> {
                setEffect { RequestsEffect.Navigation.ToRequest }
            }

            is RequestsAction.ProfileClicked -> {
                setEffect { RequestsEffect.Navigation.ToProfile }
            }
        }
    }

    init {
        loadRequests()
    }

    private fun loadRequests() {
        viewModelScope.launch {
            delay(2_000) // TODO delete

            try {
                // TODO get requests use case
                setState { copy(requests = getRequestsMock(), loading = false) }
            } catch (e: Throwable) {
                setState { copy(requests = null, loading = false) }
            }
        }
    }

    private fun getRequestsMock(): List<BorrowRequest> = listOf(
        BorrowRequest(
            id = 76,
            dateIssue = null,
            dateCreate = "01.10.2021",
            sum = 12,
        ),
        BorrowRequest(
            id = 56,
            dateIssue = "15.03.2021",
            dateCreate = "21.01.2021",
            sum = 2,
        ),
        BorrowRequest(
            id = 45,
            dateIssue = "09.10.2020",
            dateCreate = "05.09.2020",
            sum = 7,
        ),
    )
}