package com.yuliakazachok.synloans.android.features.signup.presentation

import com.yuliakazachok.synloans.android.util.Action
import com.yuliakazachok.synloans.android.util.Effect
import com.yuliakazachok.synloans.android.util.State

sealed class SignUpAction : Action {

    object SignUpClicked : SignUpAction()

    object BackClicked : SignUpAction()

    data class EmailChanged(val newValue: String) : SignUpAction()

    data class PasswordChanged(val newValue: String) : SignUpAction()

    data class PasswordAgainChanged(val newValue: String) : SignUpAction()
}

data class SignUpState(val content: SignUpData, val loading: Boolean = false) : State

sealed class SignUpEffect : Effect {

    data class Error(val message: String? = null) : SignUpEffect()

    sealed class Navigation : SignUpEffect() {

        object ToBack : Navigation()
    }
}