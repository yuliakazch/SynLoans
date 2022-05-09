package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorBackView
import com.yuliakazachok.synloans.desktop.components.navigation.SurfaceNavigation
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextThreeLinesView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.ScheduleType
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetActualScheduleUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetPlannedScheduleUseCase

private sealed class PaymentScheduleUiState {
    object LoadingSchedule : PaymentScheduleUiState()
    data class Content(val plannedPayments: List<PaymentInfo>? = null, val actualPayments: List<Payment>? = null) : PaymentScheduleUiState()
    object Error : PaymentScheduleUiState()
}

class PaymentScheduleScreen(
    private val requestId: Int,
    private val scheduleType: ScheduleType,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val requestsScreen = rememberScreen(NavigationScreen.Requests)
        val profileScreen = rememberScreen(NavigationScreen.ProfileInfo)

        SurfaceNavigation(
            mainContent = { PaymentScheduleContent(navigator, requestId, scheduleType) },
            selectedRequests = true,
            onClickedRequests = { navigator.replaceAll(requestsScreen) },
            onClickedProfile = { navigator.replaceAll(profileScreen) },
        )
    }
}

@Composable
fun PaymentScheduleContent(
    navigator: Navigator,
    requestId: Int,
    scheduleType: ScheduleType,
) {
    val uiState = remember { mutableStateOf<PaymentScheduleUiState>(PaymentScheduleUiState.LoadingSchedule) }

    val getPlannedScheduleUseCase = koin.get<GetPlannedScheduleUseCase>()
    val getActualScheduleUseCase = koin.get<GetActualScheduleUseCase>()

    Scaffold(
        topBar = {
            TopBarBackView(
                title = TextResources.payments,
                onIconClicked = { navigator.pop() },
            )
        },
    ) {
        when (val state = uiState.value) {
            is PaymentScheduleUiState.LoadingSchedule -> {
                LoadingView()
                uiState.value = when (scheduleType) {
                    ScheduleType.PLANNED -> {
                        loadPlannedSchedule(getPlannedScheduleUseCase, requestId).value
                    }
                    ScheduleType.ACTUAL -> {
                        loadActualSchedule(getActualScheduleUseCase, requestId).value
                    }
                }
            }

            is PaymentScheduleUiState.Content -> {
                when {
                    state.plannedPayments != null -> {
                        PlannedPaymentScheduleView(payments = state.plannedPayments)
                    }

                    state.actualPayments != null -> {
                        ActualPaymentScheduleView(payments = state.actualPayments)
                    }
                }
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

@Composable
fun PlannedPaymentScheduleView(
    payments: List<PaymentInfo>,
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
    }
}

@Composable
fun ActualPaymentScheduleView(
    payments: List<Payment>,
) {
    val textSumUnit = TextResources.unitSum
    val textDatePayment = TextResources.datePayment
    val textSum = TextResources.sumPayment

    LazyColumn(
        modifier = Modifier.padding(top = 12.dp),
    ) {
        if (payments.isEmpty()) {
            item {
                Text(
                    text = TextResources.notPayments,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                )
            }
        } else {
            payments.forEach { payment ->
                item {
                    TextTwoLinesView(
                        textOne = textDatePayment + payment.date,
                        textTwo = textSum + payment.payment.toString() + textSumUnit,
                    )
                }
            }
        }
    }
}

@Composable
private fun loadPlannedSchedule(
    getPlannedScheduleUseCase: GetPlannedScheduleUseCase,
    requestId: Int,
): State<PaymentScheduleUiState> =
    produceState<PaymentScheduleUiState>(initialValue = PaymentScheduleUiState.LoadingSchedule, getPlannedScheduleUseCase) {
        value = try {
            val payments = getPlannedScheduleUseCase(requestId)
            PaymentScheduleUiState.Content(plannedPayments = payments)
        } catch (throwable: Throwable) {
            PaymentScheduleUiState.Error
        }
    }

@Composable
private fun loadActualSchedule(
    getActualScheduleUseCase: GetActualScheduleUseCase,
    requestId: Int,
): State<PaymentScheduleUiState> =
    produceState<PaymentScheduleUiState>(initialValue = PaymentScheduleUiState.LoadingSchedule, getActualScheduleUseCase) {
        value = try {
            val payments = getActualScheduleUseCase(requestId)
            PaymentScheduleUiState.Content(actualPayments = payments)
        } catch (throwable: Throwable) {
            PaymentScheduleUiState.Error
        }
    }