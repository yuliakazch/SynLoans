package com.yuliakazachok.synloans.features.signin.data

import com.yuliakazachok.synloans.features.signin.domain.entity.Credentials
import com.yuliakazachok.synloans.features.signin.domain.repository.SignInRepository

class SignInRepositoryImpl : SignInRepository {

    override suspend fun signIn(credentials: Credentials) {}
}