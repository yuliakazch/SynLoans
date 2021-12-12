package com.yuliakazachok.synloans.shared.request.di

import com.yuliakazachok.synloans.shared.request.data.datasource.RequestDataSource
import com.yuliakazachok.synloans.shared.request.data.datasource.RequestDataSourceImpl
import com.yuliakazachok.synloans.shared.request.data.repository.RequestRepositoryImpl
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository
import com.yuliakazachok.synloans.shared.request.domain.usecase.*
import org.koin.dsl.module

val requestModule = module {

    single<RequestDataSource> { RequestDataSourceImpl(get()) }

    single<RequestRepository> { RequestRepositoryImpl(get(), get()) }

    single { CreateRequestUseCase(get()) }
    single { GetBorrowRequestsUseCase(get()) }
    single { GetBankRequestsUseCase(get()) }
    single { GetRequestDetailUseCase(get()) }
    single { JoinSyndicateUseCase(get()) }
    single { GetActualScheduleUseCase(get()) }
    single { GetPlannedScheduleUseCase(get()) }
    single { CancelRequestUseCase(get()) }
}