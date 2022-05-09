package com.yuliakazachok.synloans.desktop.screens

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
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.navigation.SurfaceNavigation
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesClickableView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.core.getTextResource
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBankRequestsUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBorrowRequestsUseCase

private sealed class RequestsUiState {
    object Loading : RequestsUiState()
    data class BankContent(val bankRequests: BankRequests) : RequestsUiState()
    data class BorrowContent(val borrowRequests: List<RequestCommon>) : RequestsUiState()
    object Error : RequestsUiState()
}

class RequestsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val profileScreen = rememberScreen(NavigationScreen.ProfileInfo)

        SurfaceNavigation(
            mainContent = { RequestsContent(navigator) },
            selectedRequests = true,
            onClickedProfile = { navigator.replaceAll(profileScreen) },
        )
    }
}

@Composable
fun RequestsContent(navigator: Navigator) {
    val uiState = remember { mutableStateOf<RequestsUiState>(RequestsUiState.Loading) }

    val requestCreateScreen = rememberScreen(NavigationScreen.RequestCreate)

    val getBankRequestsUseCase = koin.get<GetBankRequestsUseCase>()
    val getBorrowRequestsUseCase = koin.get<GetBorrowRequestsUseCase>()
    val isCreditOrganisationUseCase = koin.get<IsCreditOrganisationUseCase>()

    Scaffold(
        topBar = {
            TopBarView(
                title = TextResources.requests,
            )
        }
    ) {
        when (val state = uiState.value) {
            is RequestsUiState.Loading -> {
                LoadingView()
                uiState.value = loadRequests(getBankRequestsUseCase, getBorrowRequestsUseCase, isCreditOrganisationUseCase).value
            }

            is RequestsUiState.BankContent -> {
                BankRequestsView(
                    requests = state.bankRequests,
                    navigator = navigator,
                )
            }

            is RequestsUiState.BorrowContent -> {
                BorrowerRequestsView(
                    requests = state.borrowRequests,
                    navigator = navigator,
                    onCreateClicked = { navigator.push(requestCreateScreen) },
                )
            }

            is RequestsUiState.Error -> {
                ErrorView(
                    onUpdateClicked = { uiState.value = RequestsUiState.Loading },
                )
            }
        }
    }
}

@Composable
private fun BankRequestsView(
    requests: BankRequests,
    navigator: Navigator,
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        item {
            ListRequestView(
                headerText = TextResources.ownRequests,
                requests = requests.own,
                participantBank = true,
                navigator = navigator,
            )
        }
        item {
            Divider(modifier = Modifier.padding(all = 16.dp))
            ListRequestView(
                headerText = TextResources.otherRequests,
                requests = requests.other,
                participantBank = false,
                navigator = navigator,
            )
        }
    }
}

@Composable
fun ListRequestView(
    headerText: String,
    requests: List<RequestCommon>,
    participantBank: Boolean,
    navigator: Navigator,
) {
    Text(
        text = headerText,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
    )
    if (requests.isNotEmpty()) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
        ) {
            requests.forEach { request ->
                val requestDetailScreen = rememberScreen(NavigationScreen.RequestDetail(request.info.id, participantBank = participantBank))
                TextTwoLinesClickableView(
                    textOne = request.borrower.shortName,
                    textTwo = request.info.sum.value.toString() + request.info.sum.unit.getTextResource(),
                    onClicked = { navigator.push(requestDetailScreen) },
                )
            }
        }
    } else {
        Text(
            text = TextResources.emptyRequests,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
fun BorrowerRequestsView(
    requests: List<RequestCommon>,
    navigator: Navigator,
    onCreateClicked: () -> Unit,
) {
    Column {
        requests.forEach { request ->
            val requestDetailScreen = rememberScreen(NavigationScreen.RequestDetail(request.info.id, participantBank = false))
            TextTwoLinesClickableView(
                textOne = if (request.info.dateIssue.isNullOrEmpty()) {
                    TextResources.dateCreate + request.info.dateCreate
                } else {
                    TextResources.dateIssue + request.info.dateIssue
                },
                textTwo = request.info.sum.value.toString() + request.info.sum.unit.getTextResource(),
                onClicked = { navigator.push(requestDetailScreen) },
            )
        }

        Button(
            onClick = onCreateClicked,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {
            Text(TextResources.createRequest)
        }
    }
}

@Composable
private fun loadRequests(
    getBankRequestsUseCase: GetBankRequestsUseCase,
    getBorrowRequestsUseCase: GetBorrowRequestsUseCase,
    isCreditOrganisationUseCase: IsCreditOrganisationUseCase,
): State<RequestsUiState> =
    produceState<RequestsUiState>(initialValue = RequestsUiState.Loading, getBankRequestsUseCase, getBorrowRequestsUseCase, isCreditOrganisationUseCase) {
        value = try {
            if (isCreditOrganisationUseCase()) {
                RequestsUiState.BankContent(bankRequests = getBankRequestsUseCase())
            } else {
                RequestsUiState.BorrowContent(borrowRequests = getBorrowRequestsUseCase())
            }
        } catch (throwable: Throwable) {
            RequestsUiState.Error
        }
    }