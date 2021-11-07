package com.yuliakazachok.synloans.android.features.editprofile.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfileViewModel : BaseViewModel<EditProfileAction, EditProfileState, EditProfileEffect>() {

	override fun setInitialState(): EditProfileState =
		EditProfileState(data = null, loading = true)

	override fun handleActions(action: EditProfileAction) {
		when (action) {
			is EditProfileAction.SaveClicked          -> {
				saveChanges()
			}

			is EditProfileAction.BackClicked          -> {
				setEffect { EditProfileEffect.Navigation.ToBack }
			}

			is EditProfileAction.FullNameChanged      -> {
				setState {
					copy(data = data?.copy(fullName = action.newValue))
				}
			}

			is EditProfileAction.ShortNameChanged     -> {
				setState {
					copy(data = data?.copy(shortName = action.newValue))
				}
			}

			is EditProfileAction.TinChanged           -> {
				setState {
					copy(data = data?.copy(tin = action.newValue))
				}
			}

			is EditProfileAction.IecChanged           -> {
				setState {
					copy(data = data?.copy(iec = action.newValue))
				}
			}

			is EditProfileAction.LegalAddressChanged  -> {
				setState {
					copy(data = data?.copy(legalAddress = action.newValue))
				}
			}

			is EditProfileAction.ActualAddressChanged -> {
				setState {
					copy(data = data?.copy(actualAddress = action.newValue))
				}
			}

			is EditProfileAction.EmailChanged         -> {
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
			delay(2_000) // TODO delete

			try {
				// TODO get profile use case
				setState { copy(data = getProfileMock(), loading = false) }
			} catch (e: Throwable) {
				setState { copy(data = null, loading = false) }
			}
		}
	}

	private fun getProfileMock(): EditData =
		EditData(
			fullName = "Публичное акционерное общество “Компания”",
			shortName = "ПАО “Компания”",
			tin = "7708004761",
			iec = "43653462219",
			legalAddress = "101000, Москва, Бульвар Сретенский, 11",
			actualAddress = "117420, Москва, Наметкина, 16",
			email = "company@companymai.ru",
		)

	private fun saveChanges() {
		viewModelScope.launch {
			setState { copy(loading = true) }
			delay(2_000) // TODO delete

			try {
				// TODO save changed use case
				setEffect { EditProfileEffect.Navigation.ToBack }
			} catch (e: Throwable) {
				setState { copy(loading = false) }
				setEffect { EditProfileEffect.Error() }
			}
		}
	}
}