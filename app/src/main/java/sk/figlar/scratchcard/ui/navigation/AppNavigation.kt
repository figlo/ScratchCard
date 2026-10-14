package sk.figlar.scratchcard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import sk.figlar.scratchcard.data.activation.ActivationRepositoryImpl
import sk.figlar.scratchcard.ui.ActivationScreen
import sk.figlar.scratchcard.ui.HomeScreen
import sk.figlar.scratchcard.ui.ScratchCardViewModel
import sk.figlar.scratchcard.ui.ScratchCardViewModelFactory
import sk.figlar.scratchcard.ui.ScratchScreen

@Composable
fun AppNavigation(
    repository: ActivationRepositoryImpl,
) {
    val backStack = rememberNavBackStack(Destination.Home)

    val viewModel: ScratchCardViewModel = viewModel(
        factory = ScratchCardViewModelFactory(repository)
    )

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Destination.Home> {
                HomeScreen(
                    viewModel = viewModel,
                    onScratchClick = {
                        backStack.add(Destination.Scratch)
                    },
                    onActivationClick = {
                        backStack.add(Destination.Activation)
                    },
                )
            }
            entry<Destination.Scratch> {
                ScratchScreen(viewModel = viewModel)
            }
            entry<Destination.Activation> {
                ActivationScreen(
                    viewModel = viewModel,
                    onReturnToHome = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}