package com.yuliakazachok.synloans.android.features.makepayment.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State

sealed class MakePaymentAction : Action {

    object BackClicked : MakePaymentAction()

    object RepeatClicked : MakePaymentAction()

    object MakePaymentClicked : MakePaymentAction()

    data class SumChanged(val newValue: String) : MakePaymentAction()
}

data class MakePaymentState(val sum: String, val loading: Boolean, val hasError: Boolean) : State

sealed class MakePaymentEffect : Effect {

    sealed class Navigation : MakePaymentEffect() {

        object ToBack : Navigation()
    }
}