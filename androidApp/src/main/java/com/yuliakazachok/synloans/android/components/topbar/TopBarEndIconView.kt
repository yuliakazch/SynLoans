package com.yuliakazachok.synloans.android.components.topbar

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TopBarEndIconView(
    title: String,
    icon: ImageVector,
    onIconClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        actions = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.clickable { onIconClicked() },
            )
        },
    )
}