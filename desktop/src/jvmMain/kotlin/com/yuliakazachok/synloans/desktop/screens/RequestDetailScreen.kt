package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.checkbox.TextWithCheckboxView
import com.yuliakazachok.synloans.desktop.components.error.ErrorBackView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.EditTextView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesClickableView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.core.getIndexMonthText
import com.yuliakazachok.synloans.desktop.core.getTextResource
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.*
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.ScheduleType
import com.yuliakazachok.synloans.shared.request.domain.usecase.*

private sealed class RequestDetailUiState {
    object LoadingRequest : RequestDetailUiState()
    data class Content(val request: RequestCommon, val creditOrganisation: Boolean, val participantBank: Boolean) : RequestDetailUiState()
    object CancelRequest : RequestDetailUiState()
    object JoinSyndicateRequest : RequestDetailUiState()
    object ExitSyndicateRequest : RequestDetailUiState()
    object StartCreditRequest : RequestDetailUiState()
    object MakePaymentRequest : RequestDetailUiState()
    data class Error(val errorType: ErrorType) : RequestDetailUiState()
}

private sealed class ErrorType {
    object Detail : ErrorType()
    object Cancel : ErrorType()
    object JoinSyndicate : ErrorType()
    object ExitSyndicate : ErrorType()
    object StartCredit : ErrorType()
    object MakePayment : ErrorType()
}

