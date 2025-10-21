package com.white.fox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import ceui.lisa.hermes.viewmodel.constructVM
import com.white.fox.ui.common.Dependency
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.NavHost
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.theme.WhiteFoxTheme

class MainActivity : ComponentActivity() {

    private val navViewModel: NavViewModel by constructVM(
        { (application as ServiceProvider).sessionManager })
    { sessionManager ->
        NavViewModel(sessionManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val serviceProvider =
            (application as? ServiceProvider) ?: throw ServiceProviderException()
        val dependency = Dependency(
            database = serviceProvider.database,
            client = serviceProvider.client,
            sessionManager = serviceProvider.sessionManager,
            prefStore = serviceProvider.prefStore,
            gson = serviceProvider.gson,
        )
        setContent {
            WhiteFoxTheme {
                CompositionLocalProvider(
                    LocalDependency provides dependency,
                    LocalNavViewModel provides navViewModel
                ) {
                    NavHost()
                }
            }
        }
    }
}
