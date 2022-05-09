package com.yuliakazachok.synloans.desktop.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun NavigationTab(
    label: String,
    icon: Painter,
    selected: Boolean,
    onClicked: () -> Unit = {},
) {
    val color = if (selected) MaterialTheme.colors.primary else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked() }
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            tint = color,
            modifier = Modifier.size(24.dp),
            contentDescription = null,
        )
        Text(
            text = label,
            color = color,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}