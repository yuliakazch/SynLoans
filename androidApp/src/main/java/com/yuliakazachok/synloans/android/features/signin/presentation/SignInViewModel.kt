package com.yuliakazachok.synloans.android.features.signin.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.util.BaseViewModel
import com.yuliakazachok.synloans.features.signin.domain.entity.Credentials
import kotlinx.coroutines.launch

class SignInViewModel(
    //private val repository: SignInRepository,
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
                //repository.signIn(viewState.value.credentials)
                setEffect { SignInEffect.Navigation.ToProfile }
            } catch (e: Throwable) {
                setState { copy(credentials = credentials.copy(password = ""), loading = false) }
                setEffect { SignInEffect.Error() }
            }
        }
    }
}