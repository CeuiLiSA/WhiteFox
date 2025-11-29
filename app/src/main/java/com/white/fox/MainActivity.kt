package com.white.fox

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import ceui.lisa.hermes.common.saveJsonToDownloads
import ceui.lisa.hermes.db.gson
import ceui.lisa.hermes.viewmodel.constructVM
import com.white.fox.ui.common.Dependency
import com.white.fox.ui.common.LinkHandler
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.NavHost
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.setting.LocalAppLocaleContext
import com.white.fox.ui.theme.WhiteFoxTheme
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val navViewModel by constructVM({ requireSessionManager() }) { sessionManager ->
        NavViewModel(sessionManager)
    }
    private val linkHandler by lazy {
        LinkHandler(navViewModel, (application as ServiceProvider).client::loginWithUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val serviceProvider = (application as? ServiceProvider) ?: throw ServiceProviderException()
        val dependency = Dependency(
            database = serviceProvider.database,
            client = serviceProvider.client,
            sessionManager = serviceProvider.sessionManager,
            settingsManager = serviceProvider.settingsManager,
            prefStore = serviceProvider.prefStore,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            lifecycleScope.launch {
                serviceProvider.sessionManager.stateFlow.collect { session ->
                    if (serviceProvider.sessionManager.loggedInUid() > 0L) {
                        val json = gson.toJson(session)
                        Timber.d("sdaasddasdassw22 start ${json} ")
                        saveJsonToDownloads(this@MainActivity, jsonContent = json)
                        Timber.d("sdaasddasdassw22 end ${json} ")
                    }

                }
            }
        }

        setContent {

            val settingsState = dependency.settingsManager.stateFlow.collectAsState()
            val currentLocale = settingsState.value?.language?.locale ?: Locale.getDefault()
            val localizedConfigurationContext = remember(currentLocale) {
                val config = Configuration(resources.configuration)
                config.setLocale(currentLocale)
                createConfigurationContext(config)
            }

            WhiteFoxTheme {
                CompositionLocalProvider(
                    LocalDependency provides dependency,
                    LocalNavViewModel provides navViewModel,
                    LocalAppLocaleContext provides localizedConfigurationContext,
                ) {
                    NavHost()
                }
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data?.toString() ?: return

        if (linkHandler.processLink(data)) {
            Timber.i("handleIntent Handled deep link: $data")
        } else {
            Timber.w("handleIntent Unhandled deep link: $data")
        }
    }
}
