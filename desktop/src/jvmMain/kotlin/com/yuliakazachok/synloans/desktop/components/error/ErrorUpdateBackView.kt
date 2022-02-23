package com.yuliakazachok.synloans.desktop.components.error

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.desktop.core.TextResources

@Composable
fun ErrorUpdateBackView(
    textBack: String,
    onBackClicked: () -> Unit,
    onUpdateClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = TextResources.error,
        )
        Button(
            onClick = onUpdateClicked,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(TextResources.repeat)
        }
        Text(
            text = textBack,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(top = 8.dp).clickable { onBackClicked() },
        )
    }
}