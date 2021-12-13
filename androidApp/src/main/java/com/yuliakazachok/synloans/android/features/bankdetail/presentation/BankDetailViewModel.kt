package com.yuliakazachok.synloans.android.features.bankdetail.presentation

import androidx.lifecycle.viewModelScope
import com.yuliakazachok.synloans.android.core.BaseViewModel
import com.yuliakazachok.synloans.shared.bank.domain.usecase.GetBankDetailUseCase
import kotlinx.coroutines.launch

class BankDetailViewModel(
    private val getBankDetailUseCase: GetBankDetailUseCase,
    private val bankId: Int,
) : BaseViewModel<BankDetailAction, BankDetailState, BankDetailEffect>() {

    override fun setInitialState(): BankDetailState =
        BankDetailState(data = null, loading = true)

    override fun handleActions(action: BankDetailAction) {
        when (action) {
            is BankDetailAction.BackClicked -> {
                setEffect { BankDetailEffect.Navigation.ToBack }
            }
        }
    }

    init {
        loadBankInfo()
    }

    private fun loadBankInfo() {
        viewModelScope.launch {
            try {
                val detail = getBankDetailUseCase(bankId)
                setState { copy(data = detail, loading = false) }
            } catch (e: Throwable) {
                setState { copy(data = null, loading = false) }
            }
        }
    }
}