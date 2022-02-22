package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.checkbox.TextWithCheckboxView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.EditLargeTextView
import com.yuliakazachok.synloans.desktop.components.text.EditPasswordLargeTextView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.shared.user.domain.entity.SignUpInfo
import com.yuliakazachok.synloans.shared.user.domain.usecase.SignUpUseCase

private sealed class SignUpUiState {
    data class Content(val hasError: Boolean = false) : SignUpUiState()
    data class SendingRequest(val data: SignUpInfo) : SignUpUiState()
}

class SignUpScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val signUpUseCase = koin.get<SignUpUseCase>()

        val uiState = remember { mutableStateOf<SignUpUiState>(SignUpUiState.Content()) }

        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordAgain = remember { mutableStateOf("") }
        val fullName = remember { mutableStateOf("") }
        val shortName = remember { mutableStateOf("") }
        val inn = remember { mutableStateOf("") }
        val kpp = remember { mutableStateOf("") }
        val legalAddress = remember { mutableStateOf("") }
        val actualAddress = remember { mutableStateOf("") }
        val creditOrganisation = remember { mutableStateOf(false) }

        val scaffoldState: ScaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
        ) {
            when (val state = uiState.value) {
                is SignUpUiState.Content -> {
                    if (state.hasError) {
                        LaunchedEffect(scaffoldState.snackbarHostState) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = TextResources.error,
                                duration = SnackbarDuration.Short,
                            )
                        }
                    }
                    SignUpContentView(
                        email = email.value,
                        password = password.value,
                        passwordAgain = passwordAgain.value,
                        fullName = fullName.value,
                        shortName = shortName.value,
                        inn = inn.value,
                        kpp = kpp.value,
                        legalAddress = legalAddress.value,
                        actualAddress = actualAddress.value,
                        creditOrganisation = creditOrganisation.value,
                        onEmailChanged = { email.value = it },
                        onPasswordChanged = { password.value = it },
                        onPasswordAgainChanged = { passwordAgain.value = it },
                        onFullNameChanged = { fullName.value = it },
                        onShortNameChanged = { shortName.value = it },
                        onInnChanged = { inn.value = it },
                        onKppChanged = { kpp.value = it },
                        onLegalAddressChanged = { legalAddress.value = it },
                        onActualAddressChanged = { actualAddress.value = it },
                        onCreditOrganisationChanged = { creditOrganisation.value = it },
                        onAuthorizationClick = { navigator.pop() },
                        onRegistrationClick = { data ->
                            uiState.value = if (password.value == passwordAgain.value) {
                                SignUpUiState.SendingRequest(data)
                            } else {
                                SignUpUiState.Content(hasError = true)
                            }
                        },
                    )
                }

                is SignUpUiState.SendingRequest -> {
                    LoadingView()
                    uiState.value = signUp(
                        signUpUseCase = signUpUseCase,
                        data = state.data,
                        onSignInRoute = { navigator.pop() },
                    ).value
                }
            }
        }
    }
}

@Composable
fun SignUpContentView(
    email: String,
    password: String,
    passwordAgain: String,
    fullName: String,
    shortName: String,
    inn: String,
    kpp: String,
    legalAddress: String,
    actualAddress: String,
    creditOrganisation: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordAgainChanged: (String) -> Unit,
    onFullNameChanged: (String) -> Unit,
    onShortNameChanged: (String) -> Unit,
    onInnChanged: (String) -> Unit,
    onKppChanged: (String) -> Unit,
    onLegalAddressChanged: (String) -> Unit,
    onActualAddressChanged: (String) -> Unit,
    onCreditOrganisationChanged: (Boolean) -> Unit,
    onAuthorizationClick: () -> Unit,
    onRegistrationClick: (SignUpInfo) -> Unit,
) {
    Scaffold {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Text(
                    text = TextResources.registration,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }
            item {
                EditLargeTextView(
                    text = email,
                    label = TextResources.email,
                    onTextChange = onEmailChanged,
                )
            }
            item {
                EditPasswordLargeTextView(
                    text = password,
                    label = TextResources.password,
                    onTextChange = onPasswordChanged,
                )
            }
            item {
                EditPasswordLargeTextView(
                    text = passwordAgain,
                    label = TextResources.passwordAgain,
                    onTextChange = onPasswordAgainChanged,
                )
            }
            item {
                EditLargeTextView(
                    text = fullName,
                    label = TextResources.fullName,
                    onTextChange = onFullNameChanged,
                )
            }
            item {
                EditLargeTextView(
                    text = shortName,
                    label = TextResources.shortName,
                    onTextChange = onShortNameChanged,
                )
            }
            item {
                EditLargeTextView(
                    text = inn,
                    label = TextResources.inn,
                    onTextChange = onInnChanged,
                )
            }
            item {
                EditLargeTextView(
                    text = kpp,
                    label = TextResources.kpp,
                    onTextChange = onKppChanged,
                )
            }
            item {
                EditLargeTextView(
                    text = legalAddress,
                    label = TextResources.legalAddress,
                    onTextChange = onLegalAddressChanged,
                )
            }
            item {
                EditLargeTextView(
                    text = actualAddress,
                    label = TextResources.actualAddress,
                    onTextChange = onActualAddressChanged,
                )
            }
            item {
                TextWithCheckboxView(
                    text = TextResources.creditOrganisation,
                    checked = creditOrganisation,
                    onCheckedChange = onCreditOrganisationChanged,
                )
            }
            item {
                Button(
                    onClick = {
                        onRegistrationClick(
                            SignUpInfo(
                                fullName = fullName,
                                shortName = shortName,
                                inn = inn,
                                kpp = kpp,
                                legalAddress = legalAddress,
                                actualAddress = actualAddress,
                                creditOrganisation = creditOrganisation,
                                email = email,
                                password = password,
                            )
                        )
                    },
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                ) {
                    Text(TextResources.signUp)
                }
            }
            item {
                Text(
                    text = TextResources.authorization,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(bottom = 8.dp).clickable { onAuthorizationClick() },
                )
            }
        }
    }
}

@Composable
private fun signUp(
    signUpUseCase: SignUpUseCase,
    data: SignUpInfo,
    onSignInRoute: () -> Unit,
): State<SignUpUiState> =
    produceState<SignUpUiState>(initialValue = SignUpUiState.SendingRequest(data), signUpUseCase, data) {
        try {
            signUpUseCase(data)
            onSignInRoute()
        } catch (throwable: Throwable) {
            value = SignUpUiState.Content(hasError = true)
        }
    }