package com.yuliakazachok.synloans.android.features.signin.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State
import com.yuliakazachok.synloans.features.signin.domain.entity.Credentials

sealed class SignInAction : Action {

    object SignInClicked : SignInAction()

    object RegistrationClicked : SignInAction()

    data class EmailChanged(val newValue: String) : SignInAction()

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