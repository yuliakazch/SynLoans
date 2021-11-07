package com.yuliakazachok.synloans.android.features.joinsyndicate.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateEffect
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun JoinSyndicateDestination(navController: NavHostController) {

	val viewModel = getViewModel<JoinSyndicateViewModel>()
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