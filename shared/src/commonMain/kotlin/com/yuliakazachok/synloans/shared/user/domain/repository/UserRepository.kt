package com.yuliakazachok.synloans.shared.user.domain.repository

import com.yuliakazachok.synloans.shared.user.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.domain.entity.EditProfileInfo
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.entity.SignUpInfo

interface UserRepository {

    suspend fun signIn(credentials: Credentials)

    suspend fun signUp(data: SignUpInfo)

    suspend fun getProfile(): Profile

    suspend fun updateProfile(data: EditProfileInfo)
}