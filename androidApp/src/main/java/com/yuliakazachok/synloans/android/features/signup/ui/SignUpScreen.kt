package com.yuliakazachok.synloans.android.features.signup.ui

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
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.textfield.EmailView
import com.yuliakazachok.synloans.android.components.textfield.PasswordDoneView
import com.yuliakazachok.synloans.android.components.textfield.PasswordNextView
import com.yuliakazachok.synloans.android.features.signup.presentation.SignUpAction
import com.yuliakazachok.synloans.android.features.signup.presentation.SignUpEffect
import com.yuliakazachok.synloans.android.features.signup.presentation.SignUpState
import com.yuliakazachok.synloans.android.util.LAUNCH_LISTEN_FOR_EFFECTS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun SignUpScreen(
    state: SignUpState,
    effectFlow: Flow<SignUpEffect>?,
    onActionSent: (action: SignUpAction) -> Unit,
    onNavigationRequested: (navigationEffect: SignUpEffect.Navigation) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val textError = stringResource(R.string.error)

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is SignUpEffect.Error ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = effect.message ?: textError,
                        duration = SnackbarDuration.Short
                    )
                is SignUpEffect.Navigation ->
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
            SignUpContentView(state, onActionSent)
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SignUpContentView(
    state: SignUpState,
    onActionSent: (action: SignUpAction) -> Unit,
) {
    val focusRequesterOne = remember { FocusRequester() }
    val focusRequesterTwo = remember { FocusRequester() }
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
            EmailView(
                email = state.content.email,
                focusRequester = focusRequesterOne,
                onAnimateScrolled = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 1)
                    }
                },
                onEmailChange = { onActionSent(SignUpAction.EmailChanged(it)) },
            )
        }
        item {
            PasswordNextView(
                password = state.content.password,
                label = stringResource(R.string.password),
                focusRequesterOne = focusRequesterOne,
                focusRequesterTwo = focusRequesterTwo,
                onAnimateScrolled = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 2)
                    }
                },
                onPasswordChange = { onActionSent(SignUpAction.PasswordChanged(it)) }
            )
        }
        item {
            PasswordDoneView(
                password = state.content.passwordAgain,
                label = stringResource(R.string.password_again),
                keyboardController = keyboardController,
                focusRequester = focusRequesterTwo,
                onPasswordChange = { onActionSent(SignUpAction.PasswordAgainChanged(it)) },
            )
        }
        item {
            Button(
                onClick = { onActionSent(SignUpAction.SignUpClicked) },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.sign_up))
            }
        }
    }
}