package sk.figlar.scratchcard.data.activation

import kotlinx.coroutines.delay
import sk.figlar.scratchcard.SIMULATED_OPERATION_DELAY
import sk.figlar.scratchcard.THRESHOLD
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

class ActivationRepositoryImpl(
    private val api: ActivationApi,
): ActivationRepository {
    override suspend fun activate(code: UUID): ActivationResult {
        delay(SIMULATED_OPERATION_DELAY.milliseconds)
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