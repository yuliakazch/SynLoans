package com.yuliakazachok.synloans.android.components.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TextTwoLinesView(
    textOne: String,
    textTwo: String,
) {
    Column(
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
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