package sk.figlar.scratchcard.domain

import java.util.UUID

sealed interface ScratchCardEvent {
    data class ScratchCompleted(val code: UUID): ScratchCardEvent
    data object ActivationSucceeded : ScratchCardEvent
}