package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesClickableView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarTwoEndIconView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.core.getTextResource
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBankRequestsUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBorrowRequestsUseCase
import com.yuliakazachok.synloans.shared.token.domain.usecase.ClearTokenUseCase
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase

private sealed class MainUiState {
    data class Content(val profile: Profile, val bankRequests: BankRequests? = null, val borrowRequests: List<RequestCommon>? = null) : MainUiState()
    object SendingRequest : MainUiState()
    object Error : MainUiState()
}

class MainScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = remember { mutableStateOf<MainUiState>(MainUiState.SendingRequest) }

        val signInScreen = rememberScreen(NavigationScreen.SignIn)
        val requestCreateScreen = rememberScreen(NavigationScreen.RequestCreate)
        var editProfileScreen: Screen? = null

        val getBankRequestsUseCase = koin.get<GetBankRequestsUseCase>()
        val getBorrowRequestsUseCase = koin.get<GetBorrowRequestsUseCase>()
        val getProfileUseCase = koin.get<GetProfileUseCase>()
        val clearTokenUseCase = koin.get<ClearTokenUseCase>()

        Scaffold(
            topBar = {
                TopBarTwoEndIconView(
                    title = TextResources.profile,
                    iconOne = Icons.Filled.Edit,
                    iconTwo = Icons.Filled.ExitToApp,
                    onOneIconClicked = { editProfileScreen?.let { navigator.push(it) } },
                    onTwoIconClicked = {
                        clearTokenUseCase()
                        navigator.replaceAll(signInScreen)
                    },
                )
            }
        ) {
            when (val state = uiState.value) {
                is MainUiState.SendingRequest -> {
                    LoadingView()
                    uiState.value = loadData(getProfileUseCase, getBankRequestsUseCase, getBorrowRequestsUseCase).value
                }

                is MainUiState.Content -> {
                    editProfileScreen = rememberScreen(NavigationScreen.EditProfile(state.profile))
                    ProfileView(
                        profile = state.profile,
                        bankRequests = state.bankRequests,
                        borrowRequests = state.borrowRequests,
                        navigator = navigator,
                        onCreateClicked = { navigator.push(requestCreateScreen) },
                    )
                }

                is MainUiState.Error -> {
                    ErrorView(
                        onUpdateClicked = { uiState.value = MainUiState.SendingRequest },
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileView(
    profile: Profile,
    bankRequests: BankRequests?,
    borrowRequests: List<RequestCommon>?,
    navigator: Navigator,
    onCreateClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        item {
            TextTwoLinesView(
                textOne = TextResources.fullName,
                textTwo = profile.fullName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.shortName,
                textTwo = profile.shortName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.inn,
                textTwo = profile.inn,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.kpp,
                textTwo = profile.kpp,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.legalAddress,
                textTwo = profile.legalAddress,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.actualAddress,
                textTwo = profile.actualAddress,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.email,
                textTwo = profile.email,
            )
        }
        item {
            if (profile.creditOrganisation) {
                bankRequests?.let { BankRequestsView(data = it, navigator = navigator) }
            } else {
                borrowRequests?.let { BorrowerRequestsView(requests = it, navigator = navigator, onCreateClicked = onCreateClicked) }
            }
        }
    }
}

@Composable
private fun BankRequestsView(
    data: BankRequests,
    navigator: Navigator,
) {
    ListRequestView(
        headerText = TextResources.ownRequests,
        requests = data.own,
        participantBank = true,
        navigator = navigator,
    )
    ListRequestView(
        headerText = TextResources.otherRequests,
        requests = data.other,
        participantBank = false,
        navigator = navigator,
    )
}

@Composable
fun ListRequestView(
    headerText: String,
    requests: List<RequestCommon>,
    participantBank: Boolean,
    navigator: Navigator,
) {
    Divider(modifier = Modifier.padding(all = 16.dp))
    Text(
        text = headerText,
        fontWeight = SemiBold,
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
        Divider(modifier = Modifier.padding(all = 16.dp))
        Text(
            text = TextResources.requests,
            fontWeight = SemiBold,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )
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
private fun loadData(
    getProfileUseCase: GetProfileUseCase,
    getBankRequestsUseCase: GetBankRequestsUseCase,
    getBorrowRequestsUseCase: GetBorrowRequestsUseCase,
): State<MainUiState> =
    produceState<MainUiState>(initialValue = MainUiState.SendingRequest, getProfileUseCase, getBankRequestsUseCase, getBorrowRequestsUseCase) {
        value = try {
            val profile = getProfileUseCase()
            if (profile.creditOrganisation) {
                MainUiState.Content(profile, bankRequests = getBankRequestsUseCase())
            } else {
                MainUiState.Content(profile, borrowRequests = getBorrowRequestsUseCase())
            }
        } catch (throwable: Throwable) {
            MainUiState.Error
        }
    }