package com.yuliakazachok.synloans.android.features.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.core.NavigationKeys.EDIT_PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_IN
import com.yuliakazachok.synloans.android.features.profile.presentation.ProfileEffect
import com.yuliakazachok.synloans.android.features.profile.presentation.ProfileViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ProfileDestination(navController: NavHostController) {

	val viewModel = getViewModel<ProfileViewModel>()
	val state = viewModel.viewState.collectAsState().value

	ProfileScreen(
		state = state,
		effectFlow = viewModel.effect,
		onActionSent = { action -> viewModel.setEvent(action) },
		onNavigationRequested = { navigationEffect ->
			when (navigationEffect) {
				is ProfileEffect.Navigation.ToRequests -> {
					navController.navigate(REQUESTS)
				}

				is ProfileEffect.Navigation.ToEditProfile -> {
					navController.navigate(EDIT_PROFILE)
				}

				is ProfileEffect.Navigation.ToLogout -> {
					navController.navigate(SIGN_IN) {
						popUpTo(PROFILE) { inclusive = true }
					}
				}
			}
		}
	)
}