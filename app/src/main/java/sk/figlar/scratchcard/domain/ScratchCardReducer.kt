package sk.figlar.scratchcard.domain

object ScratchCardReducer {
    fun reduce(
        card: ScratchCard,
        event: ScratchCardEvent,
    ): ScratchCard =
        when (event) {
            is ScratchCardEvent.ScratchCompleted    ->
                if (card.status == ScratchCardStatus.UNSCRATCHED)
                    card.copy(
                        status = ScratchCardStatus.SCRATCHED,
                        code = event.code,
                    )
                else card

            ScratchCardEvent.ActivationSucceeded ->
                if (card.status == ScratchCardStatus.SCRATCHED)
                    card.copy(
                        status = ScratchCardStatus.ACTIVATED,
                    )
                else card
        }
}