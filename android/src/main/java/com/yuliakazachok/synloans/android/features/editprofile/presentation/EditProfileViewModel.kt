package com.yuliakazachok.synloans.android.features.editprofile.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.user.domain.entity.EditProfileInfo
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase
import com.yuliakazachok.synloans.shared.user.domain.usecase.UpdateProfileUseCase
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : BaseViewModel<EditProfileAction, EditProfileState, EditProfileEffect>() {

    private lateinit var profile: Profile

    override fun setInitialState(): EditProfileState =
        EditProfileState(data = null, loading = false)

    override fun handleActions(action: EditProfileAction) {
        when (action) {
            is EditProfileAction.SaveClicked -> {
                saveChanges()
            }

            is EditProfileAction.BackClicked -> {
                setEffect { EditProfileEffect.Navigation.ToBack }
            }

            is EditProfileAction.RepeatClicked -> {
                loadProfile()
            }

            is EditProfileAction.ShortNameChanged -> {
                setState {
                    copy(data = data?.copy(shortName = action.newValue))
                }
            }

            is EditProfileAction.LegalAddressChanged -> {
                setState {
                    copy(data = data?.copy(legalAddress = action.newValue))
                }
            }

            is EditProfileAction.ActualAddressChanged -> {
                setState {
                    copy(data = data?.copy(actualAddress = action.newValue))
                }
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
                profile = getProfileUseCase()
                setState { copy(data = profile.convertToEditInfo(), loading = false) }
            } catch (e: Throwable) {
                setState { copy(data = null, loading = false) }
            }
        }
    }

    private fun Profile.convertToEditInfo(): EditProfileInfo =
        EditProfileInfo(
            shortName = shortName,
            legalAddress = legalAddress,
            actualAddress = actualAddress,
        )

    private fun saveChanges() {
        viewState.value.data?.let { data ->
            val changeProfile = EditProfileInfo(
                shortName = if (data.shortName != profile.shortName) data.shortName else null,
                legalAddress = if (data.legalAddress != profile.legalAddress) data.legalAddress else null,
                actualAddress = if (data.actualAddress != profile.actualAddress) data.actualAddress else null,
            )

            viewModelScope.launch {
                setState { copy(loading = true) }
                try {
                    updateProfileUseCase(changeProfile)
                    setEffect { EditProfileEffect.Navigation.ToBack }
                } catch (e: Throwable) {
                    setState { copy(loading = false) }
                    setEffect { EditProfileEffect.Error() }
                }
            }
        }
    }
}