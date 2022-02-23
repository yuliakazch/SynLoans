package com.yuliakazachok.synloans.android.features.joinsyndicate.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State

sealed class JoinSyndicateAction : Action {

	object JoinClicked : JoinSyndicateAction()

	object BackClicked : JoinSyndicateAction()

	object RepeatClicked : JoinSyndicateAction()

	data class SumChanged(val newValue: String) : JoinSyndicateAction()
	data class ApproveBankAgentCheckChanged(val newValue: Boolean) : JoinSyndicateAction()
}

data class JoinSyndicateState(val data: JoinData, val loading: Boolean, val hasError: Boolean) : State

sealed class JoinSyndicateEffect : Effect {

	sealed class Navigation : JoinSyndicateEffect() {

		object ToBack : Navigation()
	}
}