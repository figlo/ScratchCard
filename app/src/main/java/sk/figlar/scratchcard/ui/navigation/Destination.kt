package sk.figlar.scratchcard.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination : NavKey {

    @Serializable
    data object Home : Destination

    @Serializable
    data object Scratch : Destination

    @Serializable
    data object Activation : Destination
}