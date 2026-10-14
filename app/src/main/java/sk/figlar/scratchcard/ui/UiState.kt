package sk.figlar.scratchcard.ui

import sk.figlar.scratchcard.domain.ScratchCard

data class ScratchCardUiState(
    val card: ScratchCard = ScratchCard(),
    val activationUiState: ActivationUiState = ActivationUiState.Idle,
)

sealed interface ActivationUiState {
    data object Idle : ActivationUiState
    data object Loading : ActivationUiState
    data object Success : ActivationUiState
    data object Rejected : ActivationUiState
    data object Error : ActivationUiState
}