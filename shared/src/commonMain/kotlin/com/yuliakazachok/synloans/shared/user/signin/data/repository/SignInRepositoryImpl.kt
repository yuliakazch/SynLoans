package com.yuliakazachok.synloans.shared.user.signin.data.repository

import com.yuliakazachok.synloans.shared.user.signin.data.datasource.SignInDataSource
import com.yuliakazachok.synloans.shared.user.signin.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.signin.domain.repository.SignInRepository

class SignInRepositoryImpl(
    private val dataSource: SignInDataSource
) : SignInRepository {

    override suspend fun signIn(credentials: Credentials) {
        dataSource.signIn(credentials)
    }
}