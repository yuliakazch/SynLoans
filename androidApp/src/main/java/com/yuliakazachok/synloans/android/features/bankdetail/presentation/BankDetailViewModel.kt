package com.yuliakazachok.synloans.android.features.bankdetail.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.features.bankdetail.domain.entity.BankInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BankDetailViewModel : BaseViewModel<BankDetailAction, BankDetailState, BankDetailEffect>() {

	override fun setInitialState(): BankDetailState =
		BankDetailState(data = null, loading = true)

	override fun handleActions(action: BankDetailAction) {
		when (action) {
			is BankDetailAction.BackClicked -> {
				setEffect { BankDetailEffect.Navigation.ToBack }
			}
		}
	}

	init {
		loadBankInfo()
	}

	private fun loadBankInfo() {
		viewModelScope.launch {
			delay(2_000) // TODO delete

			try {
				// TODO get bank info use case
				setState { copy(data = getBankInfoMock(), loading = false) }
			} catch (e: Throwable) {
				setState { copy(data = null, loading = false) }
			}
		}
	}

	private fun getBankInfoMock(): BankInfo =
		BankInfo(
			id = 31,
			fullName = "Публичное акционерное общество “Банк Северный”",
			shortName = "ПАО “Банк Северный”",
			tin = "7708004761",
			iec = "43653462219",
			legalAddress = "101000, Москва, Бульвар Сретенский, 11",
			actualAddress = "117420, Москва, Наметкина, 16",
			email = "bank@bankmail.ru",
		)
}