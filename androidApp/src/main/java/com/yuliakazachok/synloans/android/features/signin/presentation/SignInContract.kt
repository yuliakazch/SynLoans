package com.yuliakazachok.synloans.android.features.signin.presentation

import com.yuliakazachok.synloans.android.util.Action
import com.yuliakazachok.synloans.android.util.Effect
import com.yuliakazachok.synloans.android.util.State
import com.yuliakazachok.synloans.signin.domain.entity.Credentials

sealed class SignInAction : Action {

    object SignInClicked : SignInAction()

    object RegistrationClicked : SignInAction()

    data class LoginChanged(val newValue: String) : SignInAction()

    data class PasswordChanged(val newValue: String) : SignInAction()
}

data class SignInState(val credentials: Credentials, val loading: Boolean = false) : State

sealed class SignInEffect : Effect {

    data class Error(val message: String? = null) : SignInEffect()

    sealed class Navigation : SignInEffect() {

        object ToProfile: Navigation()

        object ToRegistration : Navigation()
    }
}