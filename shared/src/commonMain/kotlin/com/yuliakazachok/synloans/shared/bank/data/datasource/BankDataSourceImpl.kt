package com.yuliakazachok.synloans.shared.bank.data.datasource

import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank
import com.yuliakazachok.synloans.util.Config.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class BankDataSourceImpl(
    private val httpClient: HttpClient,
) : BankDataSource {

    override suspend fun getDetail(id: Int): Bank =
        httpClient.get<Bank>("$BASE_URL/$id") { // TODO add url
            contentType(ContentType.Application.Json)
        }
}