package com.yuliakazachok.synloans.android.features.joinsyndicate.ui

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
import com.yuliakazachok.synloans.android.components.checkbox.TextWithCheckboxView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.textfield.EditNumberTextView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinData
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateAction
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateEffect
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun JoinSyndicateScreen(
	state: JoinSyndicateState,
	effectFlow: Flow<JoinSyndicateEffect>?,
	onActionSent: (action: JoinSyndicateAction) -> Unit,
	onNavigationRequested: (navigationEffect: JoinSyndicateEffect.Navigation) -> Unit
) {
	val scaffoldState: ScaffoldState = rememberScaffoldState()

	val textError = stringResource(R.string.error)

	LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
		effectFlow?.onEach { effect ->
			when (effect) {
				is JoinSyndicateEffect.Error ->
					scaffoldState.snackbarHostState.showSnackbar(
						message = effect.message ?: textError,
						duration = SnackbarDuration.Short
					)
				is JoinSyndicateEffect.Navigation ->
					onNavigationRequested(effect)
			}
		}?.collect()
	}

	Scaffold(
		topBar = {
			TopBarBackView(
				title = stringResource(R.string.join_syndicate_title),
				onIconClicked = { onActionSent(JoinSyndicateAction.BackClicked) },
			)
		}
	) {
		if (state.loading) {
			LoadingView()
		} else {
			JoinSyndicateView(state.data, onActionSent)
		}
	}
}

@Composable
fun JoinSyndicateView(
	data: JoinData,
	onActionSent: (action: JoinSyndicateAction) -> Unit,
) {
	val listState = rememberLazyListState()

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
	) {
		item {
			EditNumberTextView(
				text = data.sum,
				label = stringResource(R.string.request_sum),
				onTextChange = { onActionSent(JoinSyndicateAction.SumChanged(it)) },
			)
		}
		item {
			TextWithCheckboxView(
				text = stringResource(R.string.request_approve_bank_agent),
				checked = data.approveBankAgent,
				onCheckedChange = { onActionSent(JoinSyndicateAction.ApproveBankAgentCheckChanged(it)) },
			)
		}
		item {
			Button(
				onClick = { onActionSent(JoinSyndicateAction.JoinClicked) },
				modifier = Modifier
					.padding(vertical = 12.dp)
					.fillMaxWidth()
			) {
				Text(stringResource(R.string.join_syndicate_button))
			}
		}
	}
}