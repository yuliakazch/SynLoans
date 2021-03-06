package com.yuliakazachok.synloans.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.yuliakazachok.synloans.android.components.bottombar.BottomBarView
import com.yuliakazachok.synloans.android.core.CoreBottomScreen
import com.yuliakazachok.synloans.android.core.NavigationKeys.BANK_DETAIL
import com.yuliakazachok.synloans.android.core.NavigationKeys.CREATE_REQUEST
import com.yuliakazachok.synloans.android.core.NavigationKeys.EDIT_PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.JOIN_SYNDICATE
import com.yuliakazachok.synloans.android.core.NavigationKeys.MAKE_PAYMENT
import com.yuliakazachok.synloans.android.core.NavigationKeys.PAYMENT_SCHEDULE
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUEST_DETAIL
import com.yuliakazachok.synloans.android.features.signin.ui.SignInDestination
import com.yuliakazachok.synloans.android.features.signup.ui.SignUpDestination
import com.yuliakazachok.synloans.android.theme.AppTheme
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_IN
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_UP
import com.yuliakazachok.synloans.android.core.NavigationKeys.SPLASH
import com.yuliakazachok.synloans.android.features.bankdetail.ui.BankDetailDestination
import com.yuliakazachok.synloans.android.features.editprofile.ui.EditProfileDestination
import com.yuliakazachok.synloans.android.features.joinsyndicate.ui.JoinSyndicateDestination
import com.yuliakazachok.synloans.android.features.makepayment.ui.MakePaymentDestination
import com.yuliakazachok.synloans.android.features.paymentschedule.ui.PaymentScheduleDestination
import com.yuliakazachok.synloans.android.features.profile.ui.ProfileDestination
import com.yuliakazachok.synloans.android.features.requestcreate.ui.RequestCreateDestination
import com.yuliakazachok.synloans.android.features.requestdetail.ui.RequestDetailDestination
import com.yuliakazachok.synloans.android.features.requests.ui.RequestsDestination
import com.yuliakazachok.synloans.android.features.splash.ui.SplashDestination

class AppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SynLoansApp()
        }
    }
}

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
            NavHost(navController = navController, startDestination = SPLASH) {
                composable(SPLASH) { SplashDestination(navController) }
                composable(SIGN_IN) { SignInDestination(navController) }
                composable(SIGN_UP) { SignUpDestination(navController) }
                navigation(
                    startDestination = REQUESTS,
                    route = CoreBottomScreen.Requests.route,
                ) {
                    composable(REQUESTS) { RequestsDestination(navController) }
                    composable(CREATE_REQUEST) { RequestCreateDestination(navController) }
                    composable(
                        route = "$REQUEST_DETAIL/{requestId}/{participant}",
                        arguments = listOf(
                            navArgument("requestId") { type = NavType.IntType },
                            navArgument("participant") { type = NavType.BoolType },
                        ),
                    ) { backStackEntry ->
                        RequestDetailDestination(
                            navController = navController,
                            requestId = backStackEntry.arguments?.getInt("requestId") ?: throw NullPointerException("requestId is null"),
                            participant = backStackEntry.arguments?.getBoolean("participant") ?: throw NullPointerException("participant is null"),
                        )
                    }
                    composable(
                        route = "$JOIN_SYNDICATE/{requestId}",
                        arguments = listOf(navArgument("requestId") { type = NavType.IntType }),
                    ) { backStackEntry ->
                        JoinSyndicateDestination(
                            navController = navController,
                            requestId = backStackEntry.arguments?.getInt("requestId") ?: throw NullPointerException("requestId is null"),
                        )
                    }
                    composable(
                        route = "$BANK_DETAIL/{bankId}",
                        arguments = listOf(navArgument("bankId") { type = NavType.IntType }),
                    ) { backStackEntry ->
                        BankDetailDestination(
                            navController = navController,
                            bankId = backStackEntry.arguments?.getInt("bankId") ?: throw NullPointerException("bankId is null"),
                        )
                    }
                    composable(
                        route = "$PAYMENT_SCHEDULE/{requestId}/{schedule}",
                        arguments = listOf(
                            navArgument("schedule") { type = NavType.IntType },
                            navArgument("requestId") { type = NavType.IntType },
                        ),
                    ) { backStackEntry ->
                        PaymentScheduleDestination(
                            navController = navController,
                            schedule = backStackEntry.arguments?.getInt("schedule") ?: throw NullPointerException("schedule is null"),
                            requestId = backStackEntry.arguments?.getInt("requestId") ?: throw NullPointerException("requestId is null"),
                        )
                    }
                    composable(
                        route = "$MAKE_PAYMENT/{requestId}",
                        arguments = listOf(navArgument("requestId") { type = NavType.IntType }),
                    ) { backStackEntry ->
                        MakePaymentDestination(
                            navController = navController,
                            requestId = backStackEntry.arguments?.getInt("requestId") ?: throw NullPointerException("requestId is null"),
                        )
                    }
                }
                navigation(
                    startDestination = PROFILE,
                    route = CoreBottomScreen.Profile.route,
                ) {
                    composable(PROFILE) { ProfileDestination(navController) }
                    composable(EDIT_PROFILE) { EditProfileDestination(navController) }
                }
            }
        }
    }
}