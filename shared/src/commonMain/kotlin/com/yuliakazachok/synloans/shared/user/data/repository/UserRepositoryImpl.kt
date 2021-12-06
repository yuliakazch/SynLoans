package com.yuliakazachok.synloans.shared.user.data.repository

import com.yuliakazachok.synloans.shared.user.data.datasource.UserDataSource
import com.yuliakazachok.synloans.shared.user.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.domain.entity.EditProfileInfo
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.entity.SignUpInfo
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dataSource: UserDataSource,
) : UserRepository {

    override suspend fun signIn(credentials: Credentials) {
        dataSource.signIn(credentials)
    }

    override suspend fun signUp(data: SignUpInfo) {
        dataSource.signUp(data)
    }

    override suspend fun getProfile(): Profile =
        dataSource.getProfile()

    override suspend fun updateProfile(data: EditProfileInfo) {
        dataSource.updateProfile(data)
    }
}