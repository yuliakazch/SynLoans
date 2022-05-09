package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.navigation.SurfaceNavigation
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.EditLargeTextView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarBackView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
import com.yuliakazachok.synloans.desktop.navigation.NavigationScreen
import com.yuliakazachok.synloans.shared.user.domain.entity.EditProfileInfo
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.usecase.UpdateProfileUseCase

private sealed class EditProfileUiState {
    data class Content(val hasError: Boolean = false) : EditProfileUiState()
    data class SendingRequest(val data: EditProfileInfo) : EditProfileUiState()
}

class EditProfileScreen(
    private val profile: Profile,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val requestsScreen = rememberScreen(NavigationScreen.Requests)
        val profileScreen = rememberScreen(NavigationScreen.ProfileInfo)

        SurfaceNavigation(
            mainContent = { EditProfileContent(navigator, profile) },
            selectedRequests = false,
            onClickedRequests = { navigator.replaceAll(requestsScreen) },
            onClickedProfile = { navigator.replaceAll(profileScreen) },
        )
    }
}

@Composable
fun EditProfileContent(
    navigator: Navigator,
    profile: Profile,
) {
    val uiState = remember { mutableStateOf<EditProfileUiState>(EditProfileUiState.Content()) }

    val updateProfileUseCase = koin.get<UpdateProfileUseCase>()

    val editProfileInfo = remember {
        mutableStateOf(
            EditProfileInfo(
                shortName = profile.shortName,
                legalAddress = profile.legalAddress,
                actualAddress = profile.actualAddress,
            )
        )
    }

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBarBackView(
                title = TextResources.editProfile,
                onIconClicked = { navigator.pop() },
            )
        }
    ) {
        when (val state = uiState.value) {
            is EditProfileUiState.Content -> {
                if (state.hasError) {
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = TextResources.error,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                EditProfileView(
                    data = editProfileInfo.value,
                    onShortNameChanged = { editProfileInfo.value = editProfileInfo.value.copy(shortName = it) },
                    onLegalAddressChanged = { editProfileInfo.value = editProfileInfo.value.copy(legalAddress = it) },
                    onActualAddressChanged = { editProfileInfo.value = editProfileInfo.value.copy(actualAddress = it) },
                    onSaveClicked = { data ->
                        uiState.value = EditProfileUiState.SendingRequest(data)
                    },
                    onCancelClicked = { navigator.pop() },
                )
            }

            is EditProfileUiState.SendingRequest -> {
                LoadingView()
                uiState.value = updateProfile(
                    updateProfileUseCase = updateProfileUseCase,
                    data = state.data,
                    onMainRoute = { navigator.pop() },
                ).value
            }
        }
    }
}

@Composable
fun EditProfileView(
    data: EditProfileInfo,
    onShortNameChanged: (String) -> Unit,
    onLegalAddressChanged: (String) -> Unit,
    onActualAddressChanged: (String) -> Unit,
    onSaveClicked: (EditProfileInfo) -> Unit,
    onCancelClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
    ) {
        item {
            EditLargeTextView(
                text = data.shortName,
                label = TextResources.shortName,
                onTextChange = onShortNameChanged,
            )
        }
        item {
            EditLargeTextView(
                text = data.legalAddress,
                label = TextResources.legalAddress,
                onTextChange = onLegalAddressChanged,
            )
        }
        item {
            EditLargeTextView(
                text = data.actualAddress,
                label = TextResources.actualAddress,
                onTextChange = onActualAddressChanged,
            )
        }
        item {
            Button(
                onClick = { onSaveClicked(data) },
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                Text(TextResources.save)
            }
        }
    }
}

@Composable
private fun updateProfile(
    updateProfileUseCase: UpdateProfileUseCase,
    data: EditProfileInfo,
    onMainRoute: () -> Unit,
): State<EditProfileUiState> =
    produceState<EditProfileUiState>(initialValue = EditProfileUiState.SendingRequest(data), updateProfileUseCase, data) {
        try {
            updateProfileUseCase(data)
            onMainRoute()
        } catch (throwable: Throwable) {
            value = EditProfileUiState.Content(hasError = true)
        }
    }