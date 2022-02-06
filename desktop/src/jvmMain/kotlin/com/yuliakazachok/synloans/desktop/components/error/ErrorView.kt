package com.yuliakazachok.synloans.desktop.components.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuliakazachok.synloans.desktop.core.TextResources

@Composable
fun ErrorView() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = TextResources.error,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}