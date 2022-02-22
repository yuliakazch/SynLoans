package com.yuliakazachok.synloans.desktop.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.desktop.core.LARGE_WIDTH_VIEW

@Composable
fun EditLargeTextView(
    text: String,
    label: String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.padding(bottom = 4.dp).width(LARGE_WIDTH_VIEW),
    )
}