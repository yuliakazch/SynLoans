package com.yuliakazachok.synloans.android.features.requestcreate.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.sum.Sum
import com.yuliakazachok.synloans.shared.request.domain.entity.sum.SumUnit
import com.yuliakazachok.synloans.shared.request.domain.usecase.CreateRequestUseCase
import kotlinx.coroutines.launch

class RequestCreateViewModel(
	private val createRequestUseCase: CreateRequestUseCase,
) : BaseViewModel<RequestCreateAction, RequestCreateState, RequestCreateEffect>() {

	override fun setInitialState(): RequestCreateState =
		RequestCreateState(data = CreateData(), loading = false, hasError = false)

	override fun handleActions(action: RequestCreateAction) {
		when (action) {
			is RequestCreateAction.SendRequestClicked -> {
				sendRequest()
			}

			is RequestCreateAction.BackClicked        -> {
				setEffect { RequestCreateEffect.Navigation.ToBack }
			}

			is RequestCreateAction.RepeatClicked -> {
				setState {
					copy(hasError = false)
				}
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
				createRequestUseCase(viewState.value.data.convertToCreateInfo())
				setEffect { RequestCreateEffect.Navigation.ToBack }
			} catch (e: Throwable) {
				setState { copy(loading = false, hasError = true) }
			}
		}
	}

	private fun CreateData.convertToCreateInfo(): CreateRequestInfo =
		CreateRequestInfo(Sum(value = sum.toInt(), unit = SumUnit.MILLION), maxRate.toInt(), term.toInt())
}