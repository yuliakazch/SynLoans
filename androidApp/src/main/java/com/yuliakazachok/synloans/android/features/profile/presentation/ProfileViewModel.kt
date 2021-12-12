package com.yuliakazachok.synloans.android.features.profile.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel<ProfileAction, ProfileState, ProfileEffect>() {

    override fun setInitialState(): ProfileState =
        ProfileState(profile = null, loading = true)

    override fun handleActions(action: ProfileAction) {
        when (action) {
            is ProfileAction.RequestsClicked -> {
                setEffect { ProfileEffect.Navigation.ToRequests }
            }

            is ProfileAction.EditProfileClicked -> {
                setEffect { ProfileEffect.Navigation.ToEditProfile }
            }

            is ProfileAction.LogoutClicked -> {
                setEffect { ProfileEffect.Navigation.ToLogout }
            }
        }
    }

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = getProfileUseCase()
                setState { copy(profile = profile, loading = false) }
            } catch (e: Throwable) {
                setState { copy(profile = null, loading = false) }
            }
        }
    }
}