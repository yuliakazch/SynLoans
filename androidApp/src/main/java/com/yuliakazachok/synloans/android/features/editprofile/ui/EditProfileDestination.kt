package com.yuliakazachok.synloans.android.features.editprofile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.core.NavigationKeys
import com.yuliakazachok.synloans.android.features.editprofile.presentation.EditProfileEffect
import com.yuliakazachok.synloans.android.features.editprofile.presentation.EditProfileViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalComposeUiApi
@Composable
fun EditProfileDestination(navController: NavHostController) {

	val viewModel = getViewModel<EditProfileViewModel>()
	val state = viewModel.viewState.collectAsState().value

	EditProfileScreen(
		state = state,
		effectFlow = viewModel.effect,
		onActionSent = { action -> viewModel.setEvent(action) },
		onNavigationRequested = { navigationEffect ->
			when (navigationEffect) {
				is EditProfileEffect.Navigation.ToBack -> {
					navController.navigate(NavigationKeys.PROFILE) {
						popUpTo(NavigationKeys.EDIT_PROFILE) { inclusive = true }
					}
				}
			}
		}
	)
}