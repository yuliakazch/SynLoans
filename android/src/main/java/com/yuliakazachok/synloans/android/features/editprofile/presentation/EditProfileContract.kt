package com.yuliakazachok.synloans.android.features.editprofile.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.shared.user.domain.entity.EditProfileInfo

sealed class EditProfileAction : Action {

	object SaveClicked : EditProfileAction()

	object BackClicked : EditProfileAction()

	object RepeatClicked : EditProfileAction()

	data class ShortNameChanged(val newValue: String) : EditProfileAction()
	data class LegalAddressChanged(val newValue: String) : EditProfileAction()
	data class ActualAddressChanged(val newValue: String) : EditProfileAction()
}

data class EditProfileState(val data: EditProfileInfo?, val loading: Boolean) : State

sealed class EditProfileEffect : Effect {

	data class Error(val message: String? = null) : EditProfileEffect()

	sealed class Navigation : EditProfileEffect() {

		object ToBack : Navigation()
	}
}