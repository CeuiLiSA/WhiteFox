package com.white.fox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.white.fox.ui.common.NavHost
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.NavViewModelFactory
import com.white.fox.ui.theme.WhiteFoxTheme

class MainActivity : ComponentActivity() {

    private val navViewModel: NavViewModel by viewModels(factoryProducer = {
        NavViewModelFactory(
            sessionManager = (application as? ServiceProvider)?.sessionManager
                ?: throw ServiceProviderException()
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val serviceProvider =
            (application as? ServiceProvider) ?: throw ServiceProviderException()
        val dependency = Dependency(
            navViewModel = navViewModel,
            database = serviceProvider.database,
            client = serviceProvider.client,
            sessionManager = serviceProvider.sessionManager,
            prefStore = serviceProvider.prefStore,
            gson = serviceProvider.gson,
        )
        setContent {
            WhiteFoxTheme {
                NavHost(dependency)
            }
        }
    }
}
