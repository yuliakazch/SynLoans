package com.yuliakazachok.synloans.android.features.requestdetail.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.yuliakazachok.synloans.android.components.text.TextFullScreenView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesClickableView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.core.getIndexMonthText
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.ScheduleType.*
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailAction
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailEffect
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailState
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.*
import com.yuliakazachok.synloans.shared.request.domain.entity.sum.SumUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
                is RequestDetailEffect.Error ->
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
            state.loading -> LoadingView()

            state.request == null -> ErrorView(
                onUpdateClicked = { onActionSent(RequestDetailAction.RepeatClicked) },
            )

            else -> RequestDetailView(state.request, state.creditOrganisation, onActionSent)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RequestDetailView(
    request: RequestCommon,
    creditOrganisation: Boolean,
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
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = MaterialTheme.colors.background,
            edgePadding = 0.dp,
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
                            fontSize = 18.sp,
                        )
                    },
                )
            }
        }

        HorizontalPager(
            count = tabData.size,
            state = pagerState,
        ) { index ->
            when (index) {
                0 -> RequestInfoView(request.info, creditOrganisation, onActionSent)

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
    creditOrganisation: Boolean,
    onActionSent: (action: RequestDetailAction) -> Unit,
) {
    val monthsTexts = stringArrayResource(R.array.request_months)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.request_status),
                textTwo = when (request.status) {
                    StatusRequest.OPEN -> stringResource(R.string.request_status_open)
                    StatusRequest.TRANSFER -> stringResource(R.string.request_status_transfer)
                    StatusRequest.ISSUE -> stringResource(R.string.request_status_issue)
                    StatusRequest.CLOSE -> stringResource(R.string.request_status_close)
                },
            )
        }
        item {
            val textSumUnit = when (request.sum.unit) {
                SumUnit.BILLION -> stringResource(R.string.requests_sum_billion)
                SumUnit.MILLION -> stringResource(R.string.requests_sum_million)
                SumUnit.THOUSAND -> stringResource(R.string.requests_sum_thousand)
            }

            TextTwoLinesView(
                textOne = stringResource(R.string.request_sum),
                textTwo = request.sum.value.toString() + textSumUnit,
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
                textTwo = request.term.toString() + monthsTexts[getIndexMonthText(request.term)],
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
                    onClick = { onActionSent(RequestDetailAction.PaymentScheduleClicked(scheduleType = PLANNED)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Text(stringResource(R.string.request_payment_schedule))
                }

                Button(
                    onClick = { onActionSent(RequestDetailAction.PaymentScheduleClicked(scheduleType = ACTUAL)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(stringResource(R.string.payments))
                }

                if (!creditOrganisation) {
                    Button(
                        onClick = { onActionSent(RequestDetailAction.MakePaymentClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        Text(stringResource(R.string.make_payment))
                    }
                }
            }
        } else {
            if (creditOrganisation) {
                item {
                    Button(
                        onClick = { onActionSent(RequestDetailAction.JoinSyndicateClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        Text(stringResource(R.string.request_join_syndicate))
                    }
                }
                item {
                    Button(
                        onClick = { onActionSent(RequestDetailAction.ExitSyndicateClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    ) {
                        Text(stringResource(R.string.request_exit_syndicate))
                    }
                }
            } else {
                item {
                    Button(
                        onClick = { onActionSent(RequestDetailAction.CancelRequestClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        Text(stringResource(R.string.request_cancel))
                    }
                }
            }
        }
    }
}

@Composable
fun BanksView(
    banks: List<BankItem>,
    onActionSent: (action: RequestDetailAction) -> Unit,
) {
    if (banks.isEmpty()) {
        TextFullScreenView(stringResource(R.string.request_not_banks))
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
        ) {
            banks.forEach { bank ->
                item {
                    val textSumUnit = when (bank.sum.unit) {
                        SumUnit.BILLION -> stringResource(R.string.requests_sum_billion)
                        SumUnit.MILLION -> stringResource(R.string.requests_sum_million)
                        SumUnit.THOUSAND -> stringResource(R.string.requests_sum_thousand)
                    }

                    TextTwoLinesClickableView(
                        textOne = bank.name,
                        textTwo = if (bank.approveBankAgent) {
                            bank.sum.value.toString() + textSumUnit + stringResource(R.string.request_divider) + stringResource(R.string.request_approve_bank_agent)
                        } else {
                            bank.sum.value.toString() + textSumUnit
                        },
                        onClicked = { onActionSent(RequestDetailAction.BankItemClicked(bank.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun BorrowerView(
    borrower: Borrower,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
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
                textTwo = borrower.inn,
            )
        }
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_iec),
                textTwo = borrower.kpp,
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
    }
}