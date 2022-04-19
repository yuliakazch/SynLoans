package com.yuliakazachok.synloans.desktop.components.text

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
fun TextThreeLinesView(
    textOne: String,
    textTwo: String,
    textThree: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp, start = 16.dp, end = 16.dp),
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
            Text(
                text = textThree,
                fontWeight = FontWeight.Light,
            )
        }
    }
}