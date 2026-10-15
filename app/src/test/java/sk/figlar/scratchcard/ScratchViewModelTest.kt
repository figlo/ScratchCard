package sk.figlar.scratchcard

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import sk.figlar.scratchcard.data.activation.ActivationRepository
import sk.figlar.scratchcard.data.activation.ActivationResult
import sk.figlar.scratchcard.domain.ScratchCardStatus
import sk.figlar.scratchcard.ui.ActivationUiState
import sk.figlar.scratchcard.ui.ScratchCardViewModel
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchViewModelTest: StringSpec({

    "completeScratch updates card status and code" {
        runViewModelTest {
            val repository = FakeActivationRepository()
            val viewModel = ScratchCardViewModel(repository)
            val code = UUID.randomUUID()

            viewModel.completeScratch(code)

            viewModel.uiState.value.card.status shouldBe ScratchCardStatus.SCRATCHED
            viewModel.uiState.value.card.code shouldBe code
        }
    }

    "activation is ignored when card is not scratched" {
        runViewModelTest {
            val repository = FakeActivationRepository()
            val viewModel = ScratchCardViewModel(repository)

            viewModel.activateCard()

            repository.callCount shouldBe 0
            viewModel.uiState.value.activationUiState shouldBe ActivationUiState.Idle
        }
    }

    "successful activation updates card and UI state" {
        runViewModelTest {
            val repository = FakeActivationRepository()
            val viewModel = ScratchCardViewModel(repository)
            val code = UUID.randomUUID()

            viewModel.completeScratch(code)
            viewModel.activateCard()
            advanceUntilIdle()

            repository.callCount shouldBe 1
            repository.receivedCode shouldBe code
            viewModel.uiState.value.card.status shouldBe ScratchCardStatus.ACTIVATED
            viewModel.uiState.value.activationUiState shouldBe ActivationUiState.Success
        }
    }

    "rejected activation updates UI state" {
        runViewModelTest {
            val repository = FakeActivationRepository(result = ActivationResult.Rejected)
            val viewModel = ScratchCardViewModel(repository)

            viewModel.completeScratch(UUID.randomUUID())
            viewModel.activateCard()
            advanceUntilIdle()

            viewModel.uiState.value.card.status shouldBe ScratchCardStatus.SCRATCHED
            viewModel.uiState.value.activationUiState shouldBe ActivationUiState.Rejected
        }
    }

    "activation error updates UI state" {
        runViewModelTest {
            val repository = FakeActivationRepository(result = ActivationResult.Error)
            val viewModel = ScratchCardViewModel(repository)

            viewModel.completeScratch(UUID.randomUUID())
            viewModel.activateCard()
            advanceUntilIdle()

            viewModel.uiState.value.card.status shouldBe ScratchCardStatus.SCRATCHED
            viewModel.uiState.value.activationUiState shouldBe ActivationUiState.Error
        }
    }

    "resetActivationState resets state to Idle" {
        runViewModelTest {
            val repository = FakeActivationRepository(result = ActivationResult.Error)
            val viewModel = ScratchCardViewModel(repository)

            viewModel.completeScratch(UUID.randomUUID())
            viewModel.activateCard()
            advanceUntilIdle()

            viewModel.uiState.value.activationUiState shouldBe ActivationUiState.Error
            viewModel.resetActivationState()
            viewModel.uiState.value.activationUiState shouldBe ActivationUiState.Idle
        }
    }
})

private class FakeActivationRepository(
    var result: ActivationResult = ActivationResult.Success,
) : ActivationRepository {

    var callCount = 0
    var receivedCode: UUID? = null

    override suspend fun activate(code: UUID): ActivationResult {
        callCount++
        receivedCode = code
        return result
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun runViewModelTest(
    testBody: suspend TestScope.() -> Unit,
) = runTest {
    Dispatchers.setMain(StandardTestDispatcher(testScheduler))
    try {
        testBody()
    } finally {
        Dispatchers.resetMain()
    }
}