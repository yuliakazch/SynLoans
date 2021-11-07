package com.yuliakazachok.synloans.android.features.paymentschedule.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.features.paymentschedule.domain.entity.Payment

sealed class PaymentScheduleAction : Action {

	object BackClicked : PaymentScheduleAction()
}

data class PaymentScheduleState(val data: List<Payment>?, val loading: Boolean) : State

sealed class PaymentScheduleEffect : Effect {

	sealed class Navigation : PaymentScheduleEffect() {

		object ToBack : Navigation()
	}
}