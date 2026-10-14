package sk.figlar.scratchcard.data.activation

sealed interface ActivationResult {
    data object Success : ActivationResult
    data object Rejected : ActivationResult
    data object Error : ActivationResult
}