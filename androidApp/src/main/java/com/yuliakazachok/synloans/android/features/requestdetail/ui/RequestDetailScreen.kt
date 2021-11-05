package com.yuliakazachok.synloans.android.features.requestdetail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailAction
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailEffect
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailState
import com.yuliakazachok.synloans.features.requestdetail.domain.entity.RequestInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun RequestDetailScreen(
	state: RequestDetailState,
	effectFlow: Flow<RequestDetailEffect>?,
	onActionSent: (action: RequestDetailAction) -> Unit,
	onNavigationRequested: (navigationEffect: RequestDetailEffect.Navigation) -> Unit
) {
	val scaffoldState: ScaffoldState = rememberScaffoldState()

	val textError = stringResource(R.string.error)

	LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
		effectFlow?.onEach { effect ->
			when (effect) {
				is RequestDetailEffect.Error      ->
					scaffoldState.snackbarHostState.showSnackbar(
						message = effect.message ?: textError,
						duration = SnackbarDuration.Short
					)
				is RequestDetailEffect.Navigation ->
					onNavigationRequested(effect)
			}
		}?.collect()
	}

	Scaffold(
		topBar = {
			TopBarBackView(
				title = stringResource(R.string.request_title),
				onIconClicked = { onActionSent(RequestDetailAction.BackClicked) },
			)
		}
	) {
		when {
			state.loading         -> LoadingView()

			state.request == null -> ErrorView()

			else                  -> RequestDetailView(state.request, onActionSent)
		}
	}
}

@ExperimentalPagerApi
@Composable
fun RequestDetailView(
	request: RequestInfo,
	onActionSent: (action: RequestDetailAction) -> Unit,
) {
	val tabData = listOf(
		0 to R.string.request_info,
		1 to R.string.request_banks,
		2 to R.string.request_borrower,
	)

	val pagerState = rememberPagerState()
	val coroutineScope = rememberCoroutineScope()
	val tabIndex = pagerState.currentPage

	Column {
		TabRow(
			selectedTabIndex = tabIndex,
			backgroundColor = MaterialTheme.colors.background,
			indicator = {},
			divider = {},
		) {
			tabData.forEach { (index, textId) ->
				Tab(
					selected = tabIndex == index,
					onClick = {
						coroutineScope.launch {
							pagerState.animateScrollToPage(index)
						}
					},
					text = {
						Text(
							text = stringResource(textId),
							fontSize = 18.sp
						)
					},
				)
			}
		}

		HorizontalPager(
			count = 3,
			state = pagerState,
			modifier = Modifier.weight(1f)
		) { index ->
			Column(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(text = "page=$index")
			}
		}
	}
}