package sk.figlar.scratchcard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.figlar.scratchcard.R
import sk.figlar.scratchcard.domain.ScratchCardStatus

@Composable
fun HomeScreen(
    viewModel: ScratchCardViewModel,
    onScratchClick: () -> Unit,
    onActivationClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val card = uiState.card

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.scratch_card),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = when (card.status) {
                ScratchCardStatus.UNSCRATCHED -> stringResource(R.string.not_scratched)
                ScratchCardStatus.SCRATCHED   -> stringResource(R.string.scratched)
                ScratchCardStatus.ACTIVATED -> stringResource(R.string.activated)
            },
            style = MaterialTheme.typography.titleLarge,
        )

        card.code?.let { code ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.code) + ": " + code.toString().take(8),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onScratchClick,
            enabled = card.status == ScratchCardStatus.UNSCRATCHED,
        ) {
            Text(stringResource(R.string.scratch_card))
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onActivationClick,
            enabled = card.status == ScratchCardStatus.SCRATCHED,
        ) {
            Text(stringResource(R.string.activate_card))
        }
    }
}