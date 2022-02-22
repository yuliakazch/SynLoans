package com.yuliakazachok.synloans.desktop.components.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TextTwoLinesClickableView(
    textOne: String,
    textTwo: String,
    onClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClicked() }
            .padding(horizontal = 16.dp),
    ) {
        Column {
            Text(
                text = textOne,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = textTwo,
                fontWeight = FontWeight.Light,
            )
        }
    }
}