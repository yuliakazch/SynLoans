package com.yuliakazachok.synloans.shared.request.data.datasource

import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment

interface RequestDataSource {

    suspend fun create(data: CreateRequestInfo, token: String)

    suspend fun getBorrowRequests(token: String): List<RequestCommon>

    suspend fun getBankRequests(): BankRequests

    suspend fun getRequestDetail(id: Int, token: String): RequestCommon

    suspend fun join(data: JoinSyndicateInfo, token: String)

    suspend fun getActualSchedule(id: Int): List<Payment>

    suspend fun getPlannedSchedule(id: Int): List<Payment>

    suspend fun cancel(id: Int, token: String)
}