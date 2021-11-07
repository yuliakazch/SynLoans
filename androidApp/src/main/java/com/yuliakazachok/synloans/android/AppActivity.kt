package com.yuliakazachok.synloans.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.yuliakazachok.synloans.android.components.bottombar.BottomBarView
import com.yuliakazachok.synloans.android.core.CoreBottomScreen
import com.yuliakazachok.synloans.android.core.NavigationKeys.BANK_DETAIL
import com.yuliakazachok.synloans.android.core.NavigationKeys.CREATE_REQUEST
import com.yuliakazachok.synloans.android.core.NavigationKeys.EDIT_PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.JOIN_SYNDICATE
import com.yuliakazachok.synloans.android.core.NavigationKeys.PAYMENT_SCHEDULE
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUEST_DETAIL
import com.yuliakazachok.synloans.android.features.signin.ui.SignInDestination
import com.yuliakazachok.synloans.android.features.signup.ui.SignUpDestination
import com.yuliakazachok.synloans.android.theme.AppTheme
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_IN
import com.yuliakazachok.synloans.android.core.NavigationKeys.SIGN_UP
import com.yuliakazachok.synloans.android.features.bankdetail.ui.BankDetailDestination
import com.yuliakazachok.synloans.android.features.joinsyndicate.ui.JoinSyndicateDestination
import com.yuliakazachok.synloans.android.features.paymentschedule.ui.PaymentScheduleDestination
import com.yuliakazachok.synloans.android.features.profile.ui.ProfileDestination
import com.yuliakazachok.synloans.android.features.requestcreate.ui.RequestCreateDestination
import com.yuliakazachok.synloans.android.features.requestdetail.ui.RequestDetailDestination
import com.yuliakazachok.synloans.android.features.requests.ui.RequestsDestination

class AppActivity : ComponentActivity() {

	@ExperimentalPagerApi
	@ExperimentalComposeUiApi
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SynLoansApp()
		}
	}
}

@ExperimentalPagerApi
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
					composable(REQUESTS) { RequestsDestination(navController) }
					composable(CREATE_REQUEST) { RequestCreateDestination(navController) }
					composable(REQUEST_DETAIL) { RequestDetailDestination(navController) }
					composable(JOIN_SYNDICATE) { JoinSyndicateDestination(navController) }
					composable(BANK_DETAIL) { BankDetailDestination(navController) }
					composable(PAYMENT_SCHEDULE) { PaymentScheduleDestination(navController) }
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