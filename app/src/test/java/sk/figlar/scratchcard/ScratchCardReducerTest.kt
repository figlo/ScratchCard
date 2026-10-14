package sk.figlar.scratchcard

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import sk.figlar.scratchcard.domain.ScratchCard
import sk.figlar.scratchcard.domain.ScratchCardEvent
import sk.figlar.scratchcard.domain.ScratchCardReducer
import sk.figlar.scratchcard.domain.ScratchCardStatus
import java.util.UUID

class ScratchCardReducerTest: StringSpec({

    val code = UUID.fromString("d6743448-22e5-4055-b169-ec60955b5218")

    "initial card is unscratched without code" {
        val card = ScratchCard()

        card.status shouldBe ScratchCardStatus.UNSCRATCHED
        card.code shouldBe null
    }

    "scratch complete changes status and saves code" {
        val result = ScratchCardReducer.reduce(
            ScratchCard(),
            ScratchCardEvent.ScratchCompleted(code),
        )

        result.status shouldBe ScratchCardStatus.SCRATCHED
        result.code shouldBe code
    }

    "scratching an already scratched card does nothing" {
        val originalCode = UUID.randomUUID()
        val card = ScratchCard(
            ScratchCardStatus.SCRATCHED,
            originalCode,
        )

        val result = ScratchCardReducer.reduce(
            card,
            ScratchCardEvent.ScratchCompleted(code),
        )

        result shouldBe card
    }

    "activation succeeded changes status and preserves code" {
        val card = ScratchCard(
            ScratchCardStatus.SCRATCHED,
            code,
        )

        val result = ScratchCardReducer.reduce(
            card,
            ScratchCardEvent.ActivationSucceeded,
        )

        result.status shouldBe ScratchCardStatus.ACTIVATED
        result.code shouldBe code
    }

    "activation of unscratched card does nothing" {
        val card = ScratchCard()

        val result = ScratchCardReducer.reduce(
            card,
            ScratchCardEvent.ActivationSucceeded,
        )

        result shouldBe card
    }

    "activation of activated card does nothing" {
        val card = ScratchCard(
            ScratchCardStatus.ACTIVATED,
            code,
        )

        val result = ScratchCardReducer.reduce(
            card,
            ScratchCardEvent.ActivationSucceeded,
        )

        result shouldBe card
    }

})