package com.yuliakazachok.synloans.desktop.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuliakazachok.synloans.desktop.components.error.ErrorView
import com.yuliakazachok.synloans.desktop.components.progress.LoadingView
import com.yuliakazachok.synloans.desktop.components.text.TextTwoLinesView
import com.yuliakazachok.synloans.desktop.components.topbar.TopBarTwoEndIconView
import com.yuliakazachok.synloans.desktop.core.TextResources
import com.yuliakazachok.synloans.shared.token.domain.usecase.ClearTokenUseCase
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase
import org.koin.core.Koin
import ru.alexgladkov.odyssey.core.RootController

sealed class ProfileUiState {
    data class Content(val profile: Profile) : ProfileUiState()
    object SendingRequest : ProfileUiState()
    object Error : ProfileUiState()
}

@Composable
fun ProfileScreen(
    rootController: RootController,
    koin: Koin,
) {
    val getProfileUseCase = koin.get<GetProfileUseCase>()
    val clearTokenUseCase = koin.get<ClearTokenUseCase>()

    val uiState = remember { mutableStateOf<ProfileUiState>(ProfileUiState.SendingRequest) }

    Scaffold(
        topBar = {
            TopBarTwoEndIconView(
                title = TextResources.profile,
                iconOne = Icons.Filled.Edit,
                iconTwo = Icons.Filled.ExitToApp,
                onOneIconClicked = { /* TODO */ },
                onTwoIconClicked = {
                    clearTokenUseCase()
                    rootController.popBackStack()
                },
            )
        }
    ) {
        when (val state = uiState.value) {
            is ProfileUiState.SendingRequest -> {
                LoadingView()
                uiState.value = loadProfile(getProfileUseCase).value
            }

            is ProfileUiState.Content -> {
                ProfileView(state.profile)
            }

            is ProfileUiState.Error -> {
                ErrorView()
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
    produceState<ProfileUiState>(initialValue = ProfileUiState.SendingRequest, getProfileUseCase) {
        value = try {
            val profile = getProfileUseCase()
            ProfileUiState.Content(profile)
        } catch (throwable: Throwable) {
            ProfileUiState.Error
        }
    }