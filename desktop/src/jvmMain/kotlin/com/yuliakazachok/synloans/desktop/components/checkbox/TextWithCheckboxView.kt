package com.yuliakazachok.synloans.desktop.components.checkbox

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.desktop.core.DEFAULT_WIDTH_VIEW

@Composable
fun TextWithCheckboxView(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.width(DEFAULT_WIDTH_VIEW),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(top = 16.dp, start = 8.dp),
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary),
        )
    }
}