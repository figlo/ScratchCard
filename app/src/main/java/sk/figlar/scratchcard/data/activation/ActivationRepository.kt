package sk.figlar.scratchcard.data.activation

import java.util.UUID

interface ActivationRepository {
    suspend fun activate(code: UUID): ActivationResult
}