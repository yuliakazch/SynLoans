package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarTwoEndIconView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.core.getTextResource
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequest
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBankRequestsUseCase
import com.yuliakazachok.synloans.shared.request.domain.usecase.GetBorrowRequestsUseCase
import com.yuliakazachok.synloans.shared.token.domain.usecase.ClearTokenUseCase
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase

sealed class MainUiState {
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
                    ProfileView(state.profile, state.bankRequests, state.borrowRequests)
                }

                is MainUiState.Error -> {
                    ErrorView()
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
            Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
            Text(
                text = TextResources.requests,
                fontWeight = SemiBold,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
        item {
            if (profile.creditOrganisation) {
                bankRequests?.let { BankRequestsView(it) }
            } else {
                borrowRequests?.let { BorrowerRequestsView(it) }
            }
        }
    }
}

@Composable
private fun BankRequestsView(
    data: BankRequests,
) {
    Column {
        ListRequestView(
            headerText = TextResources.ownRequests,
            requests = data.own,
        )
        ListRequestView(
            headerText = TextResources.otherRequests,
            requests = data.other,
        )
    }
}

@Composable
fun ListRequestView(
    headerText: String,
    requests: List<BankRequest>,
) {
    Text(
        text = headerText,
        fontWeight = Medium,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
    )
    if (requests.isNotEmpty()) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
        ) {
            requests.forEach { request ->
                TextTwoLinesView(
                    textOne = request.name,
                    textTwo = request.sum.value.toString() + request.sum.unit.getTextResource(),
                    onClicked = { /* TODO */ }
                )
            }
        }
    } else {
        Text(
            text = TextResources.emptyActiveRequests,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
fun BorrowerRequestsView(
    requests: List<RequestCommon>,
) {
    Column(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        requests.forEach { request ->
            TextTwoLinesView(
                textOne = if (request.info.dateIssue.isNullOrEmpty()) {
                    TextResources.dateCreate + request.info.dateCreate
                } else {
                    TextResources.dateIssue + request.info.dateIssue
                },
                textTwo = request.info.sum.value.toString() + request.info.sum.unit.getTextResource(),
                onClicked = { /* TODO */ }
            )
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