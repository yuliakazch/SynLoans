package com.yuliakazachok.synloans.android.features.requests.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.text.TextFullScreenView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.android.components.topbar.TopBarEndIconView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsAction
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsEffect
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsState
import com.yuliakazachok.synloans.features.requests.domain.entity.BorrowRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun BorrowerRequestsScreen(
	state: RequestsState,
	effectFlow: Flow<RequestsEffect>?,
	onActionSent: (action: RequestsAction) -> Unit,
	onNavigationRequested: (navigationEffect: RequestsEffect.Navigation) -> Unit
) {

	LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
		effectFlow?.onEach { effect ->
			when (effect) {
				is RequestsEffect.Navigation ->
					onNavigationRequested(effect)
			}
		}?.collect()
	}

	Scaffold(
		topBar = {
			TopBarEndIconView(
				title = stringResource(R.string.requests_title),
				icon = Icons.Filled.AddCircle,
				onIconClicked = { onActionSent(RequestsAction.CreateRequestClicked) },
			)
		}
	) {
		when {
			state.loading                  -> LoadingView()

			state.borrowRequests == null   -> ErrorView()

			state.borrowRequests.isEmpty() -> TextFullScreenView(stringResource(R.string.requests_empty))

			else                           -> BorrowerRequestsView(state.borrowRequests) { onActionSent(RequestsAction.RequestClicked) }
		}
	}
}

@Composable
fun BorrowerRequestsView(
	requests: List<BorrowRequest>,
	onRequestClicked: () -> Unit,
) {
	val listState = rememberLazyListState()
	val textDateCreate = stringResource(R.string.requests_date_create)
	val textDateIssue = stringResource(R.string.requests_date_issue)
	val textSumUnit = stringResource(R.string.requests_sum_units)

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
	) {
		requests.forEach { request ->
			item {
				TextTwoLinesView(
					textOne = if (request.dateIssue.isNullOrEmpty()) {
						textDateCreate + request.dateCreate
					} else {
						textDateIssue + request.dateIssue
					},
					textTwo = request.sum.toString() + textSumUnit,
					onClicked = { onRequestClicked() }
				)
			}
		}
	}
}