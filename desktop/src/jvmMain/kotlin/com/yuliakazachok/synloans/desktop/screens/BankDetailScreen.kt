package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorBackView
import com.yuliakazachok.synloans.desktop.components.navigation.SurfaceNavigation
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank
import com.yuliakazachok.synloans.shared.bank.domain.usecase.GetBankDetailUseCase

private sealed class BankDetailUiState {
    object LoadingDetail : BankDetailUiState()
    data class Content(val bank: Bank) : BankDetailUiState()
    object Error : BankDetailUiState()
}

class BankDetailScreen(
    private val bankId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val requestsScreen = rememberScreen(NavigationScreen.Requests)
        val profileScreen = rememberScreen(NavigationScreen.ProfileInfo)

        SurfaceNavigation(
            mainContent = { BankDetailContent(navigator, bankId) },
            selectedRequests = true,
            onClickedRequests = { navigator.replaceAll(requestsScreen) },
            onClickedProfile = { navigator.replaceAll(profileScreen) },
        )
    }
}

@Composable
fun BankDetailContent(
    navigator: Navigator,
    bankId: Int,
) {
    val uiState = remember { mutableStateOf<BankDetailUiState>(BankDetailUiState.LoadingDetail) }

    val getBankDetailUseCase = koin.get<GetBankDetailUseCase>()

    Scaffold(
        topBar = {
            TopBarBackView(
                title = TextResources.infoBank,
                onIconClicked = { navigator.pop() },
            )
        }
    ) {
        when (val state = uiState.value) {
            is BankDetailUiState.LoadingDetail -> {
                LoadingView()
                uiState.value = loadBankDetail(getBankDetailUseCase, bankId).value
            }

            is BankDetailUiState.Content -> {
                BankDetailView(
                    bank = state.bank,
                )
            }

            is BankDetailUiState.Error -> {
                ErrorBackView(
                    textBack = TextResources.backRequest,
                    onBackClicked = { navigator.pop() },
                    onUpdateClicked = { uiState.value = BankDetailUiState.LoadingDetail },
                )
            }
        }
    }
}

@Composable
fun BankDetailView(
    bank: Bank,
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp),
    ) {
        item {
            TextTwoLinesView(
                textOne = TextResources.fullName,
                textTwo = bank.fullName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.shortName,
                textTwo = bank.shortName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.inn,
                textTwo = bank.inn,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.kpp,
                textTwo = bank.kpp,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.legalAddress,
                textTwo = bank.legalAddress,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.actualAddress,
                textTwo = bank.actualAddress,
            )
        }
    }
}

@Composable
private fun loadBankDetail(
    getBankDetailUseCase: GetBankDetailUseCase,
    bankId: Int,
): State<BankDetailUiState> =
    produceState<BankDetailUiState>(initialValue = BankDetailUiState.LoadingDetail, getBankDetailUseCase) {
        value = try {
            val detail = getBankDetailUseCase(bankId)
            BankDetailUiState.Content(detail)
        } catch (throwable: Throwable) {
            BankDetailUiState.Error
        }
    }