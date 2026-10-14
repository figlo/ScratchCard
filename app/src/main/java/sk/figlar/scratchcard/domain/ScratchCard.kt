package sk.figlar.scratchcard.domain

import java.util.UUID

data class ScratchCard(
    val status: ScratchCardStatus = ScratchCardStatus.UNSCRATCHED,
    val code: UUID? = null,
)