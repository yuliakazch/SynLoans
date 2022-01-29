package com.yuliakazachok.synloans.android.features.editprofile.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.textfield.EditTextView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.editprofile.presentation.EditProfileAction
import com.yuliakazachok.synloans.android.features.editprofile.presentation.EditProfileEffect
import com.yuliakazachok.synloans.android.features.editprofile.presentation.EditProfileState
import com.yuliakazachok.synloans.shared.user.domain.entity.EditProfileInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditProfileScreen(
	state: EditProfileState,
	effectFlow: Flow<EditProfileEffect>?,
	onActionSent: (action: EditProfileAction) -> Unit,
	onNavigationRequested: (navigationEffect: EditProfileEffect.Navigation) -> Unit
) {
	val scaffoldState: ScaffoldState = rememberScaffoldState()

	val textError = stringResource(R.string.error)

	LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
		effectFlow?.onEach { effect ->
			when (effect) {
				is EditProfileEffect.Error      ->
					scaffoldState.snackbarHostState.showSnackbar(
						message = effect.message ?: textError,
						duration = SnackbarDuration.Short
					)
				is EditProfileEffect.Navigation ->
					onNavigationRequested(effect)
			}
		}?.collect()
	}

	Scaffold(
		scaffoldState = scaffoldState,
		topBar = {
			TopBarBackView(
				title = stringResource(R.string.edit_profile_title),
				onIconClicked = { onActionSent(EditProfileAction.BackClicked) },
			)
		}
	) {
		when {
			state.loading      -> LoadingView()

			state.data == null -> ErrorView()

			else               -> EditProfileView(state.data, onActionSent)
		}
	}
}

@Composable
fun EditProfileView(
	data: EditProfileInfo,
	onActionSent: (action: EditProfileAction) -> Unit,
) {
	val listState = rememberLazyListState()

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
	) {
		item {
			EditTextView(
				text = data.fullName,
				label = stringResource(R.string.field_full_name_company),
				onTextChange = { onActionSent(EditProfileAction.FullNameChanged(it)) },
			)
		}
		item {
			EditTextView(
				text = data.shortName,
				label = stringResource(R.string.field_short_name_company),
				onTextChange = { onActionSent(EditProfileAction.ShortNameChanged(it)) },
			)
		}
		item {
			EditTextView(
				text = data.inn,
				label = stringResource(R.string.field_tin),
				onTextChange = { onActionSent(EditProfileAction.TinChanged(it)) },
			)
		}
		item {
			EditTextView(
				text = data.kpp,
				label = stringResource(R.string.field_iec),
				onTextChange = { onActionSent(EditProfileAction.IecChanged(it)) },
			)
		}
		item {
			EditTextView(
				text = data.legalAddress,
				label = stringResource(R.string.field_legal_address),
				onTextChange = { onActionSent(EditProfileAction.LegalAddressChanged(it)) },
			)
		}
		item {
			EditTextView(
				text = data.actualAddress,
				label = stringResource(R.string.field_actual_address),
				onTextChange = { onActionSent(EditProfileAction.ActualAddressChanged(it)) },
			)
		}
		item {
			EditTextView(
				text = data.email,
				label = stringResource(R.string.field_email),
				onTextChange = { onActionSent(EditProfileAction.EmailChanged(it)) },
			)
		}
		item {
			Button(
				onClick = { onActionSent(EditProfileAction.SaveClicked) },
				modifier = Modifier
					.padding(vertical = 12.dp)
					.fillMaxWidth()
			) {
				Text(stringResource(R.string.sign_up))
			}
		}
	}
}