package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorBackView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.EditTextView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.usecase.CreateRequestUseCase

private sealed class RequestCreateUiState {
    object Content : RequestCreateUiState()
    object CreatingRequest : RequestCreateUiState()
    object Error : RequestCreateUiState()
}

class RequestCreateScreen : Screen {

    private companion object {
        val DIGIT_REGEX = """([0-9])+""".toRegex()
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = remember { mutableStateOf<RequestCreateUiState>(RequestCreateUiState.Content) }

        val requestsScreen = rememberScreen(NavigationScreen.Requests)

        val createRequestUseCase = koin.get<CreateRequestUseCase>()

        val sum = remember { mutableStateOf("") }
        val rate = remember { mutableStateOf("") }
        val term = remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopBarView(title = TextResources.creationRequest)
            }
        ) {
            when (uiState.value) {
                is RequestCreateUiState.Content -> {
                    RequestCreateView(
                        sum = sum.value,
                        rate = rate.value,
                        term = term.value,
                        onSumChanged = { sum.value = it },
                        onRateChanged = { rate.value = it },
                        onTermChanged = { term.value = it },
                        onSendClicked = {
                            if (sum.value.matches(DIGIT_REGEX) && rate.value.matches(DIGIT_REGEX) && term.value.matches(DIGIT_REGEX)) {
                                uiState.value = RequestCreateUiState.CreatingRequest
                            }
                        },
                        onBackClicked = { navigator.replaceAll(requestsScreen) },
                    )
                }

                is RequestCreateUiState.CreatingRequest -> {
                    LoadingView()
                    uiState.value = createRequest(
                        createRequestUseCase = createRequestUseCase,
                        createRequestInfo = CreateRequestInfo(
                            sum = sum.value.toLong(),
                            maxRate = rate.value.toFloat(),
                            term = term.value.toInt(),
                        ),
                        onMainRoute = { navigator.replaceAll(requestsScreen) },
                    ).value
                }

                is RequestCreateUiState.Error -> {
                    ErrorBackView(
                        textBack = TextResources.backMain,
                        onBackClicked = { navigator.replaceAll(requestsScreen) },
                        onUpdateClicked = { uiState.value = RequestCreateUiState.CreatingRequest },
                    )
                }
            }
        }
    }
}

@Composable
fun RequestCreateView(
    sum: String,
    rate: String,
    term: String,
    onSumChanged: (String) -> Unit,
    onRateChanged: (String) -> Unit,
    onTermChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
    ) {
        item {
            EditTextView(
                text = sum,
                label = TextResources.sum,
                onTextChange = onSumChanged,
            )
        }
        item {
            EditTextView(
                text = rate,
                label = TextResources.maxRate,
                onTextChange = onRateChanged,
            )
        }
        item {
            EditTextView(
                text = term,
                label = TextResources.term,
                onTextChange = onTermChanged,
            )
        }
        item {
            Button(
                onClick = onSendClicked,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
            ) {
                Text(TextResources.sendRequest)
            }
            Text(
                text = TextResources.backMain,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 8.dp).clickable { onBackClicked() },
            )
        }
    }
}

@Composable
private fun createRequest(
    createRequestUseCase: CreateRequestUseCase,
    createRequestInfo: CreateRequestInfo,
    onMainRoute: () -> Unit,
): State<RequestCreateUiState> =
    produceState<RequestCreateUiState>(initialValue = RequestCreateUiState.CreatingRequest, createRequestUseCase) {
        try {
            createRequestUseCase(createRequestInfo)
            onMainRoute()
        } catch (throwable: Throwable) {
            value = RequestCreateUiState.Error
        }
    }