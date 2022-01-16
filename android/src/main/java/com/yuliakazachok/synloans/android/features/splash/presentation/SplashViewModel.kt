package com.yuliakazachok.synloans.android.features.splash.presentation

import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.token.domain.usecase.IsTokenExistUseCase

class SplashViewModel(
    private val isTokenExistUseCase: IsTokenExistUseCase,
) : BaseViewModel<SplashAction, SplashState, SplashEffect>() {

    override fun setInitialState(): SplashState =
        SplashState

    init {
        if (isTokenExistUseCase()) {
            setEffect { SplashEffect.Navigation.ToProfile }
        } else {
            setEffect { SplashEffect.Navigation.ToSignIn }
        }
    }

    override fun handleActions(action: SplashAction) {}
}