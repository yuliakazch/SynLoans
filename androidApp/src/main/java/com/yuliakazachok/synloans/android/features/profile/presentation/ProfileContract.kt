package com.yuliakazachok.synloans.android.features.profile.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile

sealed class ProfileAction : Action {

	object RequestsClicked : ProfileAction()

	object EditProfileClicked : ProfileAction()

	object LogoutClicked : ProfileAction()
}

data class ProfileState(val profile: Profile?, val loading: Boolean = false) : State

sealed class ProfileEffect : Effect {

	sealed class Navigation : ProfileEffect() {

		object ToRequests : Navigation()

		object ToEditProfile : Navigation()

		object ToLogout : Navigation()
	}
}