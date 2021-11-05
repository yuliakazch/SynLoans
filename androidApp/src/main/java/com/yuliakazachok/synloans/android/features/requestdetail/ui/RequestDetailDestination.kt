package com.yuliakazachok.synloans.android.features.requestdetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailEffect
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalPagerApi
@Composable
fun RequestDetailDestination(navController: NavHostController) {

	val viewModel = getViewModel<RequestDetailViewModel>()
	val state = viewModel.viewState.collectAsState().value

	RequestDetailScreen(
		state = state,
		effectFlow = viewModel.effect,
		onActionSent = { action -> viewModel.setEvent(action) },
		onNavigationRequested = { navigationEffect ->
			when (navigationEffect) {
				is RequestDetailEffect.Navigation.ToBack            -> {
					navController.popBackStack()
				}

				is RequestDetailEffect.Navigation.ToPaymentSchedule -> {
					// TODO navigate
				}

				is RequestDetailEffect.Navigation.ToJoinSyndicate   -> {
					// TODO navigate
				}

				is RequestDetailEffect.Navigation.ToBankItem        -> {
					// TODO navigate
				}
			}
		}
	)
}