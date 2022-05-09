package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.navigation.SurfaceNavigation
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarTwoEndIconView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.token.domain.usecase.ClearTokenUseCase
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase

private sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Content(val profile: Profile) : ProfileUiState()
    object Error : ProfileUiState()
}

class ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val requestsScreen = rememberScreen(NavigationScreen.Requests)

        SurfaceNavigation(
            mainContent = { ProfileContent(navigator) },
            selectedRequests = false,
            onClickedRequests = { navigator.replaceAll(requestsScreen) },
        )
    }
}

@Composable
fun ProfileContent(navigator: Navigator) {
    val uiState = remember { mutableStateOf<ProfileUiState>(ProfileUiState.Loading) }

    val getProfileUseCase = koin.get<GetProfileUseCase>()
    val clearTokenUseCase = koin.get<ClearTokenUseCase>()

    val signInScreen = rememberScreen(NavigationScreen.SignIn)
    var editProfileScreen: Screen? = null

    Scaffold(
        topBar = {
            TopBarTwoEndIconView(
                title = TextResources.profile,
                iconOne = Icons.Filled.Edit,
                iconTwo = Icons.Filled.ExitToApp,
                onOneIconClicked = { editProfileScreen?.let { navigator.push(it) } },
                onTwoIconClicked = {
                    clearTokenUseCase()
                    navigator.replaceAll(signInScreen)
                },
            )
        }
    ) {
        when (val state = uiState.value) {
            is ProfileUiState.Loading -> {
                LoadingView()
                uiState.value = loadProfile(getProfileUseCase).value
            }

            is ProfileUiState.Content -> {
                editProfileScreen = rememberScreen(NavigationScreen.EditProfile(state.profile))
                ProfileView(state.profile)
            }

            is ProfileUiState.Error -> {
                ErrorView(
                    onUpdateClicked = { uiState.value = ProfileUiState.Loading },
                )
            }
        }
    }
}

@Composable
fun ProfileView(
    profile: Profile,
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        item {
            TextTwoLinesView(
                textOne = TextResources.fullName,
                textTwo = profile.fullName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.shortName,
                textTwo = profile.shortName,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.inn,
                textTwo = profile.inn,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.kpp,
                textTwo = profile.kpp,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.legalAddress,
                textTwo = profile.legalAddress,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.actualAddress,
                textTwo = profile.actualAddress,
            )
        }
        item {
            TextTwoLinesView(
                textOne = TextResources.email,
                textTwo = profile.email,
            )
        }
    }
}

@Composable
private fun loadProfile(
    getProfileUseCase: GetProfileUseCase,
): State<ProfileUiState> =
    produceState<ProfileUiState>(initialValue = ProfileUiState.Loading, getProfileUseCase) {
        value = try {
            val profile = getProfileUseCase()
            ProfileUiState.Content(profile)
        } catch (throwable: Throwable) {
            ProfileUiState.Error
        }
    }