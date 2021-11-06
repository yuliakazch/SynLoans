package com.yuliakazachok.synloans.android.features.requestdetail.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.features.requestdetail.domain.entity.Bank
import com.yuliakazachok.synloans.features.requestdetail.domain.entity.Borrower
import com.yuliakazachok.synloans.features.requestdetail.domain.entity.RequestCommon
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

	private fun getRequestMock(): RequestCommon =
		RequestCommon(
			info = RequestInfo(
				id = 23,
				sum = 5,
				maxRate = 11,
				term = 8,
				dateIssue = null,
				dateCreate = "01.03.2021"
			),
			banks = listOf(
				Bank(
					id = 1,
					name = "Банк Западный",
					sum = 1,
					approveBankAgent = true,
				),
				Bank(
					id = 6,
					name = "Банк Северный",
					sum = 4,
					approveBankAgent = true,
				),
				Bank(
					id = 3,
					name = "Банк Южный",
					sum = 2,
					approveBankAgent = false,
				),
			),
			borrower = Borrower(
				id = 43,
				fullName = "Публичное акционерное общество “Компания”",
				shortName = "ПАО “Компания”",
				tin = "7708004761",
				iec = "43653462219",
				legalAddress = "101000, Москва, Бульвар Сретенский, 11",
				actualAddress = "117420, Москва, Наметкина, 16",
				email = "company@companymai.ru",
			),
		)

	private fun getIssuedRequestMock(): RequestCommon =
		RequestCommon(
			info = RequestInfo(
				id = 23,
				sum = 5,
				maxRate = 11,
				term = 8,
				dateIssue = "12.04.2021",
				dateCreate = "01.03.2021"
			),
			banks = listOf(
				Bank(
					id = 1,
					name = "Банк Западный",
					sum = 1,
					approveBankAgent = true,
				),
				Bank(
					id = 6,
					name = "Банк Северный",
					sum = 4,
					approveBankAgent = true,
				),
				Bank(
					id = 3,
					name = "Банк Южный",
					sum = 2,
					approveBankAgent = false,
				),
			),
			borrower = Borrower(
				id = 43,
				fullName = "Публичное акционерное общество “Компания”",
				shortName = "ПАО “Компания”",
				tin = "7708004761",
				iec = "43653462219",
				legalAddress = "101000, Москва, Бульвар Сретенский, 11",
				actualAddress = "117420, Москва, Наметкина, 16",
				email = "company@companymai.ru",
			),
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