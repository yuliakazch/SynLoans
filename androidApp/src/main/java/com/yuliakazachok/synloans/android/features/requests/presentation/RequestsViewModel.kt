package com.yuliakazachok.synloans.android.features.requests.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequest
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BorrowRequest
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestsViewModel : BaseViewModel<RequestsAction, RequestsState, RequestsEffect>() {

	// TODO add parameter creditOrganisation

	override fun setInitialState(): RequestsState =
		RequestsState(borrowRequests = null, bankRequests = null, creditOrganisation = false, loading = true)

	override fun handleActions(action: RequestsAction) {
		when (action) {
			is RequestsAction.CreateRequestClicked -> {
				setEffect { RequestsEffect.Navigation.ToCreateRequest }
			}

			is RequestsAction.RequestClicked       -> {
				setEffect { RequestsEffect.Navigation.ToRequest }
			}

			is RequestsAction.ProfileClicked       -> {
				setEffect { RequestsEffect.Navigation.ToProfile }
			}
		}
	}

	init {
		if (viewState.value.creditOrganisation) {
			loadBankRequests()
		} else {
			loadBorrowerRequests()
		}
	}

	private fun loadBankRequests() {
		viewModelScope.launch {
			delay(2_000) // TODO delete

			try {
				// TODO get bank requests use case
				setState { copy(bankRequests = getBankRequestsMock(), loading = false) }
			} catch (e: Throwable) {
				setState { copy(borrowRequests = null, loading = false) }
			}
		}
	}

	private fun loadBorrowerRequests() {
		viewModelScope.launch {
			delay(2_000) // TODO delete

			try {
				// TODO get borrower requests use case
				setState { copy(borrowRequests = getBorrowerRequestsMock(), loading = false) }
			} catch (e: Throwable) {
				setState { copy(borrowRequests = null, loading = false) }
			}
		}
	}

	private fun getBankRequestsMock(): BankRequests = BankRequests(
		own = listOf(
			BankRequest(
				id = 102,
				name = "ПАО “Компания Первая”",
				sum = 12,
			),
		),
		other = listOf(
			BankRequest(
				id = 101,
				name = "ПАО “Компания Первая”",
				sum = 7,
			),
			BankRequest(
				id = 56,
				name = "ПАО “Компания Вторая”",
				sum = 2,
			),
			BankRequest(
				id = 11,
				name = "ПАО “Компания Третья”",
				sum = 7,
			),
		),
	)

	private fun getBorrowerRequestsMock(): List<BorrowRequest> = listOf(
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