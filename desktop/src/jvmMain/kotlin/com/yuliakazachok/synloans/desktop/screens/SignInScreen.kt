package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
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
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.navigation.NavigationTree
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.shared.user.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.domain.usecase.SignInUseCase
import org.koin.core.Koin
import ru.alexgladkov.odyssey.core.RootController

sealed class SignInUiState {
    data class Content(val hasError: Boolean = false) : SignInUiState()
    data class SendingRequest(val credentials: Credentials) : SignInUiState()
}

@Composable
fun SignInScreen(
    rootController: RootController,
    koin: Koin,
) {
    val signInUseCase = koin.get<SignInUseCase>()

    val uiState = remember { mutableStateOf<SignInUiState>(SignInUiState.Content()) }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        when (val state = uiState.value) {
            is SignInUiState.Content -> {
                if (state.hasError) {
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = TextResources.error,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                SignInContentView(
                    email = email.value,
                    password = password.value,
                    onEmailChanged = { email.value = it },
                    onPasswordChanged = { password.value = it },
                    onRegistrationClick = { rootController.launch(NavigationTree.Root.SignUp.name) },
                    onAuthorizationClick = { credentials ->
                        uiState.value = SignInUiState.SendingRequest(credentials)
                    },
                )
            }

            is SignInUiState.SendingRequest -> {
                LoadingView()
                uiState.value = signIn(signInUseCase, state.credentials, rootController).value
            }
        }
    }
}

@Composable
fun SignInContentView(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegistrationClick: () -> Unit,
    onAuthorizationClick: (Credentials) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarView(title = TextResources.authorization)
        }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
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
                Button(
                    onClick = { onAuthorizationClick(Credentials(email, password)) },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Text(TextResources.signIn)
                }
            }
            item {
                Text(
                    text = TextResources.registration,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { onRegistrationClick() },
                )
            }
        }
    }
}

@Composable
private fun signIn(
    signInUseCase: SignInUseCase,
    credentials: Credentials,
    rootController: RootController,
): State<SignInUiState> =
    produceState<SignInUiState>(initialValue = SignInUiState.SendingRequest(credentials), signInUseCase, credentials) {
        try {
            signInUseCase(credentials)
            rootController.launch(NavigationTree.Root.Profile.name)
        } catch (throwable: Throwable) {
            value = SignInUiState.Content(hasError = true)
        }
    }