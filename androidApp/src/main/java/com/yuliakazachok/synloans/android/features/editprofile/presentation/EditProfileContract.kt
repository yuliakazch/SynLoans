package com.yuliakazachok.synloans.android.features.editprofile.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State

sealed class EditProfileAction : Action {

	object SaveClicked : EditProfileAction()

	object BackClicked : EditProfileAction()

	data class FullNameChanged(val newValue: String) : EditProfileAction()
	data class ShortNameChanged(val newValue: String) : EditProfileAction()
	data class TinChanged(val newValue: String) : EditProfileAction()
	data class IecChanged(val newValue: String) : EditProfileAction()
	data class LegalAddressChanged(val newValue: String) : EditProfileAction()
	data class ActualAddressChanged(val newValue: String) : EditProfileAction()
	data class EmailChanged(val newValue: String) : EditProfileAction()
}

data class EditProfileState(val data: EditData?, val loading: Boolean) : State

sealed class EditProfileEffect : Effect {

	data class Error(val message: String? = null) : EditProfileEffect()

	sealed class Navigation : EditProfileEffect() {

		object ToBack : Navigation()
	}
}