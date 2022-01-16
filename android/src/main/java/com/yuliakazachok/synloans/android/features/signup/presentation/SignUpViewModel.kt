package com.yuliakazachok.synloans.android.features.signup.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.user.domain.entity.SignUpInfo
import com.yuliakazachok.synloans.shared.user.domain.usecase.SignUpUseCase
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpAction, SignUpState, SignUpEffect>() {

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

            is SignUpAction.FullNameChanged -> {
                setState {
                    copy(content = content.copy(fullName = action.newValue))
                }
            }

            is SignUpAction.ShortNameChanged -> {
                setState {
                    copy(content = content.copy(shortName = action.newValue))
                }
            }

            is SignUpAction.TinChanged -> {
                setState {
                    copy(content = content.copy(inn = action.newValue))
                }
            }

            is SignUpAction.IecChanged -> {
                setState {
                    copy(content = content.copy(kpp = action.newValue))
                }
            }

            is SignUpAction.LegalAddressChanged -> {
                setState {
                    copy(content = content.copy(legalAddress = action.newValue))
                }
            }

            is SignUpAction.ActualAddressChanged -> {
                setState {
                    copy(content = content.copy(actualAddress = action.newValue))
                }
            }

            is SignUpAction.CreditOrganisationCheckChanged -> {
                setState {
                    copy(content = content.copy(creditOrganisation = action.newValue))
                }
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
                val password = viewState.value.content.password
                val passwordAgain = viewState.value.content.passwordAgain
                if (password == passwordAgain) {
                    signUpUseCase(viewState.value.content.convertToSignUpInfo())
                    setEffect { SignUpEffect.Navigation.ToBack }
                } else {
                    handleError()
                }
            } catch (e: Throwable) {
                handleError()
            }
        }
    }

    private fun SignUpData.convertToSignUpInfo(): SignUpInfo =
        SignUpInfo(fullName, shortName, inn, kpp, legalAddress, actualAddress, creditOrganisation, email, password)

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