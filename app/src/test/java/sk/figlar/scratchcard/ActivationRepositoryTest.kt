package sk.figlar.scratchcard

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CancellationException
import sk.figlar.scratchcard.data.activation.ActivationApi
import sk.figlar.scratchcard.data.activation.ActivationRepositoryImpl
import sk.figlar.scratchcard.data.activation.ActivationResponse
import sk.figlar.scratchcard.data.activation.ActivationResult
import java.io.IOException
import java.util.UUID

class ActivationRepositoryTest : StringSpec({

    val code = UUID.fromString("d6743448-22e5-4055-b169-ec60955b5218")

    lateinit var api: FakeActivationApi
    lateinit var repository: ActivationRepositoryImpl

    beforeTest {
        api = FakeActivationApi()
        repository = ActivationRepositoryImpl(api)
    }

    "returns Success if android version is greater than threshold" {
        api.response = ActivationResponse((THRESHOLD + 1).toString())

        repository.activate(code) shouldBe ActivationResult.Success
    }

    "returns Rejected if android version equals threshold" {
        api.response = ActivationResponse(THRESHOLD.toString())

        repository.activate(code) shouldBe ActivationResult.Rejected
    }

    "returns Rejected if android version is lower than threshold" {
        api.response = ActivationResponse((THRESHOLD - 1).toString())

        repository.activate(code) shouldBe ActivationResult.Rejected
    }

    "returns Error if android version is missing" {
        api.response = ActivationResponse(null)

        repository.activate(code) shouldBe ActivationResult.Error
    }

    "returns Error if android version is not a number" {
        api.response = ActivationResponse("invalid")

        repository.activate(code) shouldBe ActivationResult.Error
    }

    "returns Error if api throws exception" {
        api.exception = IOException("Network error")

        repository.activate(code) shouldBe ActivationResult.Error
    }

    "rethrows CancellationException" {
        api.exception = CancellationException("Cancelled")

        shouldThrow<CancellationException> {
            repository.activate(code)
        }
    }

})

private class FakeActivationApi : ActivationApi {
    var response = ActivationResponse(THRESHOLD.toString())
    var exception: Exception? = null

    override suspend fun checkActivation(code: String): ActivationResponse {
        exception?.let { throw it }
        return response
    }
}