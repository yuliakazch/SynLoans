package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.core.getIndexMonthText
import com.yuliakazachok.synloans.desktop.core.getTextResource
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.*
import com.yuliakazachok.synloans.shared.request.domain.usecase.CancelRequestUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetRequestDetailUseCase

private sealed class RequestDetailUiState {
    object LoadingRequest : RequestDetailUiState()
    data class Content(val request: RequestCommon, val creditOrganisation: Boolean) : RequestDetailUiState()
    object CancelRequest : RequestDetailUiState()
    object Error : RequestDetailUiState()
    object Exit : RequestDetailUiState()
}

class RequestDetailScreen(
    private val requestId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = remember { mutableStateOf<RequestDetailUiState>(RequestDetailUiState.LoadingRequest) }

        val mainScreen = rememberScreen(NavigationScreen.Main)

        val getRequestDetailUseCase = koin.get<GetRequestDetailUseCase>()
        val isCreditOrganisationUseCase = koin.get<IsCreditOrganisationUseCase>()
        val cancelRequestUseCase = koin.get<CancelRequestUseCase>()

        Scaffold(
            topBar = {
                TopBarView(title = TextResources.infoRequest)
            }
        ) {
            when (val state = uiState.value) {
                is RequestDetailUiState.LoadingRequest -> {
                    LoadingView()
                    uiState.value = loadRequestDetail(getRequestDetailUseCase, isCreditOrganisationUseCase, requestId).value
                }

                is RequestDetailUiState.Content -> {
                    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                        item { RequestInfoView(state.request.info) }
                        item { BanksView(state.request.banks) }
                        item { BorrowerView(state.request.borrower) }
                        item {
                            ButtonsRequestDetail(
                                request = state.request.info,
                                creditOrganisation = state.creditOrganisation,
                                onCancelClicked = { uiState.value = RequestDetailUiState.CancelRequest },
                                onBackClicked = { uiState.value = RequestDetailUiState.Exit },
                            )
                        }
                    }
                }

                is RequestDetailUiState.CancelRequest -> {
                    LoadingView()
                    uiState.value = cancelRequest(cancelRequestUseCase, requestId).value
                }

                is RequestDetailUiState.Error -> {
                    ErrorView()
                }

                is RequestDetailUiState.Exit -> {
                    navigator.replaceAll(mainScreen)
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
) {
    Column {
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
        Text(
            text = TextResources.banks,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

        banks.forEach { bank ->
            val textSumUnit = bank.sum.unit.getTextResource()

            TextTwoLinesView(
                textOne = bank.name,
                textTwo = if (bank.approveBankAgent) {
                    bank.sum.value.toString() + textSumUnit + TextResources.divider + TextResources.approveBankAgent
                } else {
                    bank.sum.value.toString() + textSumUnit
                },
                onClicked = { /* TODO */ }
            )
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
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
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
fun ButtonsRequestDetail(
    request: RequestInfo,
    creditOrganisation: Boolean,
    onCancelClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    when {
        request.dateIssue != null -> {
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            ) {
                Text(TextResources.paymentSchedule)
            }
        }

        creditOrganisation -> {
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            ) {
                Text(TextResources.joinSyndicate)
            }
        }

        else -> {
            Button(
                onClick = onCancelClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            ) {
                Text(TextResources.cancelRequest)
            }
        }
    }

    Text(
        text = TextResources.back,
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(bottom = 8.dp).clickable { onBackClicked() },
    )
}

@Composable
private fun loadRequestDetail(
    getRequestDetailUseCase: GetRequestDetailUseCase,
    isCreditOrganisationUseCase: IsCreditOrganisationUseCase,
    requestId: Int,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.LoadingRequest, getRequestDetailUseCase, isCreditOrganisationUseCase) {
        value = try {
            val request = getRequestDetailUseCase(requestId)
            val isCreditOrganisation = isCreditOrganisationUseCase()
            RequestDetailUiState.Content(request, isCreditOrganisation)
        } catch (throwable: Throwable) {
            RequestDetailUiState.Error
        }
    }

@Composable
private fun cancelRequest(
    cancelRequestUseCase: CancelRequestUseCase,
    requestId: Int,
): State<RequestDetailUiState> =
    produceState<RequestDetailUiState>(initialValue = RequestDetailUiState.CancelRequest, cancelRequestUseCase) {
        value = try {
            cancelRequestUseCase(requestId)
            RequestDetailUiState.Exit
        } catch (throwable: Throwable) {
            RequestDetailUiState.Error
        }
    }