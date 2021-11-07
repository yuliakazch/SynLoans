package com.yuliakazachok.synloans.android.features.joinsyndicate.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JoinSyndicateViewModel : BaseViewModel<JoinSyndicateAction, JoinSyndicateState, JoinSyndicateEffect>() {

	override fun setInitialState(): JoinSyndicateState =
		JoinSyndicateState(data = JoinData(), loading = false)

	override fun handleActions(action: JoinSyndicateAction) {
		when (action) {
			is JoinSyndicateAction.JoinClicked                  -> {
				joinSyndicate()
			}

			is JoinSyndicateAction.BackClicked                  -> {
				setEffect { JoinSyndicateEffect.Navigation.ToBack }
			}

			is JoinSyndicateAction.SumChanged                   -> {
				setState {
					copy(data = data.copy(sum = action.newValue))
				}
			}

			is JoinSyndicateAction.ApproveBankAgentCheckChanged -> {
				setState {
					copy(data = data.copy(approveBankAgent = action.newValue))
				}
			}
		}
	}

	private fun joinSyndicate() {
		viewModelScope.launch {
			setState { copy(loading = true) }
			try {
				delay(2_000) // TODO delete
				// TODO join syndicate use case and convert from JoinData to JoinSyndicateData
				setEffect { JoinSyndicateEffect.Navigation.ToBack }
			} catch (e: Throwable) {
				setState { copy(loading = false) }
				setEffect { JoinSyndicateEffect.Error() }
			}
		}
	}
}