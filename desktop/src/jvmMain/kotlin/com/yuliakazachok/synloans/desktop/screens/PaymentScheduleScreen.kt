package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetPlannedScheduleUseCase

private sealed class PaymentScheduleUiState {
    object LoadingSchedule : PaymentScheduleUiState()
    data class Content(val payments: List<Payment>) : PaymentScheduleUiState()
    object Error : PaymentScheduleUiState()
}

class PaymentScheduleScreen(
    private val requestId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = remember { mutableStateOf<PaymentScheduleUiState>(PaymentScheduleUiState.LoadingSchedule) }

        val getScheduleUseCase = koin.get<GetPlannedScheduleUseCase>()

        Scaffold(
            topBar = { TopBarView(title = TextResources.paymentSchedule) },
        ) {
            when (val state = uiState.value) {
                is PaymentScheduleUiState.LoadingSchedule -> {
                    LoadingView()
                    uiState.value = loadSchedule(getScheduleUseCase, requestId).value
                }

                is PaymentScheduleUiState.Content -> {
                    PaymentScheduleView(
                        payments = state.payments,
                        onBackClicked = { navigator.pop() },
                    )
                }

                is PaymentScheduleUiState.Error -> {
                    ErrorView()
                }
            }
        }
    }
}

@Composable
fun PaymentScheduleView(
    payments: List<Payment>,
    onBackClicked: () -> Unit,
) {
    val listState = rememberLazyListState()
    val textSumUnit = TextResources.thousandSum
    val textPaid = TextResources.paymentPaid
    val textNotPaid = TextResources.paymentNotPaid

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(top = 12.dp),
    ) {
        payments.forEach { payment ->
            item {
                TextTwoLinesView(
                    textOne = if (payment.paid) {
                        textPaid
                    } else {
                        textNotPaid
                    },
                    textTwo = payment.sum.toString() + textSumUnit + TextResources.divider + payment.date,
                )
            }
        }
        item {
            Text(
                text = TextResources.backRequest,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp).clickable { onBackClicked() },
            )
        }
    }
}

@Composable
private fun loadSchedule(
    getScheduleUseCase: GetPlannedScheduleUseCase,
    requestId: Int,
): State<PaymentScheduleUiState> =
    produceState<PaymentScheduleUiState>(initialValue = PaymentScheduleUiState.LoadingSchedule, getScheduleUseCase) {
        value = try {
            val payments = getScheduleUseCase(requestId)
            PaymentScheduleUiState.Content(payments)
        } catch (throwable: Throwable) {
            PaymentScheduleUiState.Error
        }
    }