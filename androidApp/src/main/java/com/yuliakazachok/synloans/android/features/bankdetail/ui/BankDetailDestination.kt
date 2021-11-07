package com.yuliakazachok.synloans.android.features.bankdetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.features.bankdetail.presentation.BankDetailEffect
import com.yuliakazachok.synloans.android.features.bankdetail.presentation.BankDetailViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun BankDetailDestination(navController: NavHostController) {

	val viewModel = getViewModel<BankDetailViewModel>()
	val state = viewModel.viewState.collectAsState().value

	BankDetailScreen(
		state = state,
		effectFlow = viewModel.effect,
		onActionSent = { action -> viewModel.setEvent(action) },
		onNavigationRequested = { navigationEffect ->
			when (navigationEffect) {
				is BankDetailEffect.Navigation.ToBack -> {
					navController.popBackStack()
				}
			}
		}
	)
}