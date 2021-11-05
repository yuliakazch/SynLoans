package com.yuliakazachok.synloans.android.features.requestdetail.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.features.requestdetail.domain.entity.RequestInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestDetailViewModel :
	BaseViewModel<RequestDetailAction, RequestDetailState, RequestDetailEffect>() {

	override fun setInitialState(): RequestDetailState =
		RequestDetailState(request = null, loading = true)

	init {
		loadInfoRequest()
	}

	private fun loadInfoRequest() {
		viewModelScope.launch {
			try {
				delay(2_000) // TODO delete
				// TODO load info use case
				setState { copy(request = getRequestMock(), loading = false) }
			} catch (e: Throwable) {
				setEffect { RequestDetailEffect.Error() }
			}

		}
	}

	private fun getRequestMock(): RequestInfo =
		RequestInfo(
			id = 23,
			sum = 5,
			maxRate = 11,
			term = 7,
			dateIssue = null,
			dateCreate = "01.03.2021"
		)

	private fun getIssuedRequestMock(): RequestInfo =
		RequestInfo(
			id = 23,
			sum = 5,
			maxRate = 11,
			term = 7,
			dateIssue = "12.04.2021",
			dateCreate = "01.03.2021"
		)

	override fun handleActions(action: RequestDetailAction) {
		when (action) {
			is RequestDetailAction.BackClicked            -> {
				setEffect { RequestDetailEffect.Navigation.ToBack }
			}

			is RequestDetailAction.CancelRequestClicked   -> {
				cancelRequest()
			}

			is RequestDetailAction.PaymentScheduleClicked -> {
				setEffect { RequestDetailEffect.Navigation.ToPaymentSchedule }
			}

			is RequestDetailAction.JoinSyndicateClicked   -> {
				setEffect { RequestDetailEffect.Navigation.ToJoinSyndicate }
			}

			is RequestDetailAction.BankItemClicked        -> {
				setEffect { RequestDetailEffect.Navigation.ToBankItem }
			}
		}
	}

	private fun cancelRequest() {
		viewModelScope.launch {
			try {
				setState { copy(loading = true) }
				delay(2_000) // TODO delete
				// TODO cancel request use case
				setEffect { RequestDetailEffect.Navigation.ToBack }
			} catch (e: Throwable) {
				setEffect { RequestDetailEffect.Error() }
			}

		}
	}
}