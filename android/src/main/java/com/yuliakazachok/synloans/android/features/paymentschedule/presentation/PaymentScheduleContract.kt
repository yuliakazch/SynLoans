package com.yuliakazachok.synloans.android.features.paymentschedule.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo

sealed class PaymentScheduleAction : Action {

    object BackClicked : PaymentScheduleAction()

    object RepeatClicked : PaymentScheduleAction()
}

data class PaymentScheduleState(
    val planned: List<PaymentInfo>?,
    val actual: List<Payment>?,
    val loading: Boolean
) : State

sealed class PaymentScheduleEffect : Effect {

    sealed class Navigation : PaymentScheduleEffect() {

        object ToBack : Navigation()
    }
}