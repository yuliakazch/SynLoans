package com.yuliakazachok.synloans.shared.request.data.repository

import com.yuliakazachok.synloans.shared.request.data.datasource.RequestDataSource
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BorrowRequest
import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class RequestRepositoryImpl(
    private val dataSource: RequestDataSource,
) : RequestRepository {

    override suspend fun create(data: CreateRequestInfo) {
        dataSource.create(data)
    }

    override suspend fun getBorrowRequests(): List<BorrowRequest> =
        dataSource.getBorrowRequests()

    override suspend fun getBankRequests(): BankRequests =
        dataSource.getBankRequests()

    override suspend fun getRequestDetail(id: Int): RequestCommon =
        dataSource.getRequestDetail(id)

    override suspend fun join(data: JoinSyndicateInfo) {
        dataSource.join(data)
    }

    override suspend fun getActualSchedule(id: Int): List<Payment> =
        dataSource.getActualSchedule(id)

    override suspend fun getPlannedSchedule(id: Int): List<Payment> =
        dataSource.getPlannedSchedule(id)

    override suspend fun cancel(id: Int) {
        dataSource.cancel(id)
    }
}