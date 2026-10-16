package sk.figlar.scratchcard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sk.figlar.scratchcard.SIMULATED_OPERATION_DELAY
import sk.figlar.scratchcard.data.activation.ActivationRepository
import sk.figlar.scratchcard.data.activation.ActivationResult
import sk.figlar.scratchcard.domain.ScratchCard
import sk.figlar.scratchcard.domain.ScratchCardEvent
import sk.figlar.scratchcard.domain.ScratchCardReducer
import sk.figlar.scratchcard.domain.ScratchCardStatus
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

class ScratchCardViewModel(
    private val activationRepository: ActivationRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(ScratchCardUiState())
    val uiState = _uiState.asStateFlow()

    fun completeScratch(code: UUID) {
        _uiState.update { state ->
            state.copy(
                card = ScratchCardReducer.reduce(
                    state.card,
                    ScratchCardEvent.ScratchCompleted(code),
                ),
            )
        }
    }

    fun activateCard() {
        val state = _uiState.value

        if (state.card.status != ScratchCardStatus.SCRATCHED) return
        if (state.activationUiState != ActivationUiState.Idle) return

        val code = state.card.code ?: return

        _uiState.update {
            it.copy(activationUiState = ActivationUiState.Loading)
        }

        viewModelScope.launch {
            delay(SIMULATED_OPERATION_DELAY.milliseconds)

            when (activationRepository.activate(code)) {
                ActivationResult.Success -> {
                    _uiState.update {
                        it.copy(
                            card = ScratchCardReducer.reduce(
                                it.card,
                                ScratchCardEvent.ActivationSucceeded,
                            ),
                            activationUiState = ActivationUiState.Success,
                        )
                    }
                }
                ActivationResult.Rejected -> {
                    _uiState.update {
                        it.copy(
                            activationUiState = ActivationUiState.Rejected,
                        )
                    }
                }
                ActivationResult.Error -> {
                    _uiState.update {
                        it.copy(
                            activationUiState = ActivationUiState.Error,
                        )
                    }
                }
            }
        }
    }

    fun resetActivationState() {
        _uiState.update { state ->
            state.copy(activationUiState = ActivationUiState.Idle)
        }
    }

    fun resetCard() {
        _uiState.update {
            it.copy(
                card = ScratchCard(),
                activationUiState = ActivationUiState.Idle,
            )
        }
    }
}
