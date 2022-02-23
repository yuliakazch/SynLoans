package com.yuliakazachok.synloans.android.features.paymentschedule.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleEffect
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PaymentScheduleDestination(navController: NavHostController, requestId: Int) {

	val viewModel = getViewModel<PaymentScheduleViewModel> {
		parametersOf(requestId)
	}
	val state = viewModel.viewState.collectAsState().value

	PaymentScheduleScreen(
		state = state,
		effectFlow = viewModel.effect,
		onActionSent = { action -> viewModel.setEvent(action) },
		onNavigationRequested = { navigationEffect ->
			when (navigationEffect) {
				is PaymentScheduleEffect.Navigation.ToBack -> {
					navController.popBackStack()
				}
			}
		}
	)
}