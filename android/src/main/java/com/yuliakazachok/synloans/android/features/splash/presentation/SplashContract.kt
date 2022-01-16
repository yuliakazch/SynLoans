package com.yuliakazachok.synloans.android.features.splash.presentation

import com.yuliakazachok.synloans.android.core.Action
import com.yuliakazachok.synloans.android.core.Effect
import com.yuliakazachok.synloans.android.core.State

sealed class SplashAction : Action

object SplashState : State

sealed class SplashEffect : Effect {

    sealed class Navigation : SplashEffect() {

        object ToSignIn : Navigation()

        object ToProfile : Navigation()
    }
}