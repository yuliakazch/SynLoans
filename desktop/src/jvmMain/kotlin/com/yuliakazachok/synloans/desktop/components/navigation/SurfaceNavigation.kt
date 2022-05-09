package com.yuliakazachok.synloans.desktop.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SurfaceNavigation(
    tabs: @Composable () -> Unit,
    mainContent: @Composable () -> Unit,
) {
    Row {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxHeight()
                .width(180.dp),
        ) {
            tabs()
        }
        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
        )
        mainContent()
    }
}