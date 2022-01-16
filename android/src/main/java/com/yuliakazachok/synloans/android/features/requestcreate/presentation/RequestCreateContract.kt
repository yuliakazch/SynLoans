package com.yuliakazachok.synloans.android.features.requestcreate.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State

sealed class RequestCreateAction : Action {

	object SendRequestClicked : RequestCreateAction()

	object BackClicked : RequestCreateAction()

	data class SumChanged(val newValue: String) : RequestCreateAction()
	data class RateChanged(val newValue: String) : RequestCreateAction()
	data class TermChanged(val newValue: String) : RequestCreateAction()
}

data class RequestCreateState(val data: CreateData, val loading: Boolean) : State

sealed class RequestCreateEffect : Effect {

	data class Error(val message: String? = null) : RequestCreateEffect()

	sealed class Navigation : RequestCreateEffect() {

		object ToBack : Navigation()
	}
}