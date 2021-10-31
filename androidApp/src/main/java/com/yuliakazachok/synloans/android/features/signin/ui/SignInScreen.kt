package com.yuliakazachok.synloans.android.features.signin.ui

import androidx.compose.runtime.Composable
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInAction
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInEffect
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInState
import kotlinx.coroutines.flow.Flow

@Composable
fun SignInScreen(
    state: SignInState,
    effectFlow: Flow<SignInEffect>?,
    onActionSent: (action: SignInAction) -> Unit,
    onNavigationRequested: (navigationEffect: SignInEffect.Navigation) -> Unit
) {

}