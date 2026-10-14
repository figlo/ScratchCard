package sk.figlar.scratchcard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.figlar.scratchcard.data.activation.ActivationRepositoryImpl

class ScratchCardViewModelFactory(
    private val repository: ActivationRepositoryImpl,
): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == ScratchCardViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return ScratchCardViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}