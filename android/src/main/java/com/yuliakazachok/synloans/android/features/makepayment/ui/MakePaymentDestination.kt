package com.yuliakazachok.synloans.android.features.makepayment.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.core.NavigationKeys
import com.yuliakazachok.synloans.android.features.makepayment.presentation.MakePaymentEffect
import com.yuliakazachok.synloans.android.features.makepayment.presentation.MakePaymentViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MakePaymentDestination(navController: NavHostController, requestId: Int) {

    val viewModel = getViewModel<MakePaymentViewModel>() {
        parametersOf(requestId)
    }
    val state = viewModel.viewState.collectAsState().value

    MakePaymentScreen(
        state = state,
        effectFlow = viewModel.effect,
        onActionSent = { action -> viewModel.setEvent(action) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is MakePaymentEffect.Navigation.ToBack -> {
                    navController.popBackStack()
                }
            }
        }
    )
}