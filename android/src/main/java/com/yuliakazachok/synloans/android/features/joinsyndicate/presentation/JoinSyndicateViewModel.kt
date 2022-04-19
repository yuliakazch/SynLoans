package com.yuliakazachok.synloans.android.features.joinsyndicate.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.usecase.JoinSyndicateUseCase
import kotlinx.coroutines.launch

class JoinSyndicateViewModel(
    private val joinSyndicateUseCase: JoinSyndicateUseCase,
    private val requestId: Int,
) : BaseViewModel<JoinSyndicateAction, JoinSyndicateState, JoinSyndicateEffect>() {

    override fun setInitialState(): JoinSyndicateState =
        JoinSyndicateState(data = JoinData(), loading = false, hasError = false)

    override fun handleActions(action: JoinSyndicateAction) {
        when (action) {
            is JoinSyndicateAction.JoinClicked -> {
                joinSyndicate()
            }

            is JoinSyndicateAction.BackClicked -> {
                setEffect { JoinSyndicateEffect.Navigation.ToBack }
            }

            is JoinSyndicateAction.RepeatClicked -> {
                setState {
                    copy(hasError = false)
                }
            }

            is JoinSyndicateAction.SumChanged -> {
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
                joinSyndicateUseCase(viewState.value.data.convertToJoinInfo())
                setEffect { JoinSyndicateEffect.Navigation.ToBackWithUpdate(requestId, participant = true) }
            } catch (e: Throwable) {
                setState { copy(loading = false, hasError = true) }
            }
        }
    }

    private fun JoinData.convertToJoinInfo(): JoinSyndicateInfo =
        JoinSyndicateInfo(
            requestId = requestId,
            sum = sum.toLong(),
            approveBankAgent = approveBankAgent,
        )
}