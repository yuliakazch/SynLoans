package com.yuliakazachok.synloans.android.features.signin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.textfield.LoginView
import com.yuliakazachok.synloans.android.components.textfield.PasswordDoneView
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInAction
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInEffect
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInState
import com.yuliakazachok.synloans.android.util.LAUNCH_LISTEN_FOR_EFFECTS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun SignInScreen(
    state: SignInState,
    effectFlow: Flow<SignInEffect>?,
    onActionSent: (action: SignInAction) -> Unit,
    onNavigationRequested: (navigationEffect: SignInEffect.Navigation) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val textError = stringResource(R.string.error)

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is SignInEffect.Error ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = effect.message ?: textError,
                        duration = SnackbarDuration.Short
                    )
                is SignInEffect.Navigation ->
                    onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        if (state.loading) {
            LoadingView()
        } else {
            SignInContentView(state, onActionSent)
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SignInContentView(
    state: SignInState,
    onActionSent: (action: SignInAction) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            LoginView(
                login = state.credentials.login,
                focusRequester = focusRequester,
                onAnimateScrolled = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 1)
                    }
                },
                onLoginChange = { onActionSent(SignInAction.LoginChanged(it)) },
            )
        }
        item {
            PasswordDoneView(
                password = state.credentials.password,
                label = stringResource(R.string.password),
                keyboardController = keyboardController,
                focusRequester = focusRequester,
                onPasswordChange = { onActionSent(SignInAction.PasswordChanged(it)) },
            )
        }
        item {
            Button(
                onClick = { onActionSent(SignInAction.SignInClicked) },
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                Text(stringResource(R.string.sign_in))
            }
        }
        item {
            Text(
                text = stringResource(R.string.registration),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onActionSent(SignInAction.RegistrationClicked)
                },
            )
        }
    }
}