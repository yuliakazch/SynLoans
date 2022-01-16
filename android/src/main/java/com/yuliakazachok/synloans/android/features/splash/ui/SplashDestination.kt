package com.yuliakazachok.synloans.android.features.splash.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.core.NavigationKeys
import com.yuliakazachok.synloans.android.features.splash.presentation.SplashEffect
import com.yuliakazachok.synloans.android.features.splash.presentation.SplashViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashDestination(navController: NavHostController) {

    val viewModel = getViewModel<SplashViewModel>()

    SplashScreen(
        effectFlow = viewModel.effect,
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SplashEffect.Navigation.ToSignIn -> {
                    navController.navigate(NavigationKeys.SIGN_IN) {
                        popUpTo(NavigationKeys.SPLASH) { inclusive = true }
                    }
                }

                is SplashEffect.Navigation.ToProfile -> {
                    navController.navigate(NavigationKeys.PROFILE) {
                        popUpTo(NavigationKeys.SPLASH) { inclusive = true }
                    }
                }
            }
        }
    )
}