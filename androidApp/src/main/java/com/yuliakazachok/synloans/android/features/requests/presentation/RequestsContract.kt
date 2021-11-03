package com.yuliakazachok.synloans.android.features.requests.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.features.requests.domain.entity.BorrowRequest

sealed class RequestsAction : Action {

    object CreateRequestClicked : RequestsAction()

    object RequestClicked : RequestsAction()

    object ProfileClicked : RequestsAction()
}

data class RequestsState(val requests: List<BorrowRequest>?, val loading: Boolean) : State

sealed class RequestsEffect : Effect {

    sealed class Navigation : RequestsEffect() {

        object ToCreateRequest : Navigation()

        object ToRequest : Navigation()

        object ToProfile : Navigation()
    }
}