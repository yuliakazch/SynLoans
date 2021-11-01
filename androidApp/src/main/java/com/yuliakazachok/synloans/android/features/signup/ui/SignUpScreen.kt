package com.yuliakazachok.synloans.android.features.signup.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.components.checkbox.TextWithCheckboxView
import com.yuliakazachok.synloans.android.components.progress.LoadingView
import com.yuliakazachok.synloans.android.components.textfield.EditTextView
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

    LazyColumn(state = listState) {
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
                label = stringResource(R.string.field_password),
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
                label = stringResource(R.string.field_password_again),
                keyboardController = keyboardController,
                focusRequester = focusRequesterTwo,
                onPasswordChange = { onActionSent(SignUpAction.PasswordAgainChanged(it)) },
            )
        }
        item {
            EditTextView(
                text = state.content.fullName,
                label = stringResource(R.string.field_full_name_company),
                onTextChange = { onActionSent(SignUpAction.FullNameChanged(it)) },
            )
        }
        item {
            EditTextView(
                text = state.content.shortName,
                label = stringResource(R.string.field_short_name_company),
                onTextChange = { onActionSent(SignUpAction.ShortNameChanged(it)) },
            )
        }
        item {
            EditTextView(
                text = state.content.tin,
                label = stringResource(R.string.field_tin),
                onTextChange = { onActionSent(SignUpAction.TinChanged(it)) },
            )
        }
        item {
            EditTextView(
                text = state.content.iec,
                label = stringResource(R.string.field_iec),
                onTextChange = { onActionSent(SignUpAction.IecChanged(it)) },
            )
        }
        item {
            EditTextView(
                text = state.content.legalAddress,
                label = stringResource(R.string.field_legal_address),
                onTextChange = { onActionSent(SignUpAction.LegalAddressChanged(it)) },
            )
        }
        item {
            EditTextView(
                text = state.content.actualAddress,
                label = stringResource(R.string.field_actual_address),
                onTextChange = { onActionSent(SignUpAction.ActualAddressChanged(it)) },
            )
        }
        item {
            TextWithCheckboxView(
                text = stringResource(R.string.field_credit_organisation),
                checked = state.content.creditOrganisation,
                onCheckedChange = { onActionSent(SignUpAction.CreditOrganisationCheckChanged(it)) },
            )
        }
        item {
            Button(
                onClick = { onActionSent(SignUpAction.SignUpClicked) },
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.sign_up))
            }
        }
    }
}