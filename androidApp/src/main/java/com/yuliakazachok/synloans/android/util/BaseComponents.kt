package com.yuliakazachok.synloans.android.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val LAUNCH_LISTEN_FOR_EFFECTS = "launch_listen_to_effects"

interface State
interface Action
interface Effect

abstract class BaseViewModel<A : Action, S : State, E : Effect>
    : ViewModel() {

    private val initialState: S by lazy { setInitialState() }
    abstract fun setInitialState(): S

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<S> = _viewState

    private val _action = MutableSharedFlow<A>()

    private val _effect = Channel<E>()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToActions()
    }

    fun setEvent(action: A) {
        viewModelScope.launch { _action.emit(action) }
    }

    protected fun setState(reducer: S.() -> S) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    private fun subscribeToActions() {
        viewModelScope.launch {
            _action.collect {
                handleActions(it)
            }
        }
    }

    abstract fun handleActions(action: A)

    protected fun setEffect(builder: () -> E) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}