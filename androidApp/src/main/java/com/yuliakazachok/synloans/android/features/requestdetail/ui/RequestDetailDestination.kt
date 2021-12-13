package com.yuliakazachok.synloans.android.features.requestdetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.yuliakazachok.synloans.android.core.NavigationKeys
import com.yuliakazachok.synloans.android.core.NavigationKeys.BANK_DETAIL
import com.yuliakazachok.synloans.android.core.NavigationKeys.JOIN_SYNDICATE
import com.yuliakazachok.synloans.android.core.NavigationKeys.PAYMENT_SCHEDULE
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailEffect
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalPagerApi
@Composable
fun RequestDetailDestination(navController: NavHostController, requestId: Int) {

    val viewModel = getViewModel<RequestDetailViewModel> {
        parametersOf(requestId)
    }
    val state = viewModel.viewState.collectAsState().value

    RequestDetailScreen(
        state = state,
        effectFlow = viewModel.effect,
        onActionSent = { action -> viewModel.setEvent(action) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is RequestDetailEffect.Navigation.ToBack -> {
                    navController.navigate(NavigationKeys.REQUESTS) {
                        popUpTo(NavigationKeys.REQUESTS) { inclusive = true }
                    }
                }

                is RequestDetailEffect.Navigation.ToPaymentSchedule -> {
                    navController.navigate(PAYMENT_SCHEDULE)
                }

                is RequestDetailEffect.Navigation.ToJoinSyndicate -> {
                    navController.navigate(JOIN_SYNDICATE)
                }

                is RequestDetailEffect.Navigation.ToBankItem -> {
                    navController.navigate(BANK_DETAIL)
                }
            }
        }
    )
}