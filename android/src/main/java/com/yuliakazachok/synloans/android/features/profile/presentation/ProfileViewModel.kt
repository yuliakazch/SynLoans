package com.yuliakazachok.synloans.android.features.profile.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.token.domain.usecase.ClearTokenUseCase
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val clearTokenUseCase: ClearTokenUseCase,
) : BaseViewModel<ProfileAction, ProfileState, ProfileEffect>() {

    override fun setInitialState(): ProfileState =
        ProfileState(profile = null, loading = false)

    override fun handleActions(action: ProfileAction) {
        when (action) {
            is ProfileAction.RequestsClicked -> {
                setEffect { ProfileEffect.Navigation.ToRequests }
            }

            is ProfileAction.EditProfileClicked -> {
                setEffect { ProfileEffect.Navigation.ToEditProfile }
            }

            is ProfileAction.LogoutClicked -> {
                clearTokenUseCase()
                setEffect { ProfileEffect.Navigation.ToLogout }
            }

            is ProfileAction.RepeatClicked -> {
                loadProfile()
            }
        }
    }

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val profile = getProfileUseCase()
                setState { copy(profile = profile, loading = false) }
            } catch (e: Throwable) {
                setState { copy(profile = null, loading = false) }
            }
        }
    }
}