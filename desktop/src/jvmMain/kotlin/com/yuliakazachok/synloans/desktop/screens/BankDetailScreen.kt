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
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
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
        val uiState = remember { mutableStateOf<BankDetailUiState>(BankDetailUiState.LoadingDetail) }

        val getBankDetailUseCase = koin.get<GetBankDetailUseCase>()

        Scaffold(
            topBar = {
                TopBarView(title = TextResources.infoBank)
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
                        onBackClicked = { navigator.pop() },
                    )
                }

                is BankDetailUiState.Error -> {
                    ErrorView()
                }
            }
        }
    }
}

@Composable
fun BankDetailView(
    bank: Bank,
    onBackClicked: () -> Unit,
) {
    LazyColumn(
        //horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 12.dp)
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