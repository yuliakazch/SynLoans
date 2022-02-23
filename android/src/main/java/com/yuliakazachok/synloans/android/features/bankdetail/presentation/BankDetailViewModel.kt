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
        BankDetailState(data = null, loading = false)

    override fun handleActions(action: BankDetailAction) {
        when (action) {
            is BankDetailAction.BackClicked -> {
                setEffect { BankDetailEffect.Navigation.ToBack }
            }

            is BankDetailAction.RepeatClicked -> {
                loadBankInfo()
            }
        }
    }

    init {
        loadBankInfo()
    }

    private fun loadBankInfo() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val detail = getBankDetailUseCase(bankId)
                setState { copy(data = detail, loading = false) }
            } catch (e: Throwable) {
                setState { copy(data = null, loading = false) }
            }
        }
    }
}