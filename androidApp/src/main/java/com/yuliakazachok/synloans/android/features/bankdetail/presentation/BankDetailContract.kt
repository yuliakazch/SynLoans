package com.yuliakazachok.synloans.android.features.bankdetail.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.features.bankdetail.domain.entity.BankInfo

sealed class BankDetailAction : Action {

	object BackClicked : BankDetailAction()
}

data class BankDetailState(val data: BankInfo?, val loading: Boolean) : State

sealed class BankDetailEffect : Effect {

	sealed class Navigation : BankDetailEffect() {

		object ToBack : Navigation()
	}
}