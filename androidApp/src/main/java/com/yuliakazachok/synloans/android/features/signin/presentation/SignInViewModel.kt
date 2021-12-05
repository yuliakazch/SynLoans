package com.yuliakazachok.synloans.android.features.signin.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.user.signin.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.signin.domain.usecase.SignInUseCase
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
) : BaseViewModel<SignInAction, SignInState, SignInEffect>() {

    override fun setInitialState(): SignInState =
        SignInState(credentials = Credentials(email = "", password = ""), loading = false)

    override fun handleActions(action: SignInAction) {
        when (action) {
            is SignInAction.SignInClicked -> {
                login()
            }

            is SignInAction.RegistrationClicked -> {
                setEffect { SignInEffect.Navigation.ToRegistration }
            }

            is SignInAction.EmailChanged -> {
                setState {
                    copy(credentials = credentials.copy(email = action.newValue))
                }
            }

            is SignInAction.PasswordChanged -> {
                setState {
                    copy(credentials = credentials.copy(password = action.newValue))
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            setState { copy(loading = true) }

            try {
                signInUseCase(viewState.value.credentials)
                setEffect { SignInEffect.Navigation.ToProfile }
            } catch (e: Throwable) {
                setState { copy(credentials = credentials.copy(password = ""), loading = false) }
                setEffect { SignInEffect.Error() }
            }
        }
    }
}