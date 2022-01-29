package com.yuliakazachok.synloans.android.features.signup.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.features.signup.presentation.SignUpEffect
import com.yuliakazachok.synloans.android.features.signup.presentation.SignUpViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SignUpDestination(navController: NavHostController) {

    val viewModel = getViewModel<SignUpViewModel>()
    val state = viewModel.viewState.collectAsState().value

    SignUpScreen(
        state = state,
        effectFlow = viewModel.effect,
        onActionSent = { action -> viewModel.setEvent(action) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SignUpEffect.Navigation.ToBack -> {
                    navController.popBackStack()
                }
            }
        }
    )
}