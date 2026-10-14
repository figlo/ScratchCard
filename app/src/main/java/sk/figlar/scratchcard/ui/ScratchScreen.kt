package sk.figlar.scratchcard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import sk.figlar.scratchcard.R
import sk.figlar.scratchcard.SCRATCH_DELAY
import sk.figlar.scratchcard.domain.ScratchCardStatus
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ScratchScreen(
    viewModel: ScratchCardViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var scratching by remember { mutableStateOf(false) }

    LaunchedEffect(scratching) {
        if (scratching) {
            delay(SCRATCH_DELAY.milliseconds)
            viewModel.completeScratch(UUID.randomUUID())
            scratching = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when {
            scratching -> {
                CircularProgressIndicator()

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.scratching_card),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            uiState.card.status == ScratchCardStatus.UNSCRATCHED -> {
                Button(onClick = { scratching = true }) {
                    Text(stringResource(R.string.scratch_card))
                }
            }

            else -> {
                Text(
                    text = stringResource(R.string.card_scratched),
                    style = MaterialTheme.typography.titleLarge,
                )

                uiState.card.code?.let { code ->
                    Spacer(Modifier.height(8.dp))
                    Text(text = stringResource(R.string.code) + ": " + code.toString().take(8))
                }
            }
        }
    }
}