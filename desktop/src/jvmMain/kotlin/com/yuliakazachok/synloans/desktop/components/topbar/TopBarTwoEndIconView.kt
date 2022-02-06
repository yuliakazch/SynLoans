package com.yuliakazachok.synloans.desktop.components.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TopBarTwoEndIconView(
    title: String,
    iconOne: ImageVector,
    iconTwo: ImageVector,
    onOneIconClicked: () -> Unit,
    onTwoIconClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        actions = {
            Icon(
                imageVector = iconOne,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onOneIconClicked() },
            )
            Icon(
                imageVector = iconTwo,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onTwoIconClicked() },
            )
        },
    )
}