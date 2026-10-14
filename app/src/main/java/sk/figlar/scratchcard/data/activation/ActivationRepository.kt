package sk.figlar.scratchcard.data.activation

import sk.figlar.scratchcard.THRESHOLD
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

class ActivationRepository(
    private val api: ActivationApi,
) {
    suspend fun activate(code: UUID): ActivationResult {
        return try {
            val response = api.checkActivation(code.toString())
            val androidVersion = response.android?.toIntOrNull() ?: return ActivationResult.Error
            if (androidVersion > THRESHOLD)
                ActivationResult.Success
            else
                ActivationResult.Rejected
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ActivationResult.Error
        }
    }
}