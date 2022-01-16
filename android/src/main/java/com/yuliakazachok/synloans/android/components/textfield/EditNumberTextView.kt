package com.yuliakazachok.synloans.android.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EditNumberTextView(
	text: String,
	label: String,
	onTextChange: (String) -> Unit,
) {
	OutlinedTextField(
		value = text,
		onValueChange = onTextChange,
		label = { Text(label) },
		singleLine = true,
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
		modifier = Modifier.fillMaxWidth(),
	)
}