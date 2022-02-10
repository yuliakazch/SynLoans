package com.yuliakazachok.synloans.android.features.requestdetail.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon

sealed class RequestDetailAction : Action {

    object BackClicked : RequestDetailAction()
    object CancelRequestClicked : RequestDetailAction()
    object PaymentScheduleClicked : RequestDetailAction()
    object JoinSyndicateClicked : RequestDetailAction()
    data class BankItemClicked(val id: Int) : RequestDetailAction()
}

data class RequestDetailState(
    val request: RequestCommon?,
    val creditOrganisation: Boolean,
    val loading: Boolean,
) : State

sealed class RequestDetailEffect : Effect {

    data class Error(val message: String? = null) : RequestDetailEffect()

    sealed class Navigation : RequestDetailEffect() {

        object ToBack : Navigation()

        object ToPaymentSchedule : Navigation()

        data class ToJoinSyndicate(val id: Int) : Navigation()

        data class ToBankItem(val id: Int) : Navigation()
    }
}