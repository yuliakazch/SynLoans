package com.yuliakazachok.synloans.android.features.signup.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.util.BaseViewModel
import kotlinx.coroutines.launch

class SignUpViewModel() : BaseViewModel<SignUpAction, SignUpState, SignUpEffect>() {

    override fun setInitialState(): SignUpState =
        SignUpState(content = SignUpData(), loading = false)

    override fun handleActions(action: SignUpAction) {
        when (action) {
            is SignUpAction.SignUpClicked -> {
                signUp()
            }

            is SignUpAction.BackClicked -> {
                setEffect { SignUpEffect.Navigation.ToBack }
            }

            is SignUpAction.EmailChanged -> {
                setState {
                    copy(content = content.copy(email = action.newValue))
                }
            }

            is SignUpAction.PasswordChanged -> {
                setState {
                    copy(content = content.copy(password = action.newValue))
                }
            }

            is SignUpAction.PasswordAgainChanged -> {
                setState {
                    copy(content = content.copy(passwordAgain = action.newValue))
                }
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val email = viewState.value.content.email
                val password = viewState.value.content.password
                val passwordAgain = viewState.value.content.passwordAgain
                if (password == passwordAgain) {
                    //signUpUseCase
                    setEffect { SignUpEffect.Navigation.ToBack }
                } else {
                    handleError()
                }
            } catch (e: Throwable) {
                handleError()
            }
        }
    }

    private fun handleError() {
        setState {
            copy(
                content = content.copy(password = "", passwordAgain = ""),
                loading = false
            )
        }
        setEffect { SignUpEffect.Error() }
    }
}