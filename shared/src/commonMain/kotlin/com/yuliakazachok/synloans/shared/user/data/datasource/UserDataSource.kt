package com.yuliakazachok.synloans.shared.user.data.datasource

import com.yuliakazachok.synloans.shared.user.domain.entity.*

interface UserDataSource {

    suspend fun signIn(credentials: Credentials): Token

    suspend fun signUp(data: SignUpInfo)

    suspend fun getProfile(token: String): Profile

    suspend fun updateProfile(data: EditProfileInfo, token: String)
}