package com.yuliakazachok.synloans.desktop.components.topbar

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun TopBarView(
    title: String
) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    )
}