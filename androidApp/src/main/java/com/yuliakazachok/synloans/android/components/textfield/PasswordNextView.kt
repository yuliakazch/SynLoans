package com.yuliakazachok.synloans.android.components.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun PasswordNextView(
    password: String,
    label: String,
    focusRequesterOne: FocusRequester,
    focusRequesterTwo: FocusRequester,
    onAnimateScrolled: () -> Unit = {},
    onPasswordChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onAnimateScrolled()
                focusRequesterTwo.requestFocus()
            }
        ),
        modifier = Modifier.focusRequester(focusRequesterOne),
    )
}