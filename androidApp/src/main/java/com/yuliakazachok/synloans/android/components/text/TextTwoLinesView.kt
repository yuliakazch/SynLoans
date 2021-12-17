package com.yuliakazachok.synloans.android.components.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TextTwoLinesView(
    textOne: String,
    textTwo: String,
    onClicked: () -> Unit = {},
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)
        .clickable { onClicked() }
        .padding(start = 16.dp, end = 16.dp)
    ) {
        Column {
            Text(
                text = textOne,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = textTwo,
                fontWeight = FontWeight.Light,
            )
        }
    }
}