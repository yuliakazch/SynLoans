package com.yuliakazachok.synloans.android.components.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.yuliakazachok.synloans.android.R

@Composable
fun LoginView(
    login: String,
    focusRequester: FocusRequester,
    onAnimateScrolled: () -> Unit = {},
    onLoginChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = login,
        onValueChange = onLoginChange,
        label = { Text(stringResource(R.string.login)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                onAnimateScrolled()
                focusRequester.requestFocus()
            }
        ),
    )
}