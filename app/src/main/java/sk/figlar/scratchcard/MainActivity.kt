package sk.figlar.scratchcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import sk.figlar.scratchcard.data.activation.ActivationRepositoryImpl
import sk.figlar.scratchcard.data.activation.NetworkModule
import sk.figlar.scratchcard.ui.navigation.AppNavigation
import sk.figlar.scratchcard.ui.theme.ScratchCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = ActivationRepositoryImpl(NetworkModule.activationApi)

        setContent {
            ScratchCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        AppNavigation(repository = repository)
                    }
                }
            }
        }
    }
}