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

            is EditProfileAction.FullNameChanged -> {
                setState {
                    copy(data = data?.copy(fullName = action.newValue))
                }
            }

            is EditProfileAction.ShortNameChanged -> {
                setState {
                    copy(data = data?.copy(shortName = action.newValue))
                }
            }

            is EditProfileAction.TinChanged -> {
                setState {
                    copy(data = data?.copy(inn = action.newValue))
                }
            }

            is EditProfileAction.IecChanged -> {
                setState {
                    copy(data = data?.copy(kpp = action.newValue))
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

            is EditProfileAction.EmailChanged -> {
                setState {
                    copy(data = data?.copy(email = action.newValue))
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
                val profile = getProfileUseCase()
                setState { copy(data = profile.convertToEditInfo(), loading = false) }
            } catch (e: Throwable) {
                setState { copy(data = null, loading = false) }
            }
        }
    }

    private fun Profile.convertToEditInfo(): EditProfileInfo =
        EditProfileInfo(fullName, shortName, inn, kpp, legalAddress, actualAddress, email)

    private fun saveChanges() {
        viewState.value.data?.let { data ->
            viewModelScope.launch {
                setState { copy(loading = true) }
                try {
                    updateProfileUseCase(data)
                    setEffect { EditProfileEffect.Navigation.ToBack }
                } catch (e: Throwable) {
                    setState { copy(loading = false) }
                    setEffect { EditProfileEffect.Error() }
                }
            }
        }
    }
}