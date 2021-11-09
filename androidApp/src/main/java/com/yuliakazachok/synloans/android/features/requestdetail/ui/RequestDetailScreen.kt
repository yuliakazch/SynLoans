package com.yuliakazachok.synloans.android.features.requestdetail.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.core.getIndexYearText
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailAction
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailEffect
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailState
import com.yuliakazachok.synloans.features.requestdetail.domain.entity.*
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
	request: RequestCommon,
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
		) { index ->
			when (index) {
				0 -> RequestInfoView(request.info, onActionSent)

				1 -> BanksView(request.banks, onActionSent)

				2 -> BorrowerView(request.borrower)
			}
		}
	}
}

@SuppressLint("ResourceType")
@Composable
fun RequestInfoView(
	request: RequestInfo,
	onActionSent: (action: RequestDetailAction) -> Unit,
) {
	val listState = rememberLazyListState()
	val yearTexts = stringArrayResource(R.array.request_years)

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
	) {
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.request_status),
				textTwo = when (request.status) {
					StatusRequest.OPEN     -> stringResource(R.string.request_status_open)
					StatusRequest.TRANSFER -> stringResource(R.string.request_status_transfer)
					StatusRequest.ISSUE    -> stringResource(R.string.request_status_issue)
					StatusRequest.CLOSE    -> stringResource(R.string.request_status_close)
				},
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.request_sum),
				textTwo = request.sum.toString() + stringResource(R.string.requests_sum_units),
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.request_max_rate),
				textTwo = request.maxRate.toString() + stringResource(R.string.request_rate_units),
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.request_term),
				textTwo = request.term.toString() + yearTexts[getIndexYearText(request.term)],
			)
		}
		if (request.dateIssue != null) {
			item {
				TextTwoLinesView(
					textOne = stringResource(R.string.requests_date_issue),
					textTwo = request.dateIssue.toString(),
				)
			}
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.requests_date_create),
				textTwo = request.dateCreate,
			)
		}

		if (request.dateIssue != null) {
			item {
				Button(
					onClick = { onActionSent(RequestDetailAction.PaymentScheduleClicked) },
					modifier = Modifier.fillMaxWidth(),
				) {
					Text(stringResource(R.string.request_payment_schedule))
				}
			}
		} else {
			item {
				Button(
					onClick = { onActionSent(RequestDetailAction.CancelRequestClicked) },
					modifier = Modifier.fillMaxWidth(),
				) {
					Text(stringResource(R.string.request_cancel))
				}
			}
			item {
				Button(
					onClick = { onActionSent(RequestDetailAction.JoinSyndicateClicked) },
					modifier = Modifier.fillMaxWidth(),
				) {
					Text(stringResource(R.string.request_join_syndicate))
				}
			}
		}
	}
}

@Composable
fun BanksView(
	banks: List<Bank>,
	onActionSent: (action: RequestDetailAction) -> Unit,
) {
	val listState = rememberLazyListState()
	val textSumUnit = stringResource(R.string.requests_sum_units)

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
	) {
		banks.forEach { bank ->
			item {
				TextTwoLinesView(
					textOne = bank.name,
					textTwo = if (bank.approveBankAgent) {
						bank.sum.toString() + textSumUnit + stringResource(R.string.request_divider) + stringResource(R.string.request_approve_bank_agent)
					} else {
						bank.sum.toString() + textSumUnit
					},
					onClicked = { onActionSent(RequestDetailAction.BankItemClicked) }
				)
			}
		}
	}
}

@Composable
fun BorrowerView(
	borrower: Borrower,
) {
	val listState = rememberLazyListState()

	LazyColumn(
		state = listState,
		modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
	) {
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_full_name_company),
				textTwo = borrower.fullName,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_short_name_company),
				textTwo = borrower.shortName,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_tin),
				textTwo = borrower.tin,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_iec),
				textTwo = borrower.iec,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_legal_address),
				textTwo = borrower.legalAddress,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_actual_address),
				textTwo = borrower.actualAddress,
			)
		}
		item {
			TextTwoLinesView(
				textOne = stringResource(R.string.field_email),
				textTwo = borrower.email,
			)
		}
	}
}