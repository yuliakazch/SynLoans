package com.yuliakazachok.synloans.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.yuliakazachok.synloans.android.features.signin.ui.SignInDestination
import com.yuliakazachok.synloans.android.features.signup.ui.SignUpDestination
import com.yuliakazachok.synloans.android.theme.AppTheme
import com.yuliakazachok.synloans.android.util.NavigationKeys.SIGN_IN
import com.yuliakazachok.synloans.android.util.NavigationKeys.SIGN_UP

class AppActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SynLoansApp()
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SynLoansApp() {
    AppTheme {
        val navController = rememberNavController()

        ProvideWindowInsets {
            Box(
                Modifier.padding(
                    rememberInsetsPaddingValues(
                        insets = LocalWindowInsets.current.systemBars,
                        applyStart = true,
                        applyTop = false,
                        applyEnd = true,
                        applyBottom = false
                    )
                )
            ) {
                NavHost(navController = navController, startDestination = SIGN_UP) {
                    composable(SIGN_IN) { SignInDestination(navController) }
                    composable(SIGN_UP) { SignUpDestination(navController) }
                }
            }
        }
    }
}