package com.yuliakazachok.synloans.android.features.bankdetail.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.error.ErrorView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.android.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.android.core.LAUNCH_LISTEN_FOR_EFFECTS
import com.yuliakazachok.synloans.android.features.bankdetail.presentation.BankDetailAction
import com.yuliakazachok.synloans.android.features.bankdetail.presentation.BankDetailEffect
import com.yuliakazachok.synloans.android.features.bankdetail.presentation.BankDetailState
import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun BankDetailScreen(
    state: BankDetailState,
    effectFlow: Flow<BankDetailEffect>?,
    onActionSent: (action: BankDetailAction) -> Unit,
    onNavigationRequested: (navigationEffect: BankDetailEffect.Navigation) -> Unit
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is BankDetailEffect.Navigation ->
                    onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        topBar = {
            TopBarBackView(
                title = stringResource(R.string.bank_detail_title),
                onIconClicked = { onActionSent(BankDetailAction.BackClicked) },
            )
        }
    ) {
        when {
            state.loading -> LoadingView()

            state.data == null -> ErrorView()

            else -> BankDetailView(state.data)
        }
    }
}

@Composable
fun BankDetailView(
    data: Bank
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
    ) {
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_full_name_company),
                textTwo = data.fullName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_short_name_company),
                textTwo = data.shortName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_tin),
                textTwo = data.inn,
            )
        }
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_iec),
                textTwo = data.kpp,
            )
        }
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_legal_address),
                textTwo = data.legalAddress,
            )
        }
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_actual_address),
                textTwo = data.actualAddress,
            )
        }
        item {
            TextTwoLinesView(
                textOne = stringResource(R.string.field_email),
                textTwo = data.email,
            )
        }
    }
}