package com.yuliakazachok.synloans.android.features.requests.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.yuliakazachok.synloans.android.components.topbar.TopBarView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsAction
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsEffect
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsState
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequest
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.sum.SumUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun BankRequestsScreen(
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
            TopBarView(
                title = stringResource(R.string.requests_title),
            )
        }
    ) {
        when {
            state.loading -> LoadingView()

            state.bankRequests == null -> ErrorView()

            else -> BankRequestsView(state.bankRequests, onActionSent)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BankRequestsView(
    data: BankRequests,
    onActionSent: (action: RequestsAction) -> Unit,
) {
    val tabData = listOf(
        0 to R.string.requests_own,
        1 to R.string.requests_other,
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
                            fontSize = 16.sp
                        )
                    },
                )
            }
        }

        HorizontalPager(
            count = 2,
            state = pagerState,
        ) { index ->
            when (index) {
                0 -> ListRequestsView(data.own, onActionSent)

                1 -> ListRequestsView(data.other, onActionSent)
            }
        }
    }
}

@Composable
fun ListRequestsView(
    data: List<BankRequest>,
    onActionSent: (action: RequestsAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth(),
    ) {
        data.forEach { request ->
            item {
                val textSumUnit = when (request.sum.unit) {
                    SumUnit.BILLION -> stringResource(R.string.requests_sum_billion)
                    SumUnit.MILLION -> stringResource(R.string.requests_sum_million)
                    SumUnit.THOUSAND -> stringResource(R.string.requests_sum_thousand)
                }

                TextTwoLinesView(
                    textOne = request.name,
                    textTwo = request.sum.value.toString() + textSumUnit,
                    onClicked = { onActionSent(RequestsAction.RequestClicked(request.id)) }
                )
            }
        }
    }
}