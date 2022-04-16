package com.yuliakazachok.synloans.shared.request.data.repository

import com.yuliakazachok.synloans.shared.request.data.datasource.RequestDataSource
import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository
import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSource

class RequestRepositoryImpl(
    private val requestDataSource: RequestDataSource,
    private val tokenDataSource: TokenDataSource,
) : RequestRepository {

    override suspend fun create(data: CreateRequestInfo) {
        requestDataSource.create(data, tokenDataSource.get())
    }

    override suspend fun getBorrowRequests(): List<RequestCommon> =
        requestDataSource.getBorrowRequests(tokenDataSource.get())

    override suspend fun getBankRequests(): BankRequests =
        requestDataSource.getBankRequests(tokenDataSource.get())

    override suspend fun getRequestDetail(id: Int): RequestCommon =
        requestDataSource.getRequestDetail(id, tokenDataSource.get())

    override suspend fun join(data: JoinSyndicateInfo) {
        requestDataSource.join(data, tokenDataSource.get())
    }

    override suspend fun getActualSchedule(id: Int): List<PaymentInfo> =
        requestDataSource.getActualSchedule(id, tokenDataSource.get())

    override suspend fun getPlannedSchedule(id: Int): List<PaymentInfo> =
        requestDataSource.getPlannedSchedule(id, tokenDataSource.get())

    override suspend fun cancel(id: Int) {
        requestDataSource.cancel(id, tokenDataSource.get())
    }

    override suspend fun makePayment(id: Int, payment: Payment) {
        requestDataSource.makePayment(id, payment, tokenDataSource.get())
    }
}