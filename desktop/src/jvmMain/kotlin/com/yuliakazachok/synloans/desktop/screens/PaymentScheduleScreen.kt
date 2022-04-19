package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorBackView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextThreeLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetPlannedScheduleUseCase

private sealed class PaymentScheduleUiState {
    object LoadingSchedule : PaymentScheduleUiState()
    data class Content(val payments: List<PaymentInfo>) : PaymentScheduleUiState()
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
                    uiState.value = loadPlannedSchedule(getScheduleUseCase, requestId).value
                }

                is PaymentScheduleUiState.Content -> {
                    PlannedPaymentScheduleView(
                        payments = state.payments,
                        onBackClicked = { navigator.pop() },
                    )
                }

                is PaymentScheduleUiState.Error -> {
                    ErrorBackView(
                        textBack = TextResources.backRequest,
                        onBackClicked = { navigator.pop() },
                        onUpdateClicked = { uiState.value = PaymentScheduleUiState.LoadingSchedule },
                    )
                }
            }
        }
    }
}

@Composable
fun PlannedPaymentScheduleView(
    payments: List<PaymentInfo>,
    onBackClicked: () -> Unit,
) {
    val textSumUnit = TextResources.unitSum
    val textDatePayment = TextResources.datePayment
    val textPrincipal = TextResources.principal
    val textPercents = TextResources.percents

    LazyColumn(
        modifier = Modifier.padding(top = 12.dp),
    ) {
        payments.forEach { payment ->
            item {
                TextThreeLinesView(
                    textOne = textDatePayment + payment.date,
                    textTwo = textPrincipal + payment.principal.toString() + textSumUnit,
                    textThree = textPercents + payment.percent + textSumUnit,
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
private fun loadPlannedSchedule(
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