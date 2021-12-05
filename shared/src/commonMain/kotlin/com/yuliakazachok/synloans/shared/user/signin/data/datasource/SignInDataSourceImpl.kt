package com.yuliakazachok.synloans.shared.user.signin.data.datasource

import com.yuliakazachok.synloans.shared.user.signin.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.signin.domain.entity.Token
import com.yuliakazachok.synloans.util.Config.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class SignInDataSourceImpl(
    private val httpClient: HttpClient
) : SignInDataSource {

    override suspend fun signIn(credentials: Credentials) {
        httpClient.post<Token>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
            body = credentials
        }
    }
}