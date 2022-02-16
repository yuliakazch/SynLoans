package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.EditTextView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.desktop.koin
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
        val uiState = remember { mutableStateOf<EditProfileUiState>(EditProfileUiState.Content()) }

        val updateProfileUseCase = koin.get<UpdateProfileUseCase>()

        val editProfileInfo = remember {
            mutableStateOf(
                EditProfileInfo(
                    fullName = profile.fullName,
                    shortName = profile.shortName,
                    inn = profile.inn,
                    kpp = profile.kpp,
                    legalAddress = profile.legalAddress,
                    actualAddress = profile.actualAddress,
                    email = profile.email,
                )
            )
        }

        val scaffoldState: ScaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopBarView(title = TextResources.editProfile)
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
                        onFullNameChanged = { editProfileInfo.value = editProfileInfo.value.copy(fullName = it) },
                        onShortNameChanged = { editProfileInfo.value = editProfileInfo.value.copy(shortName = it) },
                        onInnChanged = { editProfileInfo.value = editProfileInfo.value.copy(inn = it) },
                        onKppChanged = { editProfileInfo.value = editProfileInfo.value.copy(kpp = it) },
                        onLegalAddressChanged = { editProfileInfo.value = editProfileInfo.value.copy(legalAddress = it) },
                        onActualAddressChanged = { editProfileInfo.value = editProfileInfo.value.copy(actualAddress = it) },
                        onEmailChanged = { editProfileInfo.value = editProfileInfo.value.copy(email = it) },
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
}

@Composable
fun EditProfileView(
    data: EditProfileInfo,
    onFullNameChanged: (String) -> Unit,
    onShortNameChanged: (String) -> Unit,
    onInnChanged: (String) -> Unit,
    onKppChanged: (String) -> Unit,
    onLegalAddressChanged: (String) -> Unit,
    onActualAddressChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onSaveClicked: (EditProfileInfo) -> Unit,
    onCancelClicked: () -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
    ) {
        item {
            EditTextView(
                text = data.fullName,
                label = TextResources.fullName,
                onTextChange = onFullNameChanged,
            )
        }
        item {
            EditTextView(
                text = data.shortName,
                label = TextResources.shortName,
                onTextChange = onShortNameChanged,
            )
        }
        item {
            EditTextView(
                text = data.inn,
                label = TextResources.inn,
                onTextChange = onInnChanged,
            )
        }
        item {
            EditTextView(
                text = data.kpp,
                label = TextResources.kpp,
                onTextChange = onKppChanged,
            )
        }
        item {
            EditTextView(
                text = data.legalAddress,
                label = TextResources.legalAddress,
                onTextChange = onLegalAddressChanged,
            )
        }
        item {
            EditTextView(
                text = data.actualAddress,
                label = TextResources.actualAddress,
                onTextChange = onActualAddressChanged,
            )
        }
        item {
            EditTextView(
                text = data.email,
                label = TextResources.email,
                onTextChange = onEmailChanged,
            )
        }
        item {
            Button(
                onClick = { onSaveClicked(data) },
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(TextResources.save)
            }
        }
        item {
            Text(
                text = TextResources.cancel,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable { onCancelClicked() },
            )
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