package com.yuliakazachok.synloans.shared.user.data.datasource

import com.yuliakazachok.synloans.shared.user.domain.entity.*
import com.yuliakazachok.synloans.util.Config.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserDataSourceImpl(
    private val httpClient: HttpClient,
) : UserDataSource {

    override suspend fun signIn(credentials: Credentials) {
        httpClient.post<Token>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
            body = credentials
        }
    }

    override suspend fun signUp(data: SignUpInfo) {
        httpClient.post<Unit>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
            body = data
        }
    }

    override suspend fun getProfile(): Profile =
        httpClient.get<Profile>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
        }

    override suspend fun updateProfile(data: EditProfileInfo) {
        httpClient.post<Unit>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
            body = data
        }
    }
}