package com.yuliakazachok.synloans.android.core

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.yuliakazachok.synloans.android.R
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE_TAB
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS_TAB

sealed class CoreBottomScreen(
    val route: String,
    @StringRes val stringId: Int,
    val icon: @Composable () -> Unit,
) {

    object Requests : CoreBottomScreen(
        REQUESTS_TAB,
        R.string.requests_title,
        { Icon(painterResource(R.drawable.ic_assignment), contentDescription = null) },
    )

    object Profile : CoreBottomScreen(
        PROFILE_TAB,
        R.string.profile_title,
        { Icon(Icons.Filled.AccountBox, contentDescription = null) },
    )
}