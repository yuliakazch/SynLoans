package com.yuliakazachok.synloans.shared.user.data.repository

import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSource
import com.yuliakazachok.synloans.shared.user.data.datasource.UserDataSource
import com.yuliakazachok.synloans.shared.user.domain.entity.*
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val tokenDataSource: TokenDataSource,
) : UserRepository {

    override suspend fun signIn(credentials: Credentials) {
        val token = userDataSource.signIn(credentials)
        tokenDataSource.set(token.value)
    }

    override suspend fun signUp(data: SignUpInfo) {
        userDataSource.signUp(data)
    }

    override suspend fun getProfile(): Profile =
        userDataSource.getProfile(tokenDataSource.get())

    override suspend fun updateProfile(data: EditProfileInfo) {
        userDataSource.updateProfile(data, tokenDataSource.get())
    }
}