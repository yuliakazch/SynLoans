package com.yuliakazachok.synloans.desktop.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.desktop.core.TextResources

@Composable
fun SurfaceNavigation(
    mainContent: @Composable () -> Unit,
    selectedRequests: Boolean,
    onClickedRequests: () -> Unit = {},
    onClickedProfile: () -> Unit = {},
) {
    Row {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxHeight()
                .width(180.dp),
        ) {
            NavigationTab(
                label = TextResources.requests,
                icon = painterResource("assignment.svg"),
                selected = selectedRequests,
                onClicked = { onClickedRequests() },
            )
            NavigationTab(
                label = TextResources.profile,
                icon = rememberVectorPainter(Icons.Filled.AccountBox),
                selected = !selectedRequests,
                onClicked = { onClickedProfile() },
            )
        }
        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
        )
        mainContent()
    }
}