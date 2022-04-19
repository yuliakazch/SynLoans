package com.yuliakazachok.synloans.android.features.requestdetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.core.NavigationKeys.BANK_DETAIL
import com.yuliakazachok.synloans.android.core.NavigationKeys.JOIN_SYNDICATE
import com.yuliakazachok.synloans.android.core.NavigationKeys.MAKE_PAYMENT
import com.yuliakazachok.synloans.android.core.NavigationKeys.PAYMENT_SCHEDULE
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailEffect
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RequestDetailDestination(navController: NavHostController, requestId: Int, participant: Boolean) {

    val viewModel = getViewModel<RequestDetailViewModel> {
        parametersOf(requestId, participant)
    }
    val state = viewModel.viewState.collectAsState().value

    RequestDetailScreen(
        state = state,
        effectFlow = viewModel.effect,
        onActionSent = { action -> viewModel.setEvent(action) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is RequestDetailEffect.Navigation.ToBack -> {
                    navController.navigate(REQUESTS) {
                        popUpTo(REQUESTS) { inclusive = true }
                    }
                }

                is RequestDetailEffect.Navigation.ToPaymentSchedule -> {
                    navController.navigate("${PAYMENT_SCHEDULE}/${navigationEffect.id}/${navigationEffect.schedule}")
                }

                is RequestDetailEffect.Navigation.ToJoinSyndicate -> {
                    navController.navigate("${JOIN_SYNDICATE}/${navigationEffect.id}")
                }

                is RequestDetailEffect.Navigation.ToBankItem -> {
                    navController.navigate("${BANK_DETAIL}/${navigationEffect.id}")
                }

                is RequestDetailEffect.Navigation.ToMakePayment -> {
                    navController.navigate("${MAKE_PAYMENT}/${navigationEffect.id}")
                }
            }
        }
    )
}