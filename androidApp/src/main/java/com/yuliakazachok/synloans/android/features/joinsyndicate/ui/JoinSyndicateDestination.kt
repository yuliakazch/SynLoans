package com.yuliakazachok.synloans.android.features.joinsyndicate.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateEffect
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun JoinSyndicateDestination(navController: NavHostController, requestId: Int) {

	val viewModel = getViewModel<JoinSyndicateViewModel> {
		parametersOf(requestId)
	}
	val state = viewModel.viewState.collectAsState().value

	JoinSyndicateScreen(
		state = state,
		effectFlow = viewModel.effect,
		onActionSent = { action -> viewModel.setEvent(action) },
		onNavigationRequested = { navigationEffect ->
			when (navigationEffect) {
				is JoinSyndicateEffect.Navigation.ToBack -> {
					navController.popBackStack()
				}
			}
		}
	)
}