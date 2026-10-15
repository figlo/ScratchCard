package sk.figlar.scratchcard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.figlar.scratchcard.R
import sk.figlar.scratchcard.domain.ScratchCardStatus
import sk.figlar.scratchcard.ui.components.ScratchCardView

@Composable
fun ActivationScreen(
    viewModel: ScratchCardViewModel,
    onReturnToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val card = uiState.card
    val activationState = uiState.activationUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.activate_card),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(Modifier.height(24.dp))

        ScratchCardView(card = card)

        Spacer(Modifier.height(16.dp))

        when (activationState) {
            ActivationUiState.Idle -> {
                Text(stringResource(R.string.ready_to_activate))
            }

            ActivationUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.activating_card))
            }

            ActivationUiState.Success -> {
                Text(
                    text = stringResource(R.string.card_activated_successfully),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            ActivationUiState.Rejected,
            ActivationUiState.Error -> Unit
        }

        if (activationState == ActivationUiState.Idle &&
            card.status == ScratchCardStatus.SCRATCHED
        ) {
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = viewModel::activateCard,
            ) {
                Text(stringResource(R.string.activate_card))
            }
        }
    }

    // Alert dialog
    when (activationState) {
        ActivationUiState.Rejected -> {
            AlertDialog(
                onDismissRequest = viewModel::resetActivationState,
                title = { Text(stringResource(R.string.activation_rejected)) },
                text = { Text(stringResource(R.string.activation_rejected_body)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.resetActivationState()
                            onReturnToHome()
                        }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
            )
        }

        ActivationUiState.Error    -> {
            AlertDialog(
                onDismissRequest = viewModel::resetActivationState,
                title = { Text(stringResource(R.string.activation_failed)) },
                text = { Text(stringResource(R.string.activation_failed_body)) },
                confirmButton = {
                    TextButton(onClick = viewModel::resetActivationState) {
                        Text(stringResource(R.string.ok))
                    }
                },
            )
        }

        else                       -> Unit
    }
}
