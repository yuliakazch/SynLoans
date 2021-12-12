package com.yuliakazachok.synloans.android.features.requests.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests

sealed class RequestsAction : Action {

	object CreateRequestClicked : RequestsAction()

	object RequestClicked : RequestsAction()

	object ProfileClicked : RequestsAction()
}

data class RequestsState(
	val borrowRequests: List<RequestCommon>?,
	val bankRequests: BankRequests?,
	val creditOrganisation: Boolean,
	val loading: Boolean,
) : State

sealed class RequestsEffect : Effect {

	sealed class Navigation : RequestsEffect() {

		object ToCreateRequest : Navigation()

		object ToRequest : Navigation()

		object ToProfile : Navigation()
	}
}