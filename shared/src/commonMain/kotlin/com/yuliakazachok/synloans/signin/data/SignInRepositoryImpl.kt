package com.yuliakazachok.synloans.signin.data

import com.yuliakazachok.synloans.signin.domain.entity.Credentials
import com.yuliakazachok.synloans.signin.domain.repository.SignInRepository

class SignInRepositoryImpl : SignInRepository {

    override suspend fun signIn(credentials: Credentials) {}
}