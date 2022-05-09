package com.yuliakazachok.synloans.desktop.core

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Orange = Color(0xfff8873c)
private val Purple = Color(0xff6b70fc)

private val Colors = lightColors(
    primary = Orange,
    primaryVariant = Orange,
    onPrimary = Color.White,
    secondary = Purple,
    onSecondary = Color.White
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = Colors,
        content = {
            Surface(content = content)
        }
    )
}