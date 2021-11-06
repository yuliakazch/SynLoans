package com.yuliakazachok.synloans.android.features.requestcreate.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.RequestCreateEffect
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.RequestCreateViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun RequestCreateDestination(navController: NavHostController) {

	val viewModel = getViewModel<RequestCreateViewModel>()
	val state = viewModel.viewState.collectAsState().value

	RequestCreateScreen(
		state = state,
		effectFlow = viewModel.effect,
		onActionSent = { action -> viewModel.setEvent(action) },
		onNavigationRequested = { navigationEffect ->
			when (navigationEffect) {
				is RequestCreateEffect.Navigation.ToBack -> {
					navController.popBackStack()
				}
			}
		}
	)
}