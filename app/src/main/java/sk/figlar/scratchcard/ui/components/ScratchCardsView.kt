package sk.figlar.scratchcard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import sk.figlar.scratchcard.R
import sk.figlar.scratchcard.domain.ScratchCard
import sk.figlar.scratchcard.domain.ScratchCardStatus
import sk.figlar.scratchcard.ui.ActivationUiState

@Composable
fun ScratchCardView(
    card: ScratchCard,
    modifier: Modifier = Modifier,
    activationState: ActivationUiState = ActivationUiState.Idle,
) {
    val codeBackground = when (card.status) {
        ScratchCardStatus.ACTIVATED -> Color(0xFFC8E6C9)
        ScratchCardStatus.SCRATCHED -> Color(0xFFFFE0B2)
        else                        -> Color(0xFFE0E0E0)
    }

    val codeColor = when {
        card.status == ScratchCardStatus.ACTIVATED -> Color(0xFF1B5E20)
        card.status == ScratchCardStatus.SCRATCHED -> Color(0xFFE65100)
        else                                       -> Color(0xFF616161)
    }

    Card(
        modifier = modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF88ccFF),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.scratch_card),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(20.dp))

            when (card.status) {
                ScratchCardStatus.UNSCRATCHED -> {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                    )
                }

                ScratchCardStatus.SCRATCHED   -> {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                    )
                }

                ScratchCardStatus.ACTIVATED   -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = when {
                    activationState == ActivationUiState.Rejected ->
                        stringResource(R.string.activation_rejected)

                    activationState == ActivationUiState.Error    ->
                        stringResource(R.string.activation_failed)

                    card.status == ScratchCardStatus.UNSCRATCHED  ->
                        stringResource(R.string.not_scratched)

                    card.status == ScratchCardStatus.SCRATCHED    ->
                        stringResource(R.string.scratched)

                    else                                          ->
                        stringResource(R.string.activated)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(codeBackground)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = card.code?.toString() ?: stringResource(R.string.uuid_placeholder),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = codeColor,
                )
            }
        }
    }
}