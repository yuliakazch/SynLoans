package com.yuliakazachok.synloans.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.*
import com.yuliakazachok.synloans.android.components.bottombar.BottomBarView
import com.yuliakazachok.synloans.android.core.CoreBottomScreen
import com.yuliakazachok.synloans.android.core.NavigationKeys.EDIT_PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS
import com.yuliakazachok.synloans.android.features.signin.ui.SignInDestination
import com.yuliakazachok.synloans.android.features.signup.ui.SignUpDestination
import com.yuliakazachok.synloans.android.theme.AppTheme
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_IN
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_UP
import com.yuliakazachok.synloans.android.features.profile.ui.ProfileDestination

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
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val currentRoute = currentDestination?.route

        Scaffold(
            bottomBar = {
                if (currentRoute == PROFILE || currentRoute == REQUESTS) {
                    BottomBarView(
                        tabs = listOf(CoreBottomScreen.Requests, CoreBottomScreen.Profile),
                        navController = navController,
                        currentDestination = currentDestination,
                        currentRoute = currentRoute,
                    )
                }
            }
        ) {
            NavHost(navController = navController, startDestination = SIGN_IN) {
                composable(SIGN_IN) { SignInDestination(navController) }
                composable(SIGN_UP) { SignUpDestination(navController) }
                navigation(
                    startDestination = REQUESTS,
                    route = CoreBottomScreen.Requests.route,
                ) {
                    composable(REQUESTS) { /* TODO destination */ }
                }
                navigation(
                    startDestination = PROFILE,
                    route = CoreBottomScreen.Profile.route,
                ) {
                    composable(PROFILE) { ProfileDestination(navController) }
                    composable(EDIT_PROFILE) { /* TODO destination */ }
                }
            }
        }
    }
}