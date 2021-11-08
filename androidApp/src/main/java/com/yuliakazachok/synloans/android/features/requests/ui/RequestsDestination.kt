package com.yuliakazachok.synloans.android.features.requests.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.yuliakazachok.synloans.android.core.NavigationKeys.CREATE_REQUEST
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUEST_DETAIL
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsEffect
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalPagerApi
@Composable
fun RequestsDestination(navController: NavHostController) {

	val viewModel = getViewModel<RequestsViewModel>()
	val state = viewModel.viewState.collectAsState().value

	if (state.creditOrganisation) {
		BankRequestsScreen(
			state = state,
			effectFlow = viewModel.effect,
			onActionSent = { action -> viewModel.setEvent(action) },
			onNavigationRequested = { navigationEffect ->
				when (navigationEffect) {
					is RequestsEffect.Navigation.ToCreateRequest -> {
						navController.navigate(CREATE_REQUEST)
					}

					is RequestsEffect.Navigation.ToRequest       -> {
						navController.navigate(REQUEST_DETAIL)
					}

					is RequestsEffect.Navigation.ToProfile       -> {
						navController.navigate(PROFILE) {
							popUpTo(REQUESTS) { inclusive = true }
						}
					}
				}
			}
		)
	} else {
		BorrowerRequestsScreen(
			state = state,
			effectFlow = viewModel.effect,
			onActionSent = { action -> viewModel.setEvent(action) },
			onNavigationRequested = { navigationEffect ->
				when (navigationEffect) {
					is RequestsEffect.Navigation.ToCreateRequest -> {
						navController.navigate(CREATE_REQUEST)
					}

					is RequestsEffect.Navigation.ToRequest       -> {
						navController.navigate(REQUEST_DETAIL)
					}

					is RequestsEffect.Navigation.ToProfile       -> {
						navController.navigate(PROFILE) {
							popUpTo(REQUESTS) { inclusive = true }
						}
					}
				}
			}
		)
	}
}