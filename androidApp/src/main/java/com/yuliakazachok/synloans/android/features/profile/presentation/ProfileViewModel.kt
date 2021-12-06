package com.yuliakazachok.synloans.android.features.profile.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel<ProfileAction, ProfileState, ProfileEffect>() {

	override fun setInitialState(): ProfileState =
		ProfileState(profile = null, loading = true)

	override fun handleActions(action: ProfileAction) {
		when (action) {
			is ProfileAction.RequestsClicked    -> {
				setEffect { ProfileEffect.Navigation.ToRequests }
			}

			is ProfileAction.EditProfileClicked -> {
				setEffect { ProfileEffect.Navigation.ToEditProfile }
			}

			is ProfileAction.LogoutClicked      -> {
				setEffect { ProfileEffect.Navigation.ToLogout }
			}
		}
	}

	init {
		loadProfile()
	}

	private fun loadProfile() {
		viewModelScope.launch {
			delay(2_000) // TODO delete

			try {
				// TODO get profile use case
				setState { copy(profile = getProfileMock(), loading = false) }
			} catch (e: Throwable) {
				setState { copy(profile = null, loading = false) }
			}
		}
	}

	private fun getProfileMock(): Profile =
		Profile(
			fullName = "Публичное акционерное общество “Компания”",
			shortName = "ПАО “Компания”",
			tin = "7708004761",
			iec = "43653462219",
			legalAddress = "101000, Москва, Бульвар Сретенский, 11",
			actualAddress = "117420, Москва, Наметкина, 16",
			email = "company@companymai.ru",
			creditOrganisation = false,
		)
}