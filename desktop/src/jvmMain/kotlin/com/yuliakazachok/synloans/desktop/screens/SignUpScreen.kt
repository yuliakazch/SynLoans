package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.desktop.components.checkbox.TextWithCheckboxView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.shared.user.domain.entity.SignUpInfo
import com.yuliakazachok.synloans.shared.user.domain.usecase.SignUpUseCase
import org.koin.core.Koin
import ru.alexgladkov.odyssey.core.RootController

sealed class SignUpUiState {
    data class Content(val hasError: Boolean = false) : SignUpUiState()
    data class SendingRequest(val data: SignUpInfo) : SignUpUiState()
}

@Composable
fun SignUpScreen(
    rootController: RootController,
    koin: Koin,
) {
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
                            duration = SnackbarDuration.Short
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
                    onAuthorizationClick = { rootController.popBackStack() },
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
                uiState.value = signUp(signUpUseCase, state.data, rootController).value
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
    Scaffold(
        topBar = {
            TopBarView(title = TextResources.registration)
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChanged,
                    label = { Text(TextResources.email) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChanged,
                    label = { Text(TextResources.password) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = passwordAgain,
                    onValueChange = onPasswordAgainChanged,
                    label = { Text(TextResources.passwordAgain) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = onFullNameChanged,
                    label = { Text(TextResources.fullName) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = shortName,
                    onValueChange = onShortNameChanged,
                    label = { Text(TextResources.shortName) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = inn,
                    onValueChange = onInnChanged,
                    label = { Text(TextResources.inn) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = kpp,
                    onValueChange = onKppChanged,
                    label = { Text(TextResources.kpp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = legalAddress,
                    onValueChange = onLegalAddressChanged,
                    label = { Text(TextResources.legalAddress) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = actualAddress,
                    onValueChange = onActualAddressChanged,
                    label = { Text(TextResources.actualAddress) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(TextResources.signUp)
                }
            }
            item {
                Text(
                    text = TextResources.authorization,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { onAuthorizationClick() },
                )
            }
        }
    }
}

@Composable
private fun signUp(
    signUpUseCase: SignUpUseCase,
    data: SignUpInfo,
    rootController: RootController,
): State<SignUpUiState> =
    produceState<SignUpUiState>(initialValue = SignUpUiState.SendingRequest(data), signUpUseCase, data) {
        try {
            signUpUseCase(data)
            rootController.popBackStack()
        } catch (throwable: Throwable) {
            value = SignUpUiState.Content(hasError = true)
        }
    }