class RequestDetailScreen(
    private val requestId: Int,
    private val participantBank: Boolean,
) : Screen {

    private companion object {
        val DIGIT_REGEX = """([0-9])+""".toRegex()
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = remember { mutableStateOf<RequestDetailUiState>(RequestDetailUiState.LoadingRequest) }

        val mainScreen = rememberScreen(NavigationScreen.Main)

        val getRequestDetailUseCase = koin.get<GetRequestDetailUseCase>()
        val isCreditOrganisationUseCase = koin.get<IsCreditOrganisationUseCase>()
        val cancelRequestUseCase = koin.get<CancelRequestUseCase>()
        val joinSyndicateUseCase = koin.get<JoinSyndicateUseCase>()
        val exitSyndicateUseCase = koin.get<ExitSyndicateUseCase>()
        val startCreditUseCase = koin.get<StartCreditUseCase>()
        val makePaymentUseCase = koin.get<MakePaymentUseCase>()

        val sumJoinSyndicate = remember { mutableStateOf("") }
        val sumMakePayment = remember { mutableStateOf("") }
        val approveBankAgentJoinSyndicate = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopBarView(title = TextResources.infoRequest)
            }
        ) {
            when (val state = uiState.value) {
                is RequestDetailUiState.LoadingRequest -> {
                    LoadingView()
                    uiState.value = loadRequestDetail(getRequestDetailUseCase, isCreditOrganisationUseCase, requestId, participantBank).value
                }

                is RequestDetailUiState.Content -> {
                    LazyColumn {
                        item { RequestInfoView(state.request.info) }
                        item { BanksView(state.request.banks, navigator) }
                        item { BorrowerView(state.request.borrower) }
                        if (state.creditOrganisation && state.request.info.dateIssue == null && !state.participantBank) {
                            item {
                                JoinSyndicateView(
                                    sum = sumJoinSyndicate.value,
                                    approveBankAgent = approveBankAgentJoinSyndicate.value,
                                    onSumChanged = { sumJoinSyndicate.value = it },
                                    onApproveBankAgentChanged = { approveBankAgentJoinSyndicate.value = it },
                                    onJoinClicked = {
                                        if (sumJoinSyndicate.value.matches(DIGIT_REGEX)) {
                                            uiState.value = RequestDetailUiState.JoinSyndicateRequest
                                        }
                                    },
                                )
                            }
                        }
                        if (!state.creditOrganisation && state.request.info.dateIssue != null) {
                            item {
                                MakePaymentView(
                                    sum = sumMakePayment.value,
                                    onSumChanged = { sumMakePayment.value = it },
                                    onMakePaymentClicked = {
                                        if (sumMakePayment.value.matches(DIGIT_REGEX)) {
                                            uiState.value = RequestDetailUiState.MakePaymentRequest
                                        }
                                    },
                                )
                            }
                        }
                        item {
                            ButtonsRequestDetail(
                                request = state.request.info,
                                creditOrganisation = state.creditOrganisation,
                                participantBank = state.participantBank,
                                onExitSyndicateClicked = { uiState.value = RequestDetailUiState.ExitSyndicateRequest },
                                onStartCreditClicked = { uiState.value = RequestDetailUiState.StartCreditRequest },
                                onCancelClicked = { uiState.value = RequestDetailUiState.CancelRequest },
                                onBackClicked = { navigator.replaceAll(mainScreen) },
                                navigator = navigator,
                            )
                        }
                    }
                }

                is RequestDetailUiState.CancelRequest -> {
                    LoadingView()
                    uiState.value = cancelRequest(
                        cancelRequestUseCase = cancelRequestUseCase,
                        requestId = requestId,
                        onMainRoute = { navigator.replaceAll(mainScreen) },
                    ).value
                }

                is RequestDetailUiState.JoinSyndicateRequest -> {
                    LoadingView()
                    uiState.value = joinSyndicate(
                        joinSyndicateUseCase = joinSyndicateUseCase,
                        joinSyndicateInfo = JoinSyndicateInfo(
                            requestId = requestId,
                            sum = sumJoinSyndicate.value.toLong(),
                            approveBankAgent = approveBankAgentJoinSyndicate.value,
                        ),
                        onMainRoute = { navigator.replaceAll(mainScreen) },
                    ).value
                }

                is RequestDetailUiState.ExitSyndicateRequest -> {
                    LoadingView()
                    uiState.value = exitSyndicate(
                        exitSyndicateUseCase = exitSyndicateUseCase,
                        requestId = requestId,
                        onMainRoute = { navigator.replaceAll(mainScreen) },
                    ).value
                }

                is RequestDetailUiState.StartCreditRequest -> {
                    LoadingView()
                    uiState.value = startCredit(
                        startCreditUseCase = startCreditUseCase,
                        requestId = requestId,
                        onMainRoute = { navigator.replaceAll(mainScreen) },
                    ).value
                }

                is RequestDetailUiState.MakePaymentRequest -> {
                    LoadingView()
                    uiState.value = makePayment(
                        makePaymentUseCase = makePaymentUseCase,
                        requestId = requestId,
                        payment = Payment(sumMakePayment.value.toLong()),
                        onUpdateDetail = {
                            sumMakePayment.value = ""
                            uiState.value = RequestDetailUiState.LoadingRequest
                        },
                    ).value
                }

                is RequestDetailUiState.Error -> {
                    ErrorBackView(
                        textBack = TextResources.backMain,
                        onBackClicked = { navigator.replaceAll(mainScreen) },
                        onUpdateClicked = {
                            uiState.value = when (state.errorType) {
                                ErrorType.Cancel -> RequestDetailUiState.CancelRequest
                                ErrorType.Detail -> RequestDetailUiState.LoadingRequest
                                ErrorType.JoinSyndicate -> RequestDetailUiState.JoinSyndicateRequest
                                ErrorType.ExitSyndicate -> RequestDetailUiState.ExitSyndicateRequest
                                ErrorType.StartCredit -> RequestDetailUiState.StartCreditRequest
                                ErrorType.MakePayment -> RequestDetailUiState.MakePaymentRequest
                                else -> throw IllegalArgumentException("${state.errorType} is not support")
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun RequestInfoView(
    request: RequestInfo,
) {
    val monthsTexts = listOf(
        TextResources.monthsOne,
        TextResources.monthsFew,
        TextResources.monthsMany,
    )

    Column(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        TextTwoLinesView(
            textOne = TextResources.status,
            textTwo = when (request.status) {
                StatusRequest.OPEN -> TextResources.statusOpen
                StatusRequest.READY_TO_ISSUE -> TextResources.statusReadyToIssue
                StatusRequest.TRANSFER -> TextResources.statusTransfer
                StatusRequest.ISSUE -> TextResources.statusIssue
                StatusRequest.CLOSE -> TextResources.statusClose
            },
        )
        TextTwoLinesView(
            textOne = TextResources.sum,
            textTwo = request.sum.value.toString() + request.sum.unit.getTextResource(),
        )
        TextTwoLinesView(
            textOne = TextResources.maxRate,
            textTwo = request.maxRate.toString() + TextResources.percent,
        )
        TextTwoLinesView(
            textOne = TextResources.term,
            textTwo = request.term.toString() + monthsTexts[getIndexMonthText(request.term)],
        )
        if (request.dateIssue != null) {
            TextTwoLinesView(
                textOne = TextResources.dateIssue,
                textTwo = request.dateIssue.toString(),
            )
        }
        TextTwoLinesView(
            textOne = TextResources.dateCreate,
            textTwo = request.dateCreate,
        )
    }
}

@Composable
fun BanksView(
    banks: List<BankItem>,
    navigator: Navigator,
) {
    Column {
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
        Text(
            text = TextResources.banks,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

        if (banks.isEmpty()) {
            Text(
                text = TextResources.notBanks,
                fontWeight = FontWeight.Light,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )
        } else {
            banks.forEach { bank ->
                val bankDetailScreen = rememberScreen(NavigationScreen.BankDetail(bank.id))
                val textSumUnit = bank.sum.unit.getTextResource()

                TextTwoLinesClickableView(
                    textOne = bank.name,
                    textTwo = if (bank.approveBankAgent) {
                        bank.sum.value.toString() + textSumUnit + TextResources.divider + TextResources.approveBankAgent
                    } else {
                        bank.sum.value.toString() + textSumUnit
                    },
                    onClicked = { navigator.push(bankDetailScreen) }
                )
            }
        }
    }
}

@Composable
fun BorrowerView(
    borrower: Borrower,
) {
    Column {
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
        Text(
            text = TextResources.borrower,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )

        TextTwoLinesView(
            textOne = TextResources.fullName,
            textTwo = borrower.fullName,
        )
        TextTwoLinesView(
            textOne = TextResources.shortName,
            textTwo = borrower.shortName,
        )
        TextTwoLinesView(
            textOne = TextResources.inn,
            textTwo = borrower.inn,
        )
        TextTwoLinesView(
            textOne = TextResources.kpp,
            textTwo = borrower.kpp,
        )
        TextTwoLinesView(
            textOne = TextResources.legalAddress,
            textTwo = borrower.legalAddress,
        )
        TextTwoLinesView(
            textOne = TextResources.actualAddress,
            textTwo = borrower.actualAddress,
        )
    }
}

@Composable
fun JoinSyndicateView(
    sum: String,
    approveBankAgent: Boolean,
    onSumChanged: (String) -> Unit,
    onApproveBankAgentChanged: (Boolean) -> Unit,
    onJoinClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    ) {
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            text = TextResources.joinSyndicate,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        )
        EditTextView(
            text = sum,
            label = TextResources.sum,
            onTextChange = onSumChanged,
        )
        TextWithCheckboxView(
            text = TextResources.approveBankAgent,
            checked = approveBankAgent,
            onCheckedChange = onApproveBankAgentChanged,
        )
        Button(
            onClick = onJoinClicked,
            modifier = Modifier.padding(bottom = 12.dp),
        ) {
            Text(TextResources.join)
        }
    }
}

@Composable
fun MakePaymentView(
    sum: String,
    onSumChanged: (String) -> Unit,
    onMakePaymentClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    ) {
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            text = TextResources.makePayment,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        )
        EditTextView(
            text = sum,
            label = TextResources.sum,
            onTextChange = onSumChanged,
        )
        Button(
            onClick = onMakePaymentClicked,
        ) {
            Text(TextResources.send)
        }
        Divider(modifier = Modifier.padding(top = 12.dp, bottom = 8.dp))
    }
}

@Composable
fun ButtonsRequestDetail(
    request: RequestInfo,
    creditOrganisation: Boolean,
    participantBank: Boolean,
    onExitSyndicateClicked: () -> Unit,
    onStartCreditClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onBackClicked: () -> Unit,
    navigator: Navigator,
) {
    when {
        request.dateIssue != null -> {
            val plannedPaymentScheduleScreen = rememberScreen(NavigationScreen.PaymentSchedule(request.id, ScheduleType.PLANNED))
            val actualPaymentScheduleScreen = rememberScreen(NavigationScreen.PaymentSchedule(request.id, ScheduleType.ACTUAL))

            Button(
                onClick = { navigator.push(plannedPaymentScheduleScreen) },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 0.dp),
            ) {
                Text(TextResources.paymentSchedule)
            }
            Button(
                onClick = { navigator.push(actualPaymentScheduleScreen) },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            ) {
                Text(TextResources.payments)
            }
        }

        creditOrganisation && participantBank -> {
            Button(
                onClick = onExitSyndicateClicked,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            ) {
                Text(TextResources.exitSyndicate)
            }
        }

        !creditOrganisation -> {
            Button(
                onClick = onCancelClicked,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            ) {
                Text(TextResources.cancelRequest)
            }

            if (request.status == StatusRequest.READY_TO_ISSUE) {
                Button(
                    onClick = onStartCreditClicked,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                ) {
                    Text(TextResources.startCredit)
                }
            }
        }
    }

    Text(
        text = TextResources.backMain,
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(start = 16.dp, bottom = 12.dp).clickable { onBackClicked() },
    )
}

@Composable
private fun loadRequestDetail(
    getRequestDetailUseCase: GetRequestDetailUseCase,
    isCreditOrganisationUseCase: IsCreditOrganisationUseCase,
    requestId: Int,
    participantBank: Boolean,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.LoadingRequest, getRequestDetailUseCase, isCreditOrganisationUseCase) {
        value = try {
            val request = getRequestDetailUseCase(requestId)
            val isCreditOrganisation = isCreditOrganisationUseCase()
            RequestDetailUiState.Content(request, isCreditOrganisation, participantBank)
        } catch (throwable: Throwable) {
            RequestDetailUiState.Error(errorType = ErrorType.Detail)
        }
    }

@Composable
private fun cancelRequest(
    cancelRequestUseCase: CancelRequestUseCase,
    requestId: Int,
    onMainRoute: () -> Unit,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.CancelRequest, cancelRequestUseCase) {
        try {
            cancelRequestUseCase(requestId)
            onMainRoute()
        } catch (throwable: Throwable) {
            value = RequestDetailUiState.Error(errorType = ErrorType.Cancel)
        }
    }

@Composable
private fun joinSyndicate(
    joinSyndicateUseCase: JoinSyndicateUseCase,
    joinSyndicateInfo: JoinSyndicateInfo,
    onMainRoute: () -> Unit,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.JoinSyndicateRequest, joinSyndicateUseCase) {
        try {
            joinSyndicateUseCase(joinSyndicateInfo)
            onMainRoute()
        } catch (throwable: Throwable) {
            value = RequestDetailUiState.Error(errorType = ErrorType.JoinSyndicate)
        }
    }

@Composable
private fun exitSyndicate(
    exitSyndicateUseCase: ExitSyndicateUseCase,
    requestId: Int,
    onMainRoute: () -> Unit,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.ExitSyndicateRequest, exitSyndicateUseCase) {
        try {
            exitSyndicateUseCase(requestId)
            onMainRoute()
        } catch (throwable: Throwable) {
            value = RequestDetailUiState.Error(errorType = ErrorType.ExitSyndicate)
        }
    }

@Composable
private fun startCredit(
    startCreditUseCase: StartCreditUseCase,
    requestId: Int,
    onMainRoute: () -> Unit,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.StartCreditRequest, startCreditUseCase) {
        try {
            startCreditUseCase(requestId)
            onMainRoute()
        } catch (throwable: Throwable) {
            value = RequestDetailUiState.Error(errorType = ErrorType.StartCredit)
        }
    }

@Composable
private fun makePayment(
    makePaymentUseCase: MakePaymentUseCase,
    requestId: Int,
    payment: Payment,
    onUpdateDetail: () -> Unit,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.MakePaymentRequest, makePaymentUseCase) {
        try {
            makePaymentUseCase(requestId, payment)
            onUpdateDetail()
        } catch (throwable: Throwable) {
            value = RequestDetailUiState.Error(errorType = ErrorType.MakePayment)
        }
    }