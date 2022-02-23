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
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.EditPasswordTextView
import com.yuliakazachok.synloans.desktop.components.text.EditTextView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.user.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.domain.usecase.SignInUseCase

private sealed class SignInUiState {
    data class Content(val hasError: Boolean = false) : SignInUiState()
    data class SendingRequest(val credentials: Credentials) : SignInUiState()
}

class SignInScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val signUpScreen = rememberScreen(NavigationScreen.SignUp)
        val mainScreen = rememberScreen(NavigationScreen.Main)

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
                            password.value = ""

                            scaffoldState.snackbarHostState.showSnackbar(
                                message = TextResources.error,
                                duration = SnackbarDuration.Short,
                            )
                        }
                    }
                    SignInContentView(
                        email = email.value,
                        password = password.value,
                        onEmailChanged = { email.value = it },
                        onPasswordChanged = { password.value = it },
                        onRegistrationClick = { navigator.push(signUpScreen) },
                        onAuthorizationClick = { credentials ->
                            uiState.value = SignInUiState.SendingRequest(credentials)
                        },
                    )
                }

                is SignInUiState.SendingRequest -> {
                    LoadingView()
                    uiState.value = signIn(
                        signInUseCase = signInUseCase,
                        credentials = state.credentials,
                        onMainRoute = { navigator.replace(mainScreen) },
                    ).value
                }
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
    Scaffold {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Text(
                    text = TextResources.authorization,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }
            item {
                EditTextView(
                    text = email,
                    label = TextResources.email,
                    onTextChange = onEmailChanged,
                )
            }
            item {
                EditPasswordTextView(
                    text = password,
                    label = TextResources.password,
                    onTextChange = onPasswordChanged,
                )
            }
            item {
                Button(
                    onClick = { onAuthorizationClick(Credentials(email, password)) },
                    modifier = Modifier.padding(vertical = 8.dp),
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
    onMainRoute: () -> Unit,
): State<SignInUiState> =
    produceState<SignInUiState>(initialValue = SignInUiState.SendingRequest(credentials), signInUseCase, credentials) {
        try {
            signInUseCase(credentials)
            onMainRoute()
        } catch (throwable: Throwable) {
            value = SignInUiState.Content(hasError = true)
        }
    }