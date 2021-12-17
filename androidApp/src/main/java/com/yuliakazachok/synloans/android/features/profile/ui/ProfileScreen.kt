package com.yuliakazachok.synloans.android.features.profile.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.android.components.topbar.TopBarTwoEndIconView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.profile.presentation.ProfileAction
import com.yuliakazachok.synloans.android.features.profile.presentation.ProfileEffect
import com.yuliakazachok.synloans.android.features.profile.presentation.ProfileState
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ProfileScreen(
	state: ProfileState,
	effectFlow: Flow<ProfileEffect>?,
	onActionSent: (action: ProfileAction) -> Unit,
	onNavigationRequested: (navigationEffect: ProfileEffect.Navigation) -> Unit
) {

	LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
		effectFlow?.onEach { effect ->
			when (effect) {
				is ProfileEffect.Navigation ->
					onNavigationRequested(effect)
			}
		}?.collect()
	}

	Scaffold(
		topBar = {
			TopBarTwoEndIconView(
				title = stringResource(R.string.profile_title),
				iconOne = Icons.Filled.Edit,
				iconTwo = Icons.Filled.ExitToApp,
				onOneIconClicked = { onActionSent(ProfileAction.EditProfileClicked) },
				onTwoIconClicked = { onActionSent(ProfileAction.LogoutClicked) },
			)
		}
	) {
		when {
			state.loading -> LoadingView()

			state.profile == null -> ErrorView()

			else -> ProfileView(state.profile)
		}
	}
}

@Composable
fun ProfileView(
	profile: Profile,
) {
	val listState = rememberLazyListState()

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp)
	) {
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_full_name_company),
				textTwo = profile.fullName,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_short_name_company),
				textTwo = profile.shortName,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_tin),
				textTwo = profile.inn,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_iec),
				textTwo = profile.kpp,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_legal_address),
				textTwo = profile.legalAddress,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_actual_address),
				textTwo = profile.actualAddress,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_email),
				textTwo = profile.email,
			)
		}
	}
}