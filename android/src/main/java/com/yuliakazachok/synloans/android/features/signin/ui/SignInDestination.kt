package com.yuliakazachok.synloans.android.features.signin.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_IN
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInEffect
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInViewModel
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_UP
import org.koin.androidx.compose.getViewModel

@Composable
fun SignInDestination(navController: NavHostController) {

    val viewModel = getViewModel<SignInViewModel>()
    val state = viewModel.viewState.collectAsState().value

    SignInScreen(
        state = state,
        effectFlow = viewModel.effect,
        onActionSent = { action -> viewModel.setEvent(action) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SignInEffect.Navigation.ToProfile -> {
                    navController.navigate(PROFILE) {
                        popUpTo(SIGN_IN) { inclusive = true }
                    }
                }

                is SignInEffect.Navigation.ToRegistration -> {
                    navController.navigate(SIGN_UP)
                }
            }
        }
    )
}