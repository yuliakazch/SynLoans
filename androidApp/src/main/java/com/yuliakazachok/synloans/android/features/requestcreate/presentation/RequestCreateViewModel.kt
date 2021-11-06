package com.yuliakazachok.synloans.android.features.requestcreate.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestCreateViewModel : BaseViewModel<RequestCreateAction, RequestCreateState, RequestCreateEffect>() {

	override fun setInitialState(): RequestCreateState =
		RequestCreateState(data = CreateData(), loading = false)

	override fun handleActions(action: RequestCreateAction) {
		when (action) {
			is RequestCreateAction.SendRequestClicked -> {
				sendRequest()
			}

			is RequestCreateAction.BackClicked        -> {
				setEffect { RequestCreateEffect.Navigation.ToBack }
			}

			is RequestCreateAction.SumChanged         -> {
				setState {
					copy(data = data.copy(sum = action.newValue))
				}
			}

			is RequestCreateAction.RateChanged        -> {
				setState {
					copy(data = data.copy(maxRate = action.newValue))
				}
			}

			is RequestCreateAction.TermChanged        -> {
				setState {
					copy(data = data.copy(term = action.newValue))
				}
			}
		}
	}

	private fun sendRequest() {
		viewModelScope.launch {
			setState { copy(loading = true) }
			try {
				delay(2_000) // TODO delete
				// TODO send request use case and convert from CreateData to RequestCreateData
				setEffect { RequestCreateEffect.Navigation.ToBack }
			} catch (e: Throwable) {
				setState { copy(loading = false) }
				setEffect { RequestCreateEffect.Error() }
			}
		}
	}
